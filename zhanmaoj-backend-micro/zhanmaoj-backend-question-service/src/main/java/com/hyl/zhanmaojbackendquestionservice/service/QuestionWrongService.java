package com.hyl.zhanmaojbackendquestionservice.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyl.zhanmaojbackendmodel.model.dto.questionWrong.QuestionWrongQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionWrong;
import com.hyl.zhanmaojbackendmodel.model.vo.QuestionWrongVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Alan
* @description 针对表【question_wrong(错题表)】的数据库操作Service
* @createDate 2024-03-26 13:25:34
*/
public interface QuestionWrongService extends IService<QuestionWrong> {


    /**
     * 获取查询条件
     *
     * @param questionWrongQueryRequest
     * @return
     */
    QueryWrapper<QuestionWrong> getQueryWrapper(QuestionWrongQueryRequest questionWrongQueryRequest);

    /**
     * 分页获取题目封装
     *
     * @param questionWrongPage
     * @param request
     * @return
     */
    Page<QuestionWrongVO> getQuestionWrongVOPage(Page<QuestionWrong> questionWrongPage, HttpServletRequest request);

}
