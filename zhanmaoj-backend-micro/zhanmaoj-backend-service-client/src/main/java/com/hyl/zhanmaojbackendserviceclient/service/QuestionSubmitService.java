package com.hyl.zhanmaojbackendserviceclient.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaojbackendmodel.model.dto.questionsbumit.QuestionSubmitAddRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.questionsbumit.QuestionSubmitQueryAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.questionsbumit.QuestionSubmitQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.User;
import com.hyl.zhanmaojbackendmodel.model.vo.QuestionSubmitVO;

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

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryAdminRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryAdminRequest questionSubmitQueryAdminRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);




}
