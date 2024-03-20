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
import com.hyl.zhanmaoj.model.dto.choicequestion.*;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitQueryAdminRequest;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitQueryRequest;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitUpdateAdminRequest;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestionSubmit;
import com.hyl.zhanmaoj.model.entity.User;
import com.hyl.zhanmaoj.model.enums.StatusEnum;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionSubmitVO;
import com.hyl.zhanmaoj.model.vo.ChoiceQuestionVO;
import com.hyl.zhanmaoj.service.ChoiceQuestionService;
import com.hyl.zhanmaoj.service.ChoiceQuestionSubmitService;
import com.hyl.zhanmaoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目接口
 *
 */
@RestController
@RequestMapping("/choiceQuestion")
@Slf4j
public class ChoiceQuestionController {

    @Resource
    private ChoiceQuestionService choiceQuestionService;

    @Resource
    private ChoiceQuestionSubmitService choiceQuestionSubmitService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param choiceQuestionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChoiceQuestion(@RequestBody ChoiceQuestionAddRequest choiceQuestionAddRequest, HttpServletRequest request) {
        if (choiceQuestionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestion choiceQuestion = new ChoiceQuestion();
        BeanUtils.copyProperties(choiceQuestionAddRequest, choiceQuestion);
        List<String> tags = choiceQuestionAddRequest.getTags();
        if (tags != null) {
            choiceQuestion.setTags(GSON.toJson(tags));
        }
        User loginUser = userService.getLoginUser(request);
        choiceQuestion.setUserId(loginUser.getId());
        choiceQuestion.setSubmitNum(0);
        choiceQuestion.setAcceptedNum(0);
        boolean result = choiceQuestionService.save(choiceQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChoiceQuestionId = choiceQuestion.getId();
        return ResultUtils.success(newChoiceQuestionId);
    }

    @PostMapping("/add/backend")
    public BaseResponse<Long> addChoiceQuestionBackend(@RequestBody ChoiceQuestionAddAdminRequest choiceQuestionAddRequest, HttpServletRequest request) {
        if (choiceQuestionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestion choiceQuestion = new ChoiceQuestion();
        BeanUtils.copyProperties(choiceQuestionAddRequest, choiceQuestion);
        String tags = choiceQuestionAddRequest.getTags();
        List<String> tagsList = new ArrayList<>();
        if (StringUtils.isNotBlank(tags)) {
            try {
                tagsList = GSON.fromJson(tags, List.class);
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + tagsList);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入标签格式错误");
            }
        } else {
            tags = tagsList.toString();
        }
        User loginUser = userService.getLoginUser(request);
        choiceQuestion.setUserId(loginUser.getId());
        choiceQuestion.setTags(tags);
        boolean result = choiceQuestionService.save(choiceQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChoiceQuestionId = choiceQuestion.getId();
        return ResultUtils.success(newChoiceQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChoiceQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        ChoiceQuestion oldChoiceQuestion = choiceQuestionService.getById(id);
        ThrowUtils.throwIf(oldChoiceQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChoiceQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = choiceQuestionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param choiceQuestionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateChoiceQuestion(@RequestBody ChoiceQuestionUpdateRequest choiceQuestionUpdateRequest) {
        if (choiceQuestionUpdateRequest == null || choiceQuestionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestion choiceQuestion = new ChoiceQuestion();
        BeanUtils.copyProperties(choiceQuestionUpdateRequest, choiceQuestion);
        List<String> tags = choiceQuestionUpdateRequest.getTags();
        if (tags != null) {
            choiceQuestion.setTags(GSON.toJson(tags));
        }
        // 参数校验
        choiceQuestionService.validChoiceQuestion(choiceQuestion, false);
        long id = choiceQuestionUpdateRequest.getId();
        // 判断是否存在
        ChoiceQuestion oldChoiceQuestion = choiceQuestionService.getById(id);
        ThrowUtils.throwIf(oldChoiceQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = choiceQuestionService.updateById(choiceQuestion);
        return ResultUtils.success(result);
    }

    @PostMapping("/update/backend")
    public BaseResponse<Boolean> updateChoiceQuestionBackend(@RequestBody ChoiceQuestionUpdateAdminRequest choiceQuestionUpdateRequest) {
        if (choiceQuestionUpdateRequest == null || choiceQuestionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestion choiceQuestion = new ChoiceQuestion();
        BeanUtils.copyProperties(choiceQuestionUpdateRequest, choiceQuestion);
        String tags = choiceQuestionUpdateRequest.getTags();
        List<String> tagsList = new ArrayList<>();
        if (StringUtils.isNotBlank(tags)) {
            try {
                tagsList = GSON.fromJson(tags, List.class);
                // 如果转换成功，这里会执行
                log.info("Conversion successful: " + tagsList);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入标签格式错误");
            }
        }
        choiceQuestion.setTags(GSON.toJson(tagsList));
        // 参数校验
        choiceQuestionService.validChoiceQuestion(choiceQuestion, false);
        long id = choiceQuestionUpdateRequest.getId();
        // 判断是否存在
        ChoiceQuestion oldChoiceQuestion = choiceQuestionService.getById(id);
        ThrowUtils.throwIf(oldChoiceQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = choiceQuestionService.updateById(choiceQuestion);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ChoiceQuestionVO> getChoiceQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestion choiceQuestion = choiceQuestionService.getById(id);
        if (choiceQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(choiceQuestionService.getChoiceQuestionVO(choiceQuestion, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param choiceQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ChoiceQuestionVO>> listChoiceQuestionVOByPage(@RequestBody ChoiceQuestionQueryRequest choiceQuestionQueryRequest,
            HttpServletRequest request) {
        long current = choiceQuestionQueryRequest.getCurrent();
        long size = choiceQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ChoiceQuestion> choiceQuestionPage = choiceQuestionService.page(new Page<>(current, size),
                choiceQuestionService.getQueryWrapper(choiceQuestionQueryRequest));
        return ResultUtils.success(choiceQuestionService.getChoiceQuestionVOPage(choiceQuestionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param choiceQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ChoiceQuestionVO>> listMyChoiceQuestionVOByPage(@RequestBody ChoiceQuestionQueryRequest choiceQuestionQueryRequest,
            HttpServletRequest request) {
        if (choiceQuestionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        choiceQuestionQueryRequest.setUserId(loginUser.getId());
        long current = choiceQuestionQueryRequest.getCurrent();
        long size = choiceQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ChoiceQuestion> choiceQuestionPage = choiceQuestionService.page(new Page<>(current, size),
                choiceQuestionService.getQueryWrapper(choiceQuestionQueryRequest));
        return ResultUtils.success(choiceQuestionService.getChoiceQuestionVOPage(choiceQuestionPage, request));
    }


    /**
     * 编辑（用户）
     *
     * @param choiceQuestionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChoiceQuestion(@RequestBody ChoiceQuestionEditRequest choiceQuestionEditRequest, HttpServletRequest request) {
        if (choiceQuestionEditRequest == null || choiceQuestionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestion choiceQuestion = new ChoiceQuestion();
        BeanUtils.copyProperties(choiceQuestionEditRequest, choiceQuestion);
        List<String> tags = choiceQuestionEditRequest.getTags();
        if (tags != null) {
            choiceQuestion.setTags(GSON.toJson(tags));
        }
        // 参数校验
        choiceQuestionService.validChoiceQuestion(choiceQuestion, false);
        User loginUser = userService.getLoginUser(request);
        long id = choiceQuestionEditRequest.getId();
        // 判断是否存在
        ChoiceQuestion oldChoiceQuestion = choiceQuestionService.getById(id);
        ThrowUtils.throwIf(oldChoiceQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChoiceQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = choiceQuestionService.updateById(choiceQuestion);
        return ResultUtils.success(result);
    }

    @PostMapping("/list/my/error")
    public BaseResponse<List<ChoiceQuestion>> listMyErrorChoiceQuestion(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<ChoiceQuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.eq("status", StatusEnum.FALSE);
        List<ChoiceQuestion> choiceQuestionList = choiceQuestionSubmitService.list(queryWrapper).stream().map(choiceQuestionSubmit -> {
            return choiceQuestionService.getById(choiceQuestionSubmit.getQuestionId());
        }).collect(Collectors.toList());
        return ResultUtils.success(choiceQuestionList);
    }

    /**
     * 分页获取判断题列表（Page）（仅管理员）
     *
     * @param choiceQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ChoiceQuestion>> listChoiceQuestionByPage(@RequestBody ChoiceQuestionQueryRequest choiceQuestionQueryRequest,
                                                   HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        long current = choiceQuestionQueryRequest.getCurrent();
        long size = choiceQuestionQueryRequest.getPageSize();
        Page<ChoiceQuestion> choiceQuestionPage = choiceQuestionService.page(new Page<>(current, size),
                choiceQuestionService.getQueryWrapper(choiceQuestionQueryRequest));
        return ResultUtils.success(choiceQuestionPage);
    }

    /**
     * 分页获取判断题列表（List）（仅管理员）
     *
     * @param choiceQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<ChoiceQuestion>> listChoiceQuestionByList(@RequestBody ChoiceQuestionQueryRequest choiceQuestionQueryRequest,
                                                           HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<ChoiceQuestion> choiceQuestionList = choiceQuestionService.list(choiceQuestionService.getQueryWrapper(choiceQuestionQueryRequest));
        return ResultUtils.success(choiceQuestionList);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ChoiceQuestion> getChoiceQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestion choiceQuestion = choiceQuestionService.getById(id);
        if (choiceQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 不是本人或管理员，不能直接获取所有信息
        if (!choiceQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(choiceQuestion);
    }

    /**
     * 提交题目
     *
     * @param choiceQuestionSubmitAddRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/choiceQuestion_submit/do")
    public BaseResponse<Long> doChoiceQuestionSubmit(@RequestBody ChoiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (choiceQuestionSubmitAddRequest == null || choiceQuestionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        long choiceQuestionSubmitId = choiceQuestionSubmitService.doChoiceQuestionSubmit(choiceQuestionSubmitAddRequest, loginUser);
        return ResultUtils.success(choiceQuestionSubmitId);
    }

    /**
     * 分页获取题目提交列表（管理员可以看到全部，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param choiceQuestionSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/choiceQuestion_submit/list/page")
    public BaseResponse<Page<ChoiceQuestionSubmitVO>> listChoiceQuestionSubmitByPage(@RequestBody ChoiceQuestionSubmitQueryRequest choiceQuestionSubmitQueryRequest,
                                                                               HttpServletRequest request) {
        long current = choiceQuestionSubmitQueryRequest.getCurrent();
        long size = choiceQuestionSubmitQueryRequest.getPageSize();
        final User loginUser = userService.getLoginUser(request);
        Page<ChoiceQuestionSubmit> choiceQuestionSubmitPage = choiceQuestionSubmitService.page(new Page<>(current, size),
                choiceQuestionSubmitService.getQueryWrapper(choiceQuestionSubmitQueryRequest));
        return ResultUtils.success(choiceQuestionSubmitService.getChoiceQuestionSubmitVOPage(choiceQuestionSubmitPage, loginUser));
    }

    /**
     * List取题目提交列表（管理员）
     *
     * @param choiceQuestionSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/choiceQuestion_submit/list")
    public BaseResponse<List<ChoiceQuestionSubmit>> listChoiceQuestionSubmit(@RequestBody ChoiceQuestionSubmitQueryAdminRequest choiceQuestionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<ChoiceQuestionSubmit> choiceQuestionSubmitList = choiceQuestionSubmitService.list(choiceQuestionSubmitService.getQueryWrapper(choiceQuestionSubmitQueryRequest));
        return ResultUtils.success(choiceQuestionSubmitList);
    }


    @PostMapping("/choiceQuestion_submit/delete")
    public BaseResponse<Boolean> deleteChoiceQuestionSubmit(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        ChoiceQuestionSubmit oldChoiceQuestionSubmit = choiceQuestionSubmitService.getById(id);
        ThrowUtils.throwIf(oldChoiceQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChoiceQuestionSubmit.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = choiceQuestionSubmitService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("choiceQuestion_submit/update/backend")
    public BaseResponse<Boolean> updateChoiceQuestionSubmitBackend(@RequestBody ChoiceQuestionSubmitUpdateAdminRequest choiceQuestionSubmitUpdateRequest) {
        if (choiceQuestionSubmitUpdateRequest == null || choiceQuestionSubmitUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChoiceQuestionSubmit choiceQuestionSubmit = new ChoiceQuestionSubmit();
        BeanUtils.copyProperties(choiceQuestionSubmitUpdateRequest, choiceQuestionSubmit);
        //校验
        Integer status = choiceQuestionSubmit.getStatus();
        if (StatusEnum.getEnumByValue(status) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态不存在");
        }
        long id = choiceQuestionSubmitUpdateRequest.getId();
        // 判断是否存在
        ChoiceQuestionSubmit oldChoiceQuestionSubmit = choiceQuestionSubmitService.getById(id);
        ThrowUtils.throwIf(oldChoiceQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = choiceQuestionSubmitService.updateById(choiceQuestionSubmit);
        return ResultUtils.success(result);
    }


}
