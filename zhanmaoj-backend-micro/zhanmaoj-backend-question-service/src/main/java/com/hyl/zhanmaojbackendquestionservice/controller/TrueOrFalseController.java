package com.hyl.zhanmaojbackendquestionservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyl.zhanmaojbackendcommon.common.*;
import com.hyl.zhanmaojbackendcommon.constant.UserConstant;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendcommon.exception.ThrowUtils;
import com.hyl.zhanmaojbackendmodel.model.dto.test.TestQuestionUpdateRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse.*;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitAddRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitQueryAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitQueryRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalsesubmit.TrueOrFalseSubmitUpdateAdminRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.*;
import com.hyl.zhanmaojbackendmodel.model.enums.QuestionTypeEnum;
import com.hyl.zhanmaojbackendmodel.model.enums.StatusEnum;
import com.hyl.zhanmaojbackendmodel.model.vo.QueryRequest;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseSubmitVO;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseTestAdminVO;
import com.hyl.zhanmaojbackendmodel.model.vo.TrueOrFalseVO;
import com.hyl.zhanmaojbackendquestionservice.service.TestQuestionService;
import com.hyl.zhanmaojbackendquestionservice.service.TestService;
import com.hyl.zhanmaojbackendquestionservice.service.TrueOrFalseService;
import com.hyl.zhanmaojbackendquestionservice.service.TrueOrFalseSubmitService;
import com.hyl.zhanmaojbackenduserservice.service.UserService;
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
@RequestMapping("/trueOrFalse")
@Slf4j
public class TrueOrFalseController {

    @Resource
    private TrueOrFalseService trueOrFalseService;

    @Resource
    private TrueOrFalseSubmitService trueOrFalseSubmitService;

    @Resource
    private UserService userService;

    @Resource
    private TestQuestionService testQuestionService;

