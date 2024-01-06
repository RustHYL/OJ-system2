package com.hyl.zhanmaojcodesandbox.utils;

import com.hyl.zhanmaojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 进程工具类
 */
public class ProcessUtils {

    /**
     * 执行进程并且获取信息
     * @param runProcess
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            //等待程序执行，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            //正常退出
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                //分批获取正常进程输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String compileOutput;
                //逐行读取
                while ((compileOutput = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutput);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
            } else {
                System.out.println(opName + "失败:" + exitValue);
                //分批获取正常进程输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String compileOutput;
                //逐行读取
                while ((compileOutput = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutput);
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
                //分批获取异常进程输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                StringBuilder errorCompileOutputStringBuilder = new StringBuilder();
                String errorCompileOutput;
                //逐行读取
                while ((errorCompileOutput = errorBufferedReader.readLine()) != null) {
                    errorCompileOutputStringBuilder.append(errorCompileOutput);
                }
                executeMessage.setErrorMessage(errorCompileOutputStringBuilder.toString());
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getTotalTimeMillis());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return executeMessage;
    }
}
