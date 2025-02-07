package com.hyl.zhanmaojbackendquestionservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyl.zhanmaojbackendcommon.common.*;
import com.hyl.zhanmaojbackendcommon.constant.UserConstant;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendcommon.exception.ThrowUtils;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.JudgeInfo;
import com.hyl.zhanmaojbackendmodel.model.dto.question.*;
import com.hyl.zhanmaojbackendmodel.model.dto.questionsbumit.*;
import com.hyl.zhanmaojbackendmodel.model.dto.test.TestQuestionUpdateRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.*;
import com.hyl.zhanmaojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.hyl.zhanmaojbackendmodel.model.enums.QuestionTypeEnum;
import com.hyl.zhanmaojbackendmodel.model.vo.*;
import com.hyl.zhanmaojbackendquestionservice.service.QuestionService;
import com.hyl.zhanmaojbackendquestionservice.service.QuestionSubmitService;
import com.hyl.zhanmaojbackendquestionservice.service.TestQuestionService;
import com.hyl.zhanmaojbackendquestionservice.service.TestService;
import com.hyl.zhanmaojbackendserviceclient.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
@RequestMapping("/")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private TestService testService;

    @Resource
    private TestQuestionService testQuestionService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        questionService.validQuestion(question, true);
        User loginUser = userFeignClient.getLoginUser(request);
        question.setUserId(loginUser.getId());
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    @PostMapping("/add/backend")
    public BaseResponse<Long> addQuestionBackend(@RequestBody QuestionAddAdminRequest questionAddRequest, HttpServletRequest request) {
        if (questionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        String tags = questionAddRequest.getTags();
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
        }  else {
            question.setTags(tagsList.toString());
        }
        String judgeCaseStr = questionAddRequest.getJudgeCase();
        List<Object> judgeCaseList = new ArrayList<>();
        if (StringUtils.isNotBlank(judgeCaseStr)) {
            try {
                judgeCaseList = GSON.fromJson(judgeCaseStr, List.class);
                for (Object o : judgeCaseList) {
                    String json = GSON.toJson(o, Object.class);
                    GSON.fromJson(json, JudgeCase.class);
                }
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + judgeCaseStr);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入判题用例格式错误");
            }
        }
        question.setJudgeCase(GSON.toJson(judgeCaseList, List.class));
        String judgeConfigStr = questionAddRequest.getJudgeConfig();
        JudgeConfig judgeConfig = new JudgeConfig();
        if (StringUtils.isNotBlank(judgeConfigStr)) {
            try {
                judgeConfig = GSON.fromJson(judgeConfigStr, JudgeConfig.class);
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + judgeConfig.toString());
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入判题配置格式错误");
            }
        }
        question.setJudgeConfig(GSON.toJson(judgeConfig, JudgeConfig.class));
        User loginUser = userFeignClient.getLoginUser(request);
        question.setUserId(loginUser.getId());
        boolean result = questionService.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newQuestionId = question.getId();
        return ResultUtils.success(newQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestion.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    @PostMapping("/update/backend")
    public BaseResponse<Boolean> updateQuestionBackend(@RequestBody QuestionUpdateAdminRequest questionUpdateRequest) {
        if (questionUpdateRequest == null || questionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        String tags = questionUpdateRequest.getTags();
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
        }
        question.setTags(GSON.toJson(tagsList));
        String judgeCaseStr = questionUpdateRequest.getJudgeCase();
        List<Object> judgeCaseList = new ArrayList<>();
        if (StringUtils.isNotBlank(judgeCaseStr)) {
            try {
                judgeCaseList = GSON.fromJson(judgeCaseStr, List.class);
                for (Object o : judgeCaseList) {
                    String json = GSON.toJson(o, Object.class);
                    GSON.fromJson(json, JudgeCase.class);
                }
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + judgeCaseStr);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入判题用例格式错误");
            }
        }
        question.setJudgeCase(GSON.toJson(judgeCaseList, List.class));
        String judgeConfigStr = questionUpdateRequest.getJudgeConfig();
        JudgeConfig judgeConfig = new JudgeConfig();
        if (StringUtils.isNotBlank(judgeConfigStr)) {
            try {
                judgeConfig = GSON.fromJson(judgeConfigStr, JudgeConfig.class);
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + judgeConfig.toString());
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入判题配置格式错误");
            }
        }
        question.setJudgeConfig(GSON.toJson(judgeConfig, JudgeConfig.class));
        // 参数校验
        questionService.validQuestion(question, false);
        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionVO> getQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(questionService.getQuestionVO(question, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
            HttpServletRequest request) {
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage;
        if (questionQueryRequest.getNum() == null){
            questionPage = questionService.page(new Page<>(current, size),
                    questionService.getQueryWrapper(questionQueryRequest));
        }
        else {
            questionPage = questionService.page(new Page<>(current, questionQueryRequest.getNum()),
                    questionService.getQueryWrapper(questionQueryRequest));
        }
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionVO>> listMyQuestionVOByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
            HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        questionQueryRequest.setUserId(loginUser.getId());
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }


    /**
     * 编辑（用户）
     *
     * @param questionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestion(@RequestBody QuestionEditRequest questionEditRequest, HttpServletRequest request) {
        if (questionEditRequest == null || questionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);
        List<String> tags = questionEditRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionEditRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        questionService.validQuestion(question, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = questionEditRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = questionService.updateById(question);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取问题列表（Page）（仅管理员）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                   HttpServletRequest request) {
        User loginUser = userFeignClient.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionPage);
    }

    /**
     * 分页获取问题列表（List）（仅管理员）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<List<Question>> listQuestionByList(@RequestBody QuestionQueryAdminRequest questionQueryRequest,
                                                           HttpServletRequest request) {
        User loginUser = userFeignClient.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<Question> questionList = questionService.list(questionService.getQueryWrapper(questionQueryRequest));
        if (questionQueryRequest.getNum() != null){
            List<Question> subList = questionList.subList(0, Math.min(6, questionList.size()));
            return ResultUtils.success(subList);
        }
        return ResultUtils.success(questionList);
    }


    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getById(id);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        // 不是本人或管理员，不能直接获取所有信息
        if (!question.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(question);
    }

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/question_submit/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userFeignClient.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 获取提交信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/question_submit/get/vo")
    public BaseResponse<QuestionSubmitVO> getQuestionSubmitVO(long id,
                                                              HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        if (questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        final User loginUser = userFeignClient.getLoginUser(request);
        // 不是本人或管理员，不能直接获取所有信息
        if (!questionSubmit.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QuestionSubmitVO questionSubmitVO = questionSubmitService.getQuestionSubmitVO(questionSubmit, loginUser);
        return ResultUtils.success(questionSubmitVO);
    }

    /**
     * 获取提交状态
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/question_submit/get/status")
    public BaseResponse<QuestionSubmitStatusVO> getQuestionSubmitStatus(long id,
                                                                        HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmitStatusVO questionSubmitStatusVO = new QuestionSubmitStatusVO();
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        if (questionSubmit == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        QuestionSubmitStatusEnum enumByValue = QuestionSubmitStatusEnum.getEnumByValue(questionSubmit.getStatus());

        questionSubmitStatusVO.setStatus("RUNNING");
        if (enumByValue != null){
            if (enumByValue.equals(QuestionSubmitStatusEnum.SUCCEED) || enumByValue.equals(QuestionSubmitStatusEnum.FAILED)){
                questionSubmitStatusVO.setStatus("COMPLETED");
            }
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题状态不存在");
        }

        final User loginUser = userFeignClient.getLoginUser(request);
        // 不是本人或管理员，不能直接获取所有信息
        if (!questionSubmit.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QuestionSubmitVO questionSubmitVO = questionSubmitService.getQuestionSubmitVO(questionSubmit, loginUser);
        if (questionSubmitStatusVO.getStatus().equals("COMPLETED")){
            questionSubmitStatusVO.setResult(questionSubmitVO);
        }
        return ResultUtils.success(questionSubmitStatusVO);
    }

    /**
     * 试卷编程题提交
     * @param questionSubmitAddTestRequest
     * @param request
     * @return
     */
    @PostMapping("/question_submit/test/do")
    public BaseResponse<Long> doQuestionTestSubmit(@RequestBody QuestionSubmitAddTestRequest questionSubmitAddTestRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddTestRequest == null || questionSubmitAddTestRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userFeignClient.getLoginUser(request);
        QuestionSubmitAddRequest questionSubmitAddRequest = new QuestionSubmitAddRequest();
        BeanUtils.copyProperties(questionSubmitAddTestRequest, questionSubmitAddRequest);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        questionSubmit.setTestId(questionSubmitAddTestRequest.getTestId());
        boolean b = questionSubmitService.updateById(questionSubmit);
        if (!b) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "编程题试卷id更新失败");
        }
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表（管理员可以看到全部，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/question_submit/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        final User loginUser = userFeignClient.getLoginUser(request);
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

    /**
     * 分页获取题目提交列表（管理员可以看到全部，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/question_submit/my/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listMyQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        final User loginUser = userFeignClient.getLoginUser(request);
        QueryWrapper<QuestionSubmit> queryWrapper = questionSubmitService.getQueryWrapper(questionSubmitQueryRequest);
        queryWrapper.eq("userId", loginUser.getId());
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
               queryWrapper);
        Page<QuestionSubmitVO> page = new Page<>(questionSubmitPage.getCurrent(),questionSubmitPage.getSize(),questionSubmitPage.getTotal());
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        if (CollectionUtils.isEmpty(questionSubmitList)){
            return ResultUtils.success(page);
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit ->{
            return questionSubmitService.getQuestionSubmitVO(questionSubmit, loginUser);
        }).collect(Collectors.toList());
        page.setRecords(questionSubmitVOList);
        return ResultUtils.success(page);
    }

    /**
     * List取题目提交列表（管理员）
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/question_submit/list")
    public BaseResponse<List<QuestionSubmit>> listQuestionSubmit(@RequestBody QuestionSubmitQueryAdminRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        if (!userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<QuestionSubmit> questionSubmitList = questionSubmitService.list(questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitList);
    }


    @PostMapping("/question_submit/delete")
    public BaseResponse<Boolean> deleteQuestionSubmit(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionSubmit oldQuestionSubmit = questionSubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestionSubmit.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = questionSubmitService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("question_submit/update/backend")
    public BaseResponse<Boolean> updateQuestionSubmitBackend(@RequestBody QuestionSubmitUpdateAdminRequest questionSubmitUpdateRequest) {
        if (questionSubmitUpdateRequest == null || questionSubmitUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitUpdateRequest, questionSubmit);
        String judgeInfoStr = questionSubmitUpdateRequest.getJudgeInfo();
        JudgeInfo judgeInfo = new JudgeInfo();
        if (StringUtils.isNotBlank(judgeInfoStr)) {
            try {
                judgeInfo = GSON.fromJson(judgeInfoStr, JudgeInfo.class);
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + judgeInfoStr);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入判题信息格式错误");
            }
        }
        questionSubmit.setJudgeInfo(GSON.toJson(judgeInfo, JudgeInfo.class));
        //校验
        Integer status = questionSubmit.getStatus();
        if (QuestionSubmitStatusEnum.getEnumByValue(status) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态不存在");
        }
        long id = questionSubmitUpdateRequest.getId();
        // 判断是否存在
        QuestionSubmit oldQuestionSubmit = questionSubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = questionSubmitService.updateById(questionSubmit);
        return ResultUtils.success(result);
    }

    @PostMapping("my/error")
    public BaseResponse<List<QuestionVO>> getMyErrorQuestion(HttpServletRequest request) {
        Long id = userFeignClient.getLoginUser(request).getId();
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        List<QuestionVO> list = questionSubmitService.list(queryWrapper).stream().map(questionSubmit -> {
            return questionService.getQuestionVO(questionService.getById(questionSubmit.getQuestionId()),request);
        }).collect(Collectors.toList());
        Set<QuestionVO> set = new HashSet<>(list);
        list = new ArrayList<>(set);
        return ResultUtils.success(list);
    }

    @GetMapping("/idList")
    public BaseResponse<List<IdTitleVO>> getQuestionIdTitleList(HttpServletRequest request) {
        List<Question> list = questionService.list();
        List<IdTitleVO> idTitleVOList = list.stream().map(question -> {
            IdTitleVO idTitleVO = new IdTitleVO();
            idTitleVO.setId(question.getId());
            idTitleVO.setTitle(question.getTitle());
            return idTitleVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(idTitleVOList);
    }

    /**
     * 分页获取问题列表（List）（仅管理员）
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/question_test/list")
    public BaseResponse<List<QuestionTestAdminVO>> listQuestionTestByList(@RequestBody QuestionQueryTestAdminRequest questionQueryRequest,
                                                                          HttpServletRequest request) {
        User loginUser = userFeignClient.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (UserConstant.DEFAULT_ROLE.equals(userRole) || UserConstant.USER_LOGIN_STATE.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QueryWrapper<TestQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("testId", questionQueryRequest.getTestId());
        queryWrapper.eq("type", QuestionTypeEnum.PROGRAMMING_QUESTION.getValue());
        List<TestQuestion> list = testQuestionService.list(queryWrapper);
        List<QuestionTestAdminVO> questionTestAdminVOList = list.stream().map(testQuestion -> {
            QuestionTestAdminVO questionTestAdminVO = new QuestionTestAdminVO();
            Question question = questionService.getById(testQuestion.getQuestionId());
            questionTestAdminVO.setScore(testQuestion.getScore());
            if (question == null) {
                return null;
            }
            BeanUtils.copyProperties(question, questionTestAdminVO);
            questionTestAdminVO.setId(testQuestion.getId());
            questionTestAdminVO.setQuestionId(question.getId());
            return questionTestAdminVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(questionTestAdminVOList);
    }

    @PostMapping("/test_question/delete")
    public BaseResponse<Boolean> deleteTestQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
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
        if (!userFeignClient.isAdmin(request)) {
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

    @PostMapping("/test_question/update")
    public BaseResponse<Boolean> updateTestQuestion(@RequestBody TestQuestionUpdateRequest testQuestionUpdateRequest, HttpServletRequest request) {
        if (testQuestionUpdateRequest == null || testQuestionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = testQuestionUpdateRequest.getId();
        // 判断是否存在
        TestQuestion oldQuestion = testQuestionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 管理员可删除
        if (!userFeignClient.isAdmin(request)) {
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


    @PostMapping("/test_question/add")
    public BaseResponse<Boolean> addTestQuestion(@RequestBody QueryRequest queryRequest, HttpServletRequest request) {
        QuestionTestAddRequest addRequest = queryRequest.getQuestionTestAddRequest();
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = Long.valueOf(addRequest.getId());
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        Integer score = addRequest.getScore();
        Long testId = Long.valueOf(addRequest.getTestId());
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 管理员判定
        if (!userFeignClient.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setTestId(testId);
        testQuestion.setQuestionId(id);
        testQuestion.setType(QuestionTypeEnum.PROGRAMMING_QUESTION.getValue());
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


}
