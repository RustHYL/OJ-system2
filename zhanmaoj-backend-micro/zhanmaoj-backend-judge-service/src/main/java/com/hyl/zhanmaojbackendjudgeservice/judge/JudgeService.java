package com.hyl.zhanmaojbackendjudgeservice.judge;


import com.hyl.zhanmaojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {

    QuestionSubmit doJudge(long questionSubmitId);
}
