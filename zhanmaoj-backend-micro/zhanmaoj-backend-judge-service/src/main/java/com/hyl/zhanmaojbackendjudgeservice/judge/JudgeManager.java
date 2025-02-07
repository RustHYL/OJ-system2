package com.hyl.zhanmaojbackendjudgeservice.judge;


import com.hyl.zhanmaojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.hyl.zhanmaojbackendjudgeservice.judge.strategy.JudgeContext;
import com.hyl.zhanmaojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.JudgeInfo;
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
