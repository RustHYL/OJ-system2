package com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox.impl;


import com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeResponse;

public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
