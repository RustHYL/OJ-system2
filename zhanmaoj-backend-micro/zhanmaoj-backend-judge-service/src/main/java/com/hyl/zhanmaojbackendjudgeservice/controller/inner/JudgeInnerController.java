package com.hyl.zhanmaojbackendjudgeservice.controller.inner;


import com.hyl.zhanmaojbackendjudgeservice.judge.JudgeService;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionSubmit;
import com.hyl.zhanmaojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
