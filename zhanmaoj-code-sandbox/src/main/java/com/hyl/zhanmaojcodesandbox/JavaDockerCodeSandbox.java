package com.hyl.zhanmaojcodesandbox;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeRequest;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeResponse;
import com.hyl.zhanmaojcodesandbox.model.ExecuteMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JavaDockerCodeSandbox extends JavaCodeSandboxTemplate {

    private static final long TIME_OUT = 5000L;

    public static final Boolean FIRST_INIT = true;


    /**
     * 容器创建运行，执行程序
     * @param inputList 输入示例
     * @param userCodeFile 代码文件
     * @return 结果列表
     */
    @Override
    public List<ExecuteMessage> runFile(List<String> inputList, File userCodeFile) {
        //创建容器，把容器复制到文件内
        //获取默认的docker client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        //todo 优化：第一次需要拉取镜像，第二三次不需要拉取：单次任务？？
        String image = "openjdk:8-alpine";
        if (FIRST_INIT){
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    System.out.println("下载镜像" + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd
                        .exec(pullImageResultCallback)
                        .awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("拉取镜像异常");
                throw new RuntimeException(e);
            }
        }
        System.out.println("镜像下载完成");

        //创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        //容器配置
        HostConfig hostConfig = new HostConfig();
        //设定代码目录与映射到的容器的目录，把本地的本地同步到容器的文件，可以让容器访问（容器挂载目录）
        hostConfig.setBinds(new Bind(userCodeParentPath, new Volume("/appDate")));
        //设置容器内存
        hostConfig.withMemory(100 * 1000 * 1000L);
        //设置容器使用的CPU数
        hostConfig.withCpuCount(1L);
        CreateContainerResponse createConfigResponse = containerCmd
                .withHostConfig(hostConfig)
                //限制用户无法在根目录写文件
                .withReadonlyRootfs(true)
                //设置无法访问网络
                .withNetworkDisabled(true)
                //docker容器和本地终端连接，使得终端能够输入
                .withAttachStdin(true)
                //终端能够获取输出
                .withAttachStdout(true)
                //终端能够获取错误
                .withAttachStderr(true)
                //创建一个交互终端
                .withTty(true)
                .exec();

        String containerId = createConfigResponse.getId();

        //启动容器
        dockerClient.startContainerCmd(containerId).exec();

        //执行程序并且获取结果
        //docker exec xxx java -cp /appDate Main 1 3
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            StopWatch stopWatch = new StopWatch();
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/appDate", "Main"}, inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);

            //返回结果
            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = {null};
            final String[] errorMessage = {null};
            long time = 0L;
            final boolean[] timeout = {true};
            String execId = execCreateCmdResponse.getId();
            if (execId.isEmpty()) {
                System.out.println("执行id无法获取");
                return null;
            }
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                //如果没有超时则执行完成，会触发下面的方法
                @Override
                public void onComplete() {
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)){
                        errorMessage[0] = new String(frame.getPayload());
                        System.out.println("输出错误：" + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果：" + message[0]);
                    }
                    super.onNext(frame);
                }
            };

            final long[] maxMemory = {0L};
            //获取内存占用
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = statsCmd.exec(new ResultCallback<Statistics>() {
                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onNext(Statistics statistics) {
                    Long memoryUse = statistics.getMemoryStats().getUsage();
                    System.out.println("内存占用:" + memoryUse);
                    maxMemory[0] = Math.max(maxMemory[0], memoryUse);
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            });
            statsCmd.exec(statisticsResultCallback);

            try {
                stopWatch.start();
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        .awaitCompletion(TIME_OUT, TimeUnit.MICROSECONDS);
                stopWatch.stop();
                time = stopWatch.getLastTaskTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            executeMessage.setErrorMessage(errorMessage[0]);
            executeMessage.setMessage(message[0]);
            executeMessage.setTime(time);
            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);
        }
        //关闭容器
        //todo 可优化 什么时候关闭，多次运行一个题目是否需要关闭
        dockerClient.stopContainerCmd(containerId).exec();
        return executeMessageList;
    }

    public static void main(String[] args) {
        JavaDockerCodeSandbox javaNativeCodeSandbox = new JavaDockerCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "3 4"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}

