package com.hyl.zhanmaojbackendjudgeservice.judge.strategy;


import com.hyl.zhanmaojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
