package com.hyl.zhanmaojbackendquestionservice.controller.inner;


import com.hyl.zhanmaojbackendmodel.model.entity.Question;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionWrong;
import com.hyl.zhanmaojbackendquestionservice.service.QuestionService;
import com.hyl.zhanmaojbackendquestionservice.service.QuestionSubmitService;
import com.hyl.zhanmaojbackendquestionservice.service.QuestionWrongService;
import com.hyl.zhanmaojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionWrongService questionWrongService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    @PostMapping("/update/id")
    public boolean updateQuestionById(Question question) {
        return questionService.updateById(question);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update/id")
    public boolean updateQuestionSubmitById(QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    @PostMapping("/question_wrong/save")
    public boolean saveQuestionWrong(QuestionWrong questionWrong) {
        return questionWrongService.save(questionWrong);
    }
}
