package com.hyl.zhanmaoj.judge.strategy;


import com.hyl.zhanmaoj.model.dto.questionsbumit.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
