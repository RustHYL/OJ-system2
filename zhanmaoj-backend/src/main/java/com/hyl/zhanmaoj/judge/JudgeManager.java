package com.hyl.zhanmaoj.judge;

import com.hyl.zhanmaoj.judge.strategy.DefaultJudgeStrategy;
import com.hyl.zhanmaoj.judge.strategy.JudgeContext;
import com.hyl.zhanmaoj.judge.strategy.JudgeStrategy;
import com.hyl.zhanmaoj.judge.codesandbox.model.JudgeInfo;
import org.springframework.stereotype.Service;

/**
 * 管理判题策略
 */
@Service
public class JudgeManager {
    JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        return judgeStrategy.doJudge(judgeContext);
    }
}
