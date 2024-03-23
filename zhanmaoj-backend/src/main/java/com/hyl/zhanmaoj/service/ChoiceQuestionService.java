package com.hyl.zhanmaoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.zhanmaoj.model.dto.choicequestion.ChoiceQuestionQueryAdminRequest;
import com.hyl.zhanmaoj.model.dto.choicequestion.ChoiceQuestionQueryRequest;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionTestDetailVO;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionTitleVO;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Alan
* @description 针对表【choice_question(选择题目)】的数据库操作Service
* @createDate 2024-03-15 06:12:21
*/
public interface ChoiceQuestionService extends IService<ChoiceQuestion> {


    /**
     * 校验数据
     * @param choiceQuestion
     * @param add
     */
    void validChoiceQuestion(ChoiceQuestion choiceQuestion, boolean add);

    /**
     * 随机获取n条数据
     * @param num
     * @return
     */
    List<ChoiceQuestion> getRandomChoiceQuestionList(Integer num);

    /**
     * 获取查询条件
     *
     * @param choiceQuestionQueryRequest
     * @return
     */
    QueryWrapper<ChoiceQuestion> getQueryWrapper(ChoiceQuestionQueryRequest choiceQuestionQueryRequest);

    QueryWrapper<ChoiceQuestion> getQueryWrapper(ChoiceQuestionQueryAdminRequest choiceQuestionQueryAdminRequest);

    /**
     * 获取题目封装
     *
     * @param choiceQuestion
     * @param request
     * @return
     */
    ChoiceQuestionVO getChoiceQuestionVO(ChoiceQuestion choiceQuestion, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param choiceQuestionPage
     * @param request
     * @return
     */
    Page<ChoiceQuestionVO> getChoiceQuestionVOPage(Page<ChoiceQuestion> choiceQuestionPage, HttpServletRequest request);

    /**
     * 获取试卷中选择题信息
     * @param testId
     * @return
     */
    List<ChoiceQuestionTestDetailVO> getChoiceQuestonTestDetailList(long testId);

    List<ChoiceQuestionTitleVO> getChoiceQuestonTitleList(long testId);
}
