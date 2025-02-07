package com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox.impl;


import com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.JudgeInfo;
import com.hyl.zhanmaojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.hyl.zhanmaojbackendmodel.model.enums.QuestionSubmitStatusEnum;
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
