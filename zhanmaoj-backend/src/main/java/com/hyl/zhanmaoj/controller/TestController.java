package com.hyl.zhanmaoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyl.zhanmaoj.common.BaseResponse;
import com.hyl.zhanmaoj.common.DeleteRequest;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.common.ResultUtils;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.exception.ThrowUtils;
import com.hyl.zhanmaoj.model.dto.test.*;
import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitAddRequest;
import com.hyl.zhanmaoj.model.entity.*;
import com.hyl.zhanmaoj.model.enums.QuestionTypeEnum;
import com.hyl.zhanmaoj.model.enums.TestStatusEnum;
import com.hyl.zhanmaoj.model.vo.*;
import com.hyl.zhanmaoj.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Resource
    private TestService testService;

    @Resource
    private TrueOrFalseService trueOrFalseService;

    @Resource
    private ChoiceQuestionService choiceQuestionService;

    @Resource
    private TestQuestionService testQuestionService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    private TestUserService testUserService;


    private final static Gson GSON = new Gson();

    @PostMapping("/add")
    public BaseResponse<Long> addTest(@RequestBody TestAddRequest testAddRequest,
                                         HttpServletRequest request) {
        if (testAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer status = testAddRequest.getStatus();
        if (TestStatusEnum.getEnumByValue(status) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "试卷状态不符合规则");
        }
        String password = testAddRequest.getPassword();
        if (TestStatusEnum.getEnumByValue(status) == TestStatusEnum.ENCRYPTION && (StringUtils.isBlank(password))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "试卷密码不能为空");
        }
        Date beginTime = testAddRequest.getBeginTime();
        Date expiredTime = testAddRequest.getExpiredTime();
        if (beginTime == null || expiredTime == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "试卷时间不能为空");
        }
        if (beginTime.after(expiredTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "试卷时间不符合规则");
        }
        Integer trueOrFalseNum = testAddRequest.getTrueOrFalseNum();
        if (trueOrFalseNum == null) {
            trueOrFalseNum = 0;
        }
        Integer trueOrFalsePerScore = testAddRequest.getTrueOrFalsePerScore();
        if (trueOrFalsePerScore == null) {
            trueOrFalsePerScore = 0;
        }
        Integer choiceQuestionNum = testAddRequest.getChoiceQuestionNum();
        if (choiceQuestionNum == null) {
            choiceQuestionNum = 0;
        }
        Integer choiceQuestionPerScore = testAddRequest.getChoiceQuestionPerScore();
        if (choiceQuestionPerScore == null) {
            choiceQuestionPerScore = 0;
        }
        List<Object> questionListObject = new ArrayList<>();
        String questionListStr = testAddRequest.getQuestionList();
        List<QuestionTestAddVO> questionList = new ArrayList<>();
        if (StringUtils.isNotBlank(questionListStr)) {
            try {
                questionListObject = GSON.fromJson(questionListStr, List.class);
                for (Object o : questionListObject) {
                    String json = GSON.toJson(o, Object.class);
                    QuestionTestAddVO questionTestAddVO = GSON.fromJson(json, QuestionTestAddVO.class);
                    questionList.add(questionTestAddVO);
                }
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + questionListStr);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入编程题列表格式错误");
            }
        }
        int sumScoreNow = 0;
        for (QuestionTestAddVO questionTestAddVO : questionList) {
            sumScoreNow += questionTestAddVO.getScore();
        }
        Test test = new Test();
        BeanUtils.copyProperties(testAddRequest, test);
        test.setTotalScore(sumScoreNow + trueOrFalseNum * trueOrFalsePerScore + choiceQuestionNum * choiceQuestionPerScore);
        test.setQuestionNum(questionList.size() + trueOrFalseNum + choiceQuestionNum);
        Long id = userService.getLoginUser(request).getId();
        test.setUserId(id);
        boolean save = testService.save(test);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "试卷添加失败");
        }
        //保存各题与试卷关系
        Long testId = test.getId();
        List<TrueOrFalse> randomTrueOrFalseList = trueOrFalseService.getRandomTrueOrFalseList(trueOrFalseNum);
        List<TestQuestion> testQuestionList = new ArrayList<>();
        for (TrueOrFalse trueOrFalse : randomTrueOrFalseList) {
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setTestId(testId);
            testQuestion.setQuestionId(trueOrFalse.getId());
            testQuestion.setScore(trueOrFalsePerScore);
            testQuestion.setType(QuestionTypeEnum.TRUE_OR_FALSE.getValue());
            testQuestionList.add(testQuestion);
        }
        List<ChoiceQuestion> randomChoiceQuestionList = choiceQuestionService.getRandomChoiceQuestionList(choiceQuestionNum);
        for (ChoiceQuestion choiceQuestion : randomChoiceQuestionList) {
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setTestId(testId);
            testQuestion.setQuestionId(choiceQuestion.getId());
            testQuestion.setScore(choiceQuestionPerScore);
            testQuestion.setType(QuestionTypeEnum.CHOICE_QUESTION.getValue());
            testQuestionList.add(testQuestion);
        }
        for (QuestionTestAddVO questionTestAddVO : questionList) {
            TestQuestion testQuestionProgram = new TestQuestion();
            testQuestionProgram.setTestId(testId);
            BeanUtils.copyProperties(questionTestAddVO, testQuestionProgram);
            testQuestionProgram.setType(QuestionTypeEnum.PROGRAMMING_QUESTION.getValue());
            testQuestionList.add(testQuestionProgram);
        }
        for (TestQuestion testQuestionFinal : testQuestionList) {
            testQuestionFinal.setId(null);
            boolean saved = testQuestionService.save(testQuestionFinal);
            if (!saved) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "试卷关系添加失败");
            }
        }
        return ResultUtils.success(testId);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> addTest(@RequestBody DeleteRequest deleteRequest,
                                      HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long testId = deleteRequest.getId();
        // 判断是否存在
        Test oldTest = testService.getById(testId);
        ThrowUtils.throwIf(oldTest == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = testService.removeById(testId);
        boolean removedByTestId = testQuestionService.removeByTestId(testId);
        if (!b || !removedByTestId) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateTest(@RequestBody TestUpdateRequest testUpdateRequest,
                                            HttpServletRequest request) {
        if (testUpdateRequest == null || testUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long testId = testUpdateRequest.getId();
        Test testOld = testService.getById(testId);
        ThrowUtils.throwIf(testOld == null, ErrorCode.NOT_FOUND_ERROR);
        //todo 校验数据
        Test test = new Test();
        BeanUtils.copyProperties(testUpdateRequest, test);
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 参数校验
        boolean result = testService.updateById(test);
        return ResultUtils.success(result);
    }

    @PostMapping("/list")
    public BaseResponse<List<Test>> listTest(@RequestBody TestQueryRequest testQueryRequest,
                                             HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<Test> testList = testService.list(testService.getQueryWrapper(testQueryRequest));
        return ResultUtils.success(testList);
    }

    @PostMapping("/list/simple")
    public BaseResponse<List<Test>> listTestSimple(
                                             HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<Test> testList = testService.list(new QueryWrapper<>());
        return ResultUtils.success(testList);
    }


    /**
     * 我做过的试卷
     * @param request
     * @return
     */
    @PostMapping("/list/mine")
    public BaseResponse<List<Test>> listTestMine(HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        Long id = userService.getLoginUser(request).getId();
        QueryWrapper<TestUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        List<Test> testList = testUserService.list(queryWrapper).stream().map(testUser -> testService.getById(testUser.getTestId())).collect(Collectors.toList());
        return ResultUtils.success(testList);
    }


    @PostMapping("/test_submit/do")
    public BaseResponse<Long> doTrueOrFalseSubmit(@RequestBody TestSubmitAddRequest testSubmitAddRequest,
                                                  HttpServletRequest request) {
        if (testSubmitAddRequest == null || testSubmitAddRequest.getTestId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userService.getLoginUser(request);
//        long testSubmitId = testSubmitService.doTrueOrFalseSubmit(testSubmitAddRequest, loginUser);
        return ResultUtils.success(null);
    }

    @PostMapping("/join")
    public BaseResponse<Boolean> JoinTest(@RequestBody TestJoinRequest testJoinRequest,
                                                  HttpServletRequest request) {
        if (testJoinRequest == null || testJoinRequest.getTestId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = testService.joinTest(testJoinRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        TestUser testUser = new TestUser();
        testUser.setUserId(userId);
        testUser.setTestId(testJoinRequest.getTestId());
        boolean save = testUserService.save(testUser);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"用户试卷关系创建失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get/detail")
    public BaseResponse<TestVO> GetTestDetail(long testId,
                                              HttpServletRequest request) {
        if (testId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TestVO testVO = new TestVO();
        Test test = testService.getById(testId);
        //
        BeanUtils.copyProperties(test, testVO);
        List<ChoiceQuestionTestDetailVO> choiceQuestionTestDetailVOList = choiceQuestionService.getChoiceQuestonTestDetailList(testId);
        List<TrueOrFalseTestDetailVO> trueOrFalseTestDetailVOList = trueOrFalseService.getTrueOrFalseTestDetailList(testId);
        List<QuestionTestDetailVO> questionTestDetailVOList = questionService.getQuestionTestDetailList(testId, request);
        testVO.setQuestionTestDetailVOS(questionTestDetailVOList);
        testVO.setTrueOrFalseTestDetailVOS(trueOrFalseTestDetailVOList);
        testVO.setChoiceQuestionTestDetailVOS(choiceQuestionTestDetailVOList);
        return ResultUtils.success(testVO);
    }

    @GetMapping("/test/title")
    public BaseResponse<TestTitleVO> GetTestTitle(long testId,
                                              HttpServletRequest request) {
        if (testId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TestTitleVO testTitleVO = new TestTitleVO();
        Test test = testService.getById(testId);
        BeanUtils.copyProperties(test, testTitleVO);
        List<ChoiceQuestionTitleVO> choiceQuestionTitleVOList = choiceQuestionService.getChoiceQuestonTitleList(testId);
        List<TrueOrFalseTitleVO> trueOrFalseTitleVOList = trueOrFalseService.getTrueOrFalseTitleList(testId);
        List<QuestionTitleVO> questionTitleVOList = questionService.getQuestionTitleList(testId);
        testTitleVO.setTrueOrFalseTitleVOS(trueOrFalseTitleVOList);
        testTitleVO.setChoiceQuestionTitleVOS(choiceQuestionTitleVOList);
        testTitleVO.setQuestionTitleVOS(questionTitleVOList);
        return ResultUtils.success(testTitleVO);
    }
}
