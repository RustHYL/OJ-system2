package com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox;


import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {
    /**
     * 执行程序
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
