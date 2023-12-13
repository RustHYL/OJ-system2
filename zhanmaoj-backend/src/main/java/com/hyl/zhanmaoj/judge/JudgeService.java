package com.hyl.zhanmaoj.judge;


import com.hyl.zhanmaoj.model.vo.QuestionSubmitVO;

public interface JudgeService {

    QuestionSubmitVO doJudge(long questionSubmitId);
}
