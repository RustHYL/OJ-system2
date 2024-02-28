package com.hyl.zhanmaoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyl.zhanmaoj.common.BaseResponse;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.common.ResultUtils;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.model.dto.questionsbumit.QuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.questionsbumit.QuestionSubmitQueryRequest;
import com.hyl.zhanmaoj.model.entity.QuestionSubmit;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.model.vo.QuestionSubmitVO;
import com.hyl.zhanmaoj.service.QuestionSubmitService;
import com.hyl.zhanmaoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/lihyl">程序员鱼皮</a>
 * @from <a href="https://hyl.icu">编程导航知识星球</a>
 */
@RestController
//@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录 id
     */
//    @PostMapping("/")
//    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
//            HttpServletRequest request) {
//        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        final User loginUser = userService.getLoginUser(request);
//        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
//        return ResultUtils.success(questionSubmitId);
//    }

    /**
     * 分页获取题目提交列表（管理员可以看到全部，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
//    @PostMapping("/list/page")
//    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
//                                                                         HttpServletRequest request) {
//        long current = questionSubmitQueryRequest.getCurrent();
//        long size = questionSubmitQueryRequest.getPageSize();
//        final User loginUser = userService.getLoginUser(request);
//        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
//                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
//        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
//    }


}
