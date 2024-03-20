package com.hyl.zhanmaoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitQueryAdminRequest;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitQueryRequest;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionSubmitVO;

/**
* @author Alan
* @description 针对表【choice_question_submit(选择题提交信息)】的数据库操作Service
* @createDate 2024-03-15 05:45:06
*/
public interface ChoiceQuestionSubmitService extends IService<ChoiceQuestionSubmit> {

    /**
     * 题目提交信息
     *
     * @param choiceQuestionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doChoiceQuestionSubmit(ChoiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param choiceQuestionSubmitQueryRequest
     * @return
     */
    QueryWrapper<ChoiceQuestionSubmit> getQueryWrapper(ChoiceQuestionSubmitQueryRequest choiceQuestionSubmitQueryRequest);

    /**
     * 获取查询条件
     *
     * @param choiceQuestionSubmitQueryAdminRequest
     * @return
     */
    QueryWrapper<ChoiceQuestionSubmit> getQueryWrapper(ChoiceQuestionSubmitQueryAdminRequest choiceQuestionSubmitQueryAdminRequest);

    /**
     * 获取题目封装
     *
     * @param choiceQuestionSubmit
     * @param loginUser
     * @return
     */
    ChoiceQuestionSubmitVO getChoiceQuestionSubmitVO(ChoiceQuestionSubmit choiceQuestionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param choiceQuestionSubmitPage
     * @param loginUser
     * @return
     */
    Page<ChoiceQuestionSubmitVO> getChoiceQuestionSubmitVOPage(Page<ChoiceQuestionSubmit> choiceQuestionSubmitPage, User loginUser);

}
