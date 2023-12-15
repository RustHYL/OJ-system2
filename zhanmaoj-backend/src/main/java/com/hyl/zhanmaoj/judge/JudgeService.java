package com.hyl.zhanmaoj.judge;


import com.hyl.zhanmaoj.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
