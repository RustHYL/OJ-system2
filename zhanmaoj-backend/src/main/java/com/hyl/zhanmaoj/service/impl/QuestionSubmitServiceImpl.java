package com.hyl.zhanmaoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.model.dto.questionsbumit.JudgeInfo;
import com.hyl.zhanmaoj.model.dto.questionsbumit.QuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.entity.Question;
import com.hyl.zhanmaoj.model.entity.QuestionSubmit;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.service.QuestionService;
import com.hyl.zhanmaoj.service.QuestionSubmitService;
import com.hyl.zhanmaoj.mapper.QuestionSubmitMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author Alan
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2023-11-24 02:03:47
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    /**
     * 提交问题
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //todo 校验编程语言是否合法

        // 判断实体是否存在，根据类别获取实体
        long questionId = questionSubmitAddRequest.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交问题
        long userId = loginUser.getId();
        // 每个用户串行提交问题
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        // todo 设置初始状态
        questionSubmit.setStatus(0);
        questionSubmit.setJudgeInfo("{}");
        QueryWrapper<QuestionSubmit> thumbQueryWrapper = new QueryWrapper<>(questionSubmit);
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return questionSubmit.getId();
    }

}




