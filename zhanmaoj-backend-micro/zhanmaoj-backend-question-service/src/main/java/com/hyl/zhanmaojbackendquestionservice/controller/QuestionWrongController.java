package com.hyl.zhanmaojbackendquestionservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.hyl.zhanmaojbackendcommon.common.BaseResponse;
import com.hyl.zhanmaojbackendcommon.common.DeleteRequest;
import com.hyl.zhanmaojbackendcommon.common.ErrorCode;
import com.hyl.zhanmaojbackendcommon.common.ResultUtils;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendcommon.exception.ThrowUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.questionWrong.QuestionWrongQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionWrong;
import com.hyl.zhanmaojbackendmodel.model.vo.QuestionWrongVO;
import com.hyl.zhanmaojbackendquestionservice.service.QuestionWrongService;
import com.hyl.zhanmaojbackenduserservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 题目接口
 *
 */
@RestController
@RequestMapping("/question_wrong")
@Slf4j
public class QuestionWrongController {

    @Resource
    private QuestionWrongService questionWrongService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();


    /**
     * 分页获取我的列表（封装类）
     *
     * @param questionWrongQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/my/page/vo")
    public BaseResponse<Page<QuestionWrongVO>> listMyQuestionWrongVOByPage(@RequestBody QuestionWrongQueryRequest questionWrongQueryRequest,
                                                                           HttpServletRequest request) {
        long current = questionWrongQueryRequest.getCurrent();
        long size = questionWrongQueryRequest.getPageSize();
        Long num = 0L;
        if (questionWrongQueryRequest.getNum() != null){
            num = questionWrongQueryRequest.getNum();
        }
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        questionWrongQueryRequest.setUserId(userService.getLoginUser(request).getId());
        Page<QuestionWrong> questionWrongPage = new Page<>();
        if (num != 0){
            questionWrongPage = questionWrongService.page(new Page<>(current, num),
                    questionWrongService.getQueryWrapper(questionWrongQueryRequest));
        } else {
            questionWrongPage = questionWrongService.page(new Page<>(current, size),
                    questionWrongService.getQueryWrapper(questionWrongQueryRequest));
        }
        return ResultUtils.success(questionWrongService.getQuestionWrongVOPage(questionWrongPage, request));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionWrong(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        Long submitId = deleteRequest.getId();
        if (submitId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<QuestionWrong> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("submitId", submitId);
        boolean remove = questionWrongService.remove(queryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

}
