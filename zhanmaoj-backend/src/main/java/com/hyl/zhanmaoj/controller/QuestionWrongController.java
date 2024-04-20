package com.hyl.zhanmaoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyl.zhanmaoj.common.BaseResponse;
import com.hyl.zhanmaoj.common.DeleteRequest;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.common.ResultUtils;
import com.hyl.zhanmaoj.constant.UserConstant;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.exception.ThrowUtils;
import com.hyl.zhanmaoj.judge.codesandbox.model.JudgeInfo;
import com.hyl.zhanmaoj.model.dto.question.*;
import com.hyl.zhanmaoj.model.dto.questionWrong.QuestionWrongQueryRequest;
import com.hyl.zhanmaoj.model.dto.questionsbumit.*;
import com.hyl.zhanmaoj.model.entity.Question;
import com.hyl.zhanmaoj.model.entity.QuestionSubmit;
import com.hyl.zhanmaoj.model.entity.QuestionWrong;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.model.enums.QuestionSubmitStatusEnum;
import com.hyl.zhanmaoj.model.vo.QuestionSubmitVO;
import com.hyl.zhanmaoj.model.vo.QuestionVO;
import com.hyl.zhanmaoj.model.vo.QuestionWrongVO;
import com.hyl.zhanmaoj.service.QuestionService;
import com.hyl.zhanmaoj.service.QuestionSubmitService;
import com.hyl.zhanmaoj.service.QuestionWrongService;
import com.hyl.zhanmaoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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



}
