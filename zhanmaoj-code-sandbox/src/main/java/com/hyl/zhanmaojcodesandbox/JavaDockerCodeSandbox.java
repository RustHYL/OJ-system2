package com.hyl.zhanmaojcodesandbox;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.hyl.zhanmaojcodesandbox.model.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JavaDockerCodeSandbox extends JavaCodeSandboxTemplate {

    private static final long TIME_OUT = 20000L;

    public static final Boolean FIRST_INIT = true;


    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println(executeCodeRequest);
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        File userCodeFile = null;
        try {
            //1.把用户的代码保存为文件
            userCodeFile = saveCodeToFile(code);
            //2.编译代码
            ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
            System.out.println(compileFileExecuteMessage);
            //3.执行程序,得到输出结果
            List<ExecuteMessage> executeMessageList = runFile(inputList, userCodeFile);
            //4.收集整理输出结果
            executeCodeResponse = getOutputResponse(executeMessageList);
        } catch (RuntimeException e){
            // 处理异常的代码块
            log.error("Exception caught: " + e.getMessage());
            executeCodeResponse.setMessage(e.getMessage());
            //用户提交的代码错误
            executeCodeResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
            // catch块之后的代码会继续执行
        }
        //5.清理文件
        if (userCodeFile != null && !deleteFile(userCodeFile)) {
            log.error("delete file error, userCodeFilePath={}", userCodeFile.getAbsolutePath());
        }
        return executeCodeResponse;
    }

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
            // 列出所有镜像，并使用过滤器查找特定的镜像
            List<Image> images = dockerClient.listImagesCmd()
                    .withImageNameFilter(image)
                    .exec();

            // 检查是否找到了镜像
            boolean imageExists = images.stream()
                    .anyMatch(img -> img.getRepoTags() != null &&
                            img.getRepoTags().length > 0 &&
                            img.getRepoTags()[0].equals(image));
            if (!imageExists){
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
                    throw new RuntimeException("pull image error");
                }
            }
        }
        System.out.println("镜像拉取完成");

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
            final String[] errorMessages = {null};
            long time = 0L;
            final boolean[] timeout = {true};
            String execId = execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                //如果没有超时则执行完成，会触发下面的方法
                @Override
                public void onComplete() {
                    timeout[0] = false;
                    super.onComplete();
                }

                @Override
                public void onNext(Frame frame) {
                    String payload = new String(frame.getPayload(), StandardCharsets.UTF_8).trim();
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)){
                        errorMessage[0] = payload;
                        errorMessages[0] = (errorMessages[0] == null) ? errorMessage[0] : errorMessages[0] + "\n" + errorMessage[0];
                        System.out.println("输出错误:" + errorMessage[0]);
                    } else {
                        if (!payload.isEmpty()) { // 确保不是空白行
                            message[0] = payload;
                            System.out.println("输出结果:" + message[0]);
                        }
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
                    if(statistics.getMemoryStats().getUsage() != null){
                        Long memoryUse = statistics.getMemoryStats().getUsage();
                        System.out.println("内存占用:" + memoryUse);
                        maxMemory[0] = Math.max(maxMemory[0], memoryUse);
                    }
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
                        .awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
                stopWatch.stop();
                time = stopWatch.getLastTaskTimeMillis();
                statsCmd.close();
            } catch (InterruptedException e) {
                System.out.println("程序执行异常");
                throw new RuntimeException(e);
            }
            System.out.println("MaxMemory:" + maxMemory[0]);
            System.out.println("errorMessages =====" + errorMessages[0]);
            executeMessage.setErrorMessage(errorMessages[0]);
            executeMessage.setMessage(message[0]);
            executeMessage.setTime(time);
            executeMessage.setMemory(maxMemory[0]);
            System.out.println("executeMsg:" + executeMessage);
            executeMessageList.add(executeMessage);
        }
        //关闭容器
        //todo 可优化 什么时候关闭，多次运行一个题目是否需要关闭
        dockerClient.stopContainerCmd(containerId).exec();
        return executeMessageList;
    }


    @Override
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            //如果有错误直接设置响应为错误，响应信息为错误信息
            //todo 改造设置成输出全部output（不管错误或者成功），可以用于打分（错一部分）
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                //用户提交的代码错误
                executeCodeResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
                break;
            }
            outputList.add(executeMessage.getMessage());
        }
        executeCodeResponse.setOutputList(outputList);
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
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
            return null;
        }).collect(Collectors.joining(",")));
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
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
