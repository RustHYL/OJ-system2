package com.hyl.zhanmaoj.judge.codesandbox.impl;

import com.hyl.zhanmaoj.judge.codesandbox.CodeSandbox;
import com.hyl.zhanmaoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.hyl.zhanmaoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.hyl.zhanmaoj.judge.codesandbox.model.JudgeInfo;
import com.hyl.zhanmaoj.model.enums.JudgeInfoMessageEnum;
import com.hyl.zhanmaoj.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
