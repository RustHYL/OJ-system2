package com.hyl.zhanmaoj.judge.strategy;


import com.hyl.zhanmaoj.judge.codesandbox.model.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
