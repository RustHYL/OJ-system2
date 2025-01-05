package com.hyl.zhanmaojbackendquestionservice.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaojbackendmodel.model.dto.question.QuestionQueryAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.Question;
import com.hyl.zhanmaojbackendmodel.model.vo.QuestionTestDetailVO;
import com.hyl.zhanmaojbackendmodel.model.vo.QuestionTitleVO;
import com.hyl.zhanmaojbackendmodel.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Alan
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-11-24 02:03:05
*/
public interface QuestionService extends IService<Question> {
    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取查询条件
     *
     * @param questionQueryAdminRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryAdminRequest questionQueryAdminRequest);

    /**
     * 随机获取n条数据
     * @param num
     * @return
     */
    List<Question> getRandomQuestionList(Integer num);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);

    /**
     * 获取试卷题目信息列表
     * @param testId
     * @return
     */
    List<QuestionTestDetailVO> getQuestionTestDetailList(long testId, HttpServletRequest request);

    /**
     * 获取题目id title type列表
     * @param testId
     * @return
     */
    List<QuestionTitleVO> getQuestionTitleList(long testId);
}
