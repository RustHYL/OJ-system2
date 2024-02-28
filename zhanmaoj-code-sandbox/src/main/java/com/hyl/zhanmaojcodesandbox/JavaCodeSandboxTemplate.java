package com.hyl.zhanmaojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeRequest;
import com.hyl.zhanmaojcodesandbox.model.ExecuteCodeResponse;
import com.hyl.zhanmaojcodesandbox.model.ExecuteMessage;
import com.hyl.zhanmaojcodesandbox.model.JudgeInfo;
import com.hyl.zhanmaojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandbox{

    private static final String GLOBAL_CODE_DIR_PATH = "tmpCode";

    private static final String Global_JAVA_CODE_NAME = "Main.java";

    private static final String SECURITY_MANAGER_PATH = "D:\\code\\OJSystem\\OJ-system\\zhanmaoj-code-sandbox\\src\\main\\resources\\security";

    private static final String SECURITY_MANAGER_CLASS_NAME = "DefaultSecurityManager";

    private static final long TIME_OUT = 5000L;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        //1.把用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code);
        //2.编译代码
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println(compileFileExecuteMessage);
        //3.执行程序,得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(inputList, userCodeFile);
        //4.收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = getOutputResponse(executeMessageList);
        //5.清理文件
        if (!deleteFile(userCodeFile)) {
            log.error("delete file error, userCodeFilePath={}", userCodeFile.getAbsolutePath());
        }
        return executeCodeResponse;
    }

    /**
     * 保存代码文件到指定目录
     * @param code 代码
     * @return 代码文件
     */
    public File saveCodeToFile(String code){
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
        return userCodeFile;
    }

    /**
     * 编译代码
     * @param userCodeFile 代码文件
     * @return 运行结果
     */
    public ExecuteMessage compileFile(File userCodeFile){
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue() != 0){
                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (Exception e) {
//            return this.getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 运行代码
     * @param inputList 输入示例
     * @param userCodeFile 代码文件
     * @return 运行结果列表
     */
    public List<ExecuteMessage> runFile(List<String> inputList, File userCodeFile){
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
//            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGER_PATH, SECURITY_MANAGER_CLASS_NAME, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("超时了，中断");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                        //return this.getErrorResponse(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                //todo 内存
                executeMessage.setMemory(0L);
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                //return this.getErrorResponse(e);
                throw new RuntimeException("程序执行错误", e);
            }
        }
        return executeMessageList;
    }

    /**
     * 整理结果
     * @param executeMessageList 运行结果列表
     * @return 程序运行结果
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList){
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
            if (StrUtil.isBlank(errorMessage)) {
                return errorMessage;
            }
            return executeMessage.getMessage();
        }).collect(Collectors.joining(",")));
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 删除文件
     * @param userCodeFile 代码文件
     * @return 是否删除成功
     */
    public boolean deleteFile(File userCodeFile){
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            if (del) {
                System.out.println("删除文件夹成功");
            } else {
                System.out.println("删除文件夹失败");
            }
            return del;
        }
        return true;
    }


    /**
     * 获取程序运行错误响应
     * @param e 运行错误
     * @return 运行结果
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

