package com.hyl.zhanmaojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeRequest;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeResponse;
import com.hyl.zhanmaojcodesandbox.model.ExecuteMessage;
import com.hyl.zhanmaojcodesandbox.model.JudgeInfo;
import com.hyl.zhanmaojcodesandbox.utils.ProcessUtils;
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
import java.util.stream.Collectors;

@Component
public class JavaDockerCodeSandboxOld implements CodeSandbox {

    private static final String GLOBAL_CODE_DIR_PATH = "tmpCode";

    private static final String Global_JAVA_CODE_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;

    public static final Boolean FIRST_INIT = true;


    public static void main(String[] args) {
        JavaDockerCodeSandboxOld javaNativeCodeSandbox = new JavaDockerCodeSandboxOld();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "3 4"));
        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);

    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        String userDir = System.getProperty("user.dir");
        String globalCodePath = userDir + File.separator + GLOBAL_CODE_DIR_PATH;
        //判断目录是否存在，不存在就新建
        if (!FileUtil.exist(globalCodePath)) {
            FileUtil.mkdir(globalCodePath);
        }

        //把用户代码隔离存放
        String userCodeParentPath = globalCodePath + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + Global_JAVA_CODE_NAME;
        File userCodeFile = FileUtil.writeUtf8String(code, userCodePath);

        //编译代码
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            return this.getErrorResponse(e);
        }

        //创建容器，把容器复制到文件内
        //获取默认的docker client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
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


        //收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            //如果有错误直接设置响应为错误，响应信息为错误信息
            //todo 改造设置成输出全部output（不管错误或者成功），可以用于打分（错一部分）
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                //用户提交的代码错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
        }
        executeCodeResponse.setOutputList(outputList);
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        JudgeInfo judgeInfo = new JudgeInfo();
        //时间使用最大值,容易和判题标准时间作比较，进行判题
        judgeInfo.setTime(executeMessageList.stream().mapToLong(ExecuteMessage::getTime).max().getAsLong());
        judgeInfo.setMemory(executeMessageList.stream().mapToLong(ExecuteMessage::getMemory).max().getAsLong());
        judgeInfo.setMessage(executeMessageList.stream().map(executeMessage -> {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                return errorMessage;
            }
            return executeMessage.getMessage();
        }).collect(Collectors.joining(",")));
        executeCodeResponse.setJudgeInfo(judgeInfo);
        //文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            if (del) {
                System.out.println("删除文件夹成功");
            } else {
                System.out.println("删除文件夹失败");
            }
        }
        return executeCodeResponse;
    }

    /**
     * 错误响应
     * @param e
     * @return ExecuteCodeResponse
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        //程序执行错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
