package com.hyl.zhanmaojbackendquestionservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaojbackendmodel.model.entity.TestQuestion;

/**
* @author Alan
* @description 针对表【test_question(题目题目关联)】的数据库操作Service
* @createDate 2024-03-16 16:58:22
*/
public interface TestQuestionService extends IService<TestQuestion> {

    /**
     * 根据testId删除
     * @param testId
     * @return
     */
    boolean removeByTestId(long testId);


}
