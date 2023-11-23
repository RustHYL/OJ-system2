package com.hyl.zhanmaoj.service;

import com.hyl.zhanmaoj.model.dto.questionsbumit.QuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaoj.model.entity.User;

/**
* @author Alan
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-11-24 02:03:47
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交信息
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
}
