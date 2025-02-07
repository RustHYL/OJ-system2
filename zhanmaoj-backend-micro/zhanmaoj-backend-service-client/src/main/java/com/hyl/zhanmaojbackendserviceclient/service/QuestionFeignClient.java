package com.hyl.zhanmaojbackendserviceclient.service;


import com.hyl.zhanmaojbackendmodel.model.entity.Question;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionWrong;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
* @author Alan
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-11-24 02:03:05
*/
@FeignClient(name = "zhanmaoj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);


    @PostMapping("/update/id")
    boolean updateQuestionById(@RequestBody Question question);

    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("/question_submit/update/id")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    @PostMapping("/question_wrong/save")
    boolean saveQuestionWrong(@RequestBody QuestionWrong questionWrong);

}