    @Resource
    private TestService testService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param trueOrFalseAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addTrueOrFalse(@RequestBody TrueOrFalseAddRequest trueOrFalseAddRequest, HttpServletRequest request) {
        if (trueOrFalseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = new TrueOrFalse();
        BeanUtils.copyProperties(trueOrFalseAddRequest, trueOrFalse);
        List<String> tags = trueOrFalseAddRequest.getTags();
        if (tags != null) {
            trueOrFalse.setTags(GSON.toJson(tags));
        }
        User loginUser = userService.getLoginUser(request);
        trueOrFalse.setUserId(loginUser.getId());
        trueOrFalse.setSubmitNum(0);
        trueOrFalse.setAcceptedNum(0);
        boolean result = trueOrFalseService.save(trueOrFalse);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTrueOrFalseId = trueOrFalse.getId();
        return ResultUtils.success(newTrueOrFalseId);
    }

    @PostMapping("/add/backend")
    public BaseResponse<Long> addTrueOrFalseBackend(@RequestBody TrueOrFalseAddAdminRequest trueOrFalseAddRequest, HttpServletRequest request) {
        if (trueOrFalseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = new TrueOrFalse();
        BeanUtils.copyProperties(trueOrFalseAddRequest, trueOrFalse);
        String tags = trueOrFalseAddRequest.getTags();
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
        }else {
            trueOrFalse.setTags(tagsList.toString());
        }
        User loginUser = userService.getLoginUser(request);
        trueOrFalse.setUserId(loginUser.getId());
        boolean result = trueOrFalseService.save(trueOrFalse);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newTrueOrFalseId = trueOrFalse.getId();
        return ResultUtils.success(newTrueOrFalseId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTrueOrFalse(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TrueOrFalse oldTrueOrFalse = trueOrFalseService.getById(id);
        ThrowUtils.throwIf(oldTrueOrFalse == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTrueOrFalse.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = trueOrFalseService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param trueOrFalseUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateTrueOrFalse(@RequestBody TrueOrFalseUpdateRequest trueOrFalseUpdateRequest) {
        if (trueOrFalseUpdateRequest == null || trueOrFalseUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = new TrueOrFalse();
        BeanUtils.copyProperties(trueOrFalseUpdateRequest, trueOrFalse);
        List<String> tags = trueOrFalseUpdateRequest.getTags();
        if (tags != null) {
            trueOrFalse.setTags(GSON.toJson(tags));
        }
        // 参数校验
        trueOrFalseService.validTrueOrFalse(trueOrFalse, false);
        long id = trueOrFalseUpdateRequest.getId();
        // 判断是否存在
        TrueOrFalse oldTrueOrFalse = trueOrFalseService.getById(id);
        ThrowUtils.throwIf(oldTrueOrFalse == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = trueOrFalseService.updateById(trueOrFalse);
        return ResultUtils.success(result);
    }

    @PostMapping("/update/backend")
    public BaseResponse<Boolean> updateTrueOrFalseBackend(@RequestBody TrueOrFalseUpdateAdminRequest trueOrFalseUpdateRequest) {
        if (trueOrFalseUpdateRequest == null || trueOrFalseUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = new TrueOrFalse();
        BeanUtils.copyProperties(trueOrFalseUpdateRequest, trueOrFalse);
        String tags = trueOrFalseUpdateRequest.getTags();
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
        trueOrFalse.setTags(GSON.toJson(tagsList));
        // 参数校验
        trueOrFalseService.validTrueOrFalse(trueOrFalse, false);
        long id = trueOrFalseUpdateRequest.getId();
        // 判断是否存在
        TrueOrFalse oldTrueOrFalse = trueOrFalseService.getById(id);
        ThrowUtils.throwIf(oldTrueOrFalse == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = trueOrFalseService.updateById(trueOrFalse);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<TrueOrFalseVO> getTrueOrFalseVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = trueOrFalseService.getById(id);
        if (trueOrFalse == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(trueOrFalseService.getTrueOrFalseVO(trueOrFalse, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param trueOrFalseQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TrueOrFalseVO>> listTrueOrFalseVOByPage(@RequestBody TrueOrFalseQueryRequest trueOrFalseQueryRequest,
            HttpServletRequest request) {
        long current = trueOrFalseQueryRequest.getCurrent();
        long size = trueOrFalseQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<TrueOrFalse> trueOrFalsePage = trueOrFalseService.page(new Page<>(current, size),
                trueOrFalseService.getQueryWrapper(trueOrFalseQueryRequest));
        return ResultUtils.success(trueOrFalseService.getTrueOrFalseVOPage(trueOrFalsePage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param trueOrFalseQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<TrueOrFalseVO>> listMyTrueOrFalseVOByPage(@RequestBody TrueOrFalseQueryRequest trueOrFalseQueryRequest,
            HttpServletRequest request) {
        if (trueOrFalseQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        trueOrFalseQueryRequest.setUserId(loginUser.getId());
        long current = trueOrFalseQueryRequest.getCurrent();
        long size = trueOrFalseQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<TrueOrFalse> trueOrFalsePage = trueOrFalseService.page(new Page<>(current, size),
                trueOrFalseService.getQueryWrapper(trueOrFalseQueryRequest));
        return ResultUtils.success(trueOrFalseService.getTrueOrFalseVOPage(trueOrFalsePage, request));
    }


    /**
     * 编辑（用户）
     *
     * @param trueOrFalseEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editTrueOrFalse(@RequestBody TrueOrFalseEditRequest trueOrFalseEditRequest, HttpServletRequest request) {
        if (trueOrFalseEditRequest == null || trueOrFalseEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = new TrueOrFalse();
        BeanUtils.copyProperties(trueOrFalseEditRequest, trueOrFalse);
        List<String> tags = trueOrFalseEditRequest.getTags();
        if (tags != null) {
            trueOrFalse.setTags(GSON.toJson(tags));
        }
        // 参数校验
        trueOrFalseService.validTrueOrFalse(trueOrFalse, false);
        User loginUser = userService.getLoginUser(request);
        long id = trueOrFalseEditRequest.getId();
        // 判断是否存在
        TrueOrFalse oldTrueOrFalse = trueOrFalseService.getById(id);
        ThrowUtils.throwIf(oldTrueOrFalse == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldTrueOrFalse.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = trueOrFalseService.updateById(trueOrFalse);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取判断题列表（Page）（仅管理员）
     *
     * @param trueOrFalseQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<TrueOrFalse>> listTrueOrFalseByPage(@RequestBody TrueOrFalseQueryRequest trueOrFalseQueryRequest,
                                                   HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        long current = trueOrFalseQueryRequest.getCurrent();
        long size = trueOrFalseQueryRequest.getPageSize();
        Page<TrueOrFalse> trueOrFalsePage = trueOrFalseService.page(new Page<>(current, size),
                trueOrFalseService.getQueryWrapper(trueOrFalseQueryRequest));
        return ResultUtils.success(trueOrFalsePage);
    }

    /**
     * 分页获取判断题列表（List）（仅管理员）
     *
     * @param trueOrFalseQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<TrueOrFalse>> listTrueOrFalseByList(@RequestBody TrueOrFalseQueryAdminRequest trueOrFalseQueryRequest,
                                                           HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<TrueOrFalse> trueOrFalseList = trueOrFalseService.list(trueOrFalseService.getQueryWrapper(trueOrFalseQueryRequest));
        return ResultUtils.success(trueOrFalseList);
    }

    @PostMapping("/list/my/error")
    public BaseResponse<List<TrueOrFalse>> listMyErrorTrueOrFalse(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QueryWrapper<TrueOrFalseSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", loginUser.getId());
        queryWrapper.eq("status", StatusEnum.FALSE.getValue());
        List<TrueOrFalse> trueOrFalseList = trueOrFalseSubmitService.list(queryWrapper).stream().map(trueOrFalseSubmit -> {
            return trueOrFalseService.getById(trueOrFalseSubmit.getQuestionId());
        }).collect(Collectors.toList());
        return ResultUtils.success(trueOrFalseList);
    }




    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<TrueOrFalse> getTrueOrFalseById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = trueOrFalseService.getById(id);
        if (trueOrFalse == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 不是本人或管理员，不能直接获取所有信息
        if (!trueOrFalse.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(trueOrFalse);
    }

    /**
     * 提交题目
     *
     * @param trueOrFalseSubmitAddRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/trueOrFalse_submit/do")
    public BaseResponse<Long> doTrueOrFalseSubmit(@RequestBody TrueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest,
                                               HttpServletRequest request) {
        if (trueOrFalseSubmitAddRequest == null || trueOrFalseSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
        long trueOrFalseSubmitId = trueOrFalseSubmitService.doTrueOrFalseSubmit(trueOrFalseSubmitAddRequest, loginUser);
        return ResultUtils.success(trueOrFalseSubmitId);
    }

    /**
     * 分页获取题目提交列表（管理员可以看到全部，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param trueOrFalseSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/trueOrFalse_submit/list/page")
    public BaseResponse<Page<TrueOrFalseSubmitVO>> listTrueOrFalseSubmitByPage(@RequestBody TrueOrFalseSubmitQueryRequest trueOrFalseSubmitQueryRequest,
                                                                               HttpServletRequest request) {
        long current = trueOrFalseSubmitQueryRequest.getCurrent();
        long size = trueOrFalseSubmitQueryRequest.getPageSize();
        final User loginUser = userService.getLoginUser(request);
        Page<TrueOrFalseSubmit> trueOrFalseSubmitPage = trueOrFalseSubmitService.page(new Page<>(current, size),
                trueOrFalseSubmitService.getQueryWrapper(trueOrFalseSubmitQueryRequest));
        return ResultUtils.success(trueOrFalseSubmitService.getTrueOrFalseSubmitVOPage(trueOrFalseSubmitPage, loginUser));
    }

    /**
     * List取题目提交列表（管理员）
     *
     * @param trueOrFalseSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/trueOrFalse_submit/list")
    public BaseResponse<List<TrueOrFalseSubmit>> listTrueOrFalseSubmit(@RequestBody TrueOrFalseSubmitQueryAdminRequest trueOrFalseSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<TrueOrFalseSubmit> trueOrFalseSubmitList = trueOrFalseSubmitService.list(trueOrFalseSubmitService.getQueryWrapper(trueOrFalseSubmitQueryRequest));
        return ResultUtils.success(trueOrFalseSubmitList);
    }


    @PostMapping("/trueOrFalse_submit/delete")
    public BaseResponse<Boolean> deleteTrueOrFalseSubmit(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TrueOrFalseSubmit oldTrueOrFalseSubmit = trueOrFalseSubmitService.getById(id);
        ThrowUtils.throwIf(oldTrueOrFalseSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTrueOrFalseSubmit.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = trueOrFalseSubmitService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("trueOrFalse_submit/update/backend")
    public BaseResponse<Boolean> updateTrueOrFalseSubmitBackend(@RequestBody TrueOrFalseSubmitUpdateAdminRequest trueOrFalseSubmitUpdateRequest) {
        if (trueOrFalseSubmitUpdateRequest == null || trueOrFalseSubmitUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalseSubmit trueOrFalseSubmit = new TrueOrFalseSubmit();
        BeanUtils.copyProperties(trueOrFalseSubmitUpdateRequest, trueOrFalseSubmit);
        //校验
        Integer status = trueOrFalseSubmit.getStatus();
        if (StatusEnum.getEnumByValue(status) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态不存在");
        }
        long id = trueOrFalseSubmitUpdateRequest.getId();
        // 判断是否存在
        TrueOrFalseSubmit oldTrueOrFalseSubmit = trueOrFalseSubmitService.getById(id);
        ThrowUtils.throwIf(oldTrueOrFalseSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = trueOrFalseSubmitService.updateById(trueOrFalseSubmit);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/score/vo")
    public BaseResponse<TrueOrFalseVO> getTrueOrFalseScoreVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TrueOrFalse trueOrFalse = trueOrFalseService.getById(id);
        if (trueOrFalse == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(trueOrFalseService.getTrueOrFalseVO(trueOrFalse, request));
    }

    @GetMapping("/test/idList")
    public BaseResponse<List<IdTitleVO>> getTestQuestion(Long testId, HttpServletRequest request) {
        List<TrueOrFalse> list = trueOrFalseService.list();
        List<IdTitleVO> idTitleVOList = list.stream().map(question -> {
            IdTitleVO idTitleVO = new IdTitleVO();
            idTitleVO.setId(question.getId());
            idTitleVO.setTitle(question.getTitle());
            return idTitleVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(idTitleVOList);
    }

    @PostMapping("/test/list")
    public BaseResponse<List<TrueOrFalseTestAdminVO>> listTrueOrFalseTestByList(@RequestBody TrueOrFalseQueryTestAdminRequest trueOrFalseQueryRequest,
                                                                                HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("testId", trueOrFalseQueryRequest.getTestId());
        queryWrapper.eq("type", QuestionTypeEnum.TRUE_OR_FALSE.getValue());
        List<TestQuestion> list = testQuestionService.list(queryWrapper);
        List<TrueOrFalseTestAdminVO> trueOrFalseTestAdminVOList = list.stream().map(testQuestion -> {
            TrueOrFalseTestAdminVO trueOrFalseTestAdminVO = new TrueOrFalseTestAdminVO();
            TrueOrFalse trueOrFalse = trueOrFalseService.getById(testQuestion.getQuestionId());
            trueOrFalseTestAdminVO.setScore(testQuestion.getScore());
            if (trueOrFalse == null) {
                return null;
            }
            BeanUtils.copyProperties(trueOrFalse, trueOrFalseTestAdminVO);
            trueOrFalseTestAdminVO.setId(testQuestion.getId());
            trueOrFalseTestAdminVO.setQuestionId(trueOrFalse.getId());
            return trueOrFalseTestAdminVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(trueOrFalseTestAdminVOList);
    }

    @PostMapping("/test/delete")
    public BaseResponse<Boolean> deleteTestTrueOrFalse(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        TestQuestion oldQuestion = testQuestionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        Integer score = oldQuestion.getScore();
        Long testId = oldQuestion.getTestId();
        // 管理员可删除
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = testQuestionService.removeById(id);
        if (!b) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"删除失败");
        } else {
            Test test = testService.getById(testId);
            test.setTotalScore(test.getTotalScore() - score);
            test.setQuestionNum(test.getQuestionNum() - 1);
            boolean update = testService.updateById(test);
            if (!update) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷信息修改失败");
            }
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/test/update")
    public BaseResponse<Boolean> updateTestTrueOrFalse(@RequestBody TestQuestionUpdateRequest testQuestionUpdateRequest, HttpServletRequest request) {
        if (testQuestionUpdateRequest == null || testQuestionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = testQuestionUpdateRequest.getId();
        // 判断是否存在
        TestQuestion oldQuestion = testQuestionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 管理员可删除
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        oldQuestion.setScore(testQuestionUpdateRequest.getScore());
        boolean b = testQuestionService.updateById(oldQuestion);
        if (!b) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"修改失败");
        } else {
            Test test = testService.getById(oldQuestion.getTestId());
            test.setTotalScore(test.getTotalScore() - oldQuestion.getScore() + testQuestionUpdateRequest.getScore());
            boolean update = testService.updateById(test);
            if (!update) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷信息修改失败");
            }
        }
        return ResultUtils.success(true);
    }


    @PostMapping("/test/add")
    public BaseResponse<Boolean> addTestTrueOrFalse(@RequestBody QueryRequest queryRequest, HttpServletRequest request) {
        TrueOrFalseTestAddRequest addRequest = queryRequest.getTrueOrFalseTestAddRequest();
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = Long.valueOf(addRequest.getId());
        // 判断是否存在
        TrueOrFalse oldTrueOrFalse = trueOrFalseService.getById(id);
        Integer score = addRequest.getScore();
        Long testId = Long.valueOf(addRequest.getTestId());
        ThrowUtils.throwIf(oldTrueOrFalse == null, ErrorCode.NOT_FOUND_ERROR);
        // 管理员判定
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setTestId(testId);
        testQuestion.setQuestionId(id);
        testQuestion.setType(QuestionTypeEnum.TRUE_OR_FALSE.getValue());
        testQuestion.setScore(score);
        boolean save = testQuestionService.save(testQuestion);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"添加失败");
        } else {
            Test test = testService.getById(testId);
            test.setTotalScore(test.getTotalScore() + score);
            test.setQuestionNum(test.getQuestionNum() + 1);
            boolean update = testService.updateById(test);
            if (!update) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷信息修改失败");
            }
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/idList")
    public BaseResponse<List<IdTitleVO>> getTestQuestion(HttpServletRequest request) {
        List<TrueOrFalse> list = trueOrFalseService.list();
        List<IdTitleVO> idTitleVOList = list.stream().map(question -> {
            IdTitleVO idTitleVO = new IdTitleVO();
            idTitleVO.setId(question.getId());
            idTitleVO.setTitle(question.getTitle());
            return idTitleVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(idTitleVOList);
    }


}
