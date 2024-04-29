package com.hyl.zhanmaoj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hyl.zhanmaoj.common.*;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.exception.ThrowUtils;
import com.hyl.zhanmaoj.judge.codesandbox.model.JudgeInfo;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.test.*;
import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitQueryRequest;
import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitUpdateRequest;
import com.hyl.zhanmaoj.model.dto.trueorfalsesubmit.TrueOrFalseSubmitAddRequest;
import com.hyl.zhanmaoj.model.entity.*;
import com.hyl.zhanmaoj.model.enums.*;
import com.hyl.zhanmaoj.model.vo.*;
import com.hyl.zhanmaoj.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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

    @Resource
    private TrueOrFalseSubmitService trueOrFalseSubmitService;

    @Resource
    private ChoiceQuestionSubmitService choiceQuestionSubmitService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private TestSubmitService testSubmitService;


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
//        List<Object> questionListObject = new ArrayList<>();
        List<QuestionTestAddBackendVO> questionList = testAddRequest.getQuestionList();
//        Type listType = new TypeToken<List<QuestionTestAddVO>>(){}.getType();
//        if (StringUtils.isNotBlank(questionListStr)) {
//            try {
//                questionListObject = GSON.fromJson(questionListStr, listType);
//                for (Object o : questionListObject) {
//                    if (o != null) {
//                        String json = GSON.toJson(o, Object.class);
//                        QuestionTestAddVO questionTestAddVO = GSON.fromJson(json, QuestionTestAddVO.class);
//                        questionList.add(questionTestAddVO);
//                    }
//                }
//                // 如果转换成功，这里会执行
//                System.out.println("Conversion successful: " + questionListStr);
//            } catch (JsonSyntaxException e) {
//                // 如果转换失败，这里会捕获异常并执行
//                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入编程题列表格式错误");
//            }
//        }
        int sumScoreNow = 0;
        for (QuestionTestAddBackendVO questionTestAddBackendVO : questionList) {
            sumScoreNow += Integer.parseInt(questionTestAddBackendVO.getScore());
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
        for (QuestionTestAddBackendVO questionTestAddVO : questionList) {
            TestQuestion testQuestionProgram = new TestQuestion();
            testQuestionProgram.setTestId(testId);
            testQuestionProgram.setQuestionId(questionTestAddVO.getId());
            BeanUtils.copyProperties(questionTestAddVO, testQuestionProgram);
            testQuestionProgram.setScore(Integer.parseInt(questionTestAddVO.getScore()));
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
//        if (!userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        List<Test> testList;
        testList = testService.list(testService.getQueryWrapper(testQueryRequest));
        List<Test> newTest = new ArrayList<>();
        if (testQueryRequest.getNum() != null){
            newTest = testList.stream().limit(6).collect(Collectors.toList());
            return ResultUtils.success(newTest);
        }
        return ResultUtils.success(testList);
    }

    @PostMapping("/list/simple")
    public BaseResponse<List<Test>> listTestSimple(
                                             HttpServletRequest request) {
//        if (!userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        List<Test> testList = testService.list(new QueryWrapper<>());
        return ResultUtils.success(testList);
    }


    /**
     * 我做过的部分试卷
     * @param request
     * @return
     */
    @PostMapping("/list/mine")
    public BaseResponse<List<MyTestVO>> listTestVOMine(HttpServletRequest request) {
//        if (userService.getLoginUser(request) != null) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        Long id = userService.getLoginUser(request).getId();
        QueryWrapper<TestUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id);
        List<TestUser> testUserList = testUserService.list(queryWrapper);
        List<Test> testList = testUserList.stream().map(testUser -> testService.getById(testUser.getTestId())).collect(Collectors.toList());
        List<MyTestVO> myTestVOList = testList.stream().map(test -> {
            MyTestVO myTestVO = new MyTestVO();
            BeanUtils.copyProperties(test, myTestVO);
            myTestVO.setScore(testUserList.stream().filter(testUser -> testUser.getTestId().equals(test.getId())).findFirst().get().getScore());
            return myTestVO;
        }).collect(Collectors.toList());
        int maxElements = Math.min(myTestVOList.size(), 4);
        myTestVOList = myTestVOList.subList(0, maxElements);
        return ResultUtils.success(myTestVOList);
    }

    /**
     * 我做过的分页详细信息试卷
     * @param request
     * @return
     */
    @PostMapping("/list/mine/page/vo")
    public BaseResponse<Page<MyTestVO>> listTestVOMinePage(@RequestBody MyTestQueryRequest myTestQueryRequest, HttpServletRequest request) {
        long current = myTestQueryRequest.getCurrent();
        long size = myTestQueryRequest.getPageSize();
        Long num = 0L;
        if (myTestQueryRequest.getNum() != null){
            num = myTestQueryRequest.getNum();
        }
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Long userId = userService.getLoginUser(request).getId();
        String title = myTestQueryRequest.getTitle();
        Page<TestUser> testUserPage = new Page<>();
        QueryWrapper<TestUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        if (num != 0){
            testUserPage = testUserService.page(new Page<>(current, num),
                    queryWrapper);
        } else {
            testUserPage = testUserService.page(new Page<>(current, size),
                    queryWrapper);
        }
        Page<MyTestVO> myTestVOPage = testService.getMyTestVOPage(testUserPage, request,myTestQueryRequest);
        return ResultUtils.success(myTestVOPage);
    }


    @PostMapping("/join")
    public BaseResponse<Boolean> JoinTest(@RequestBody TestJoinRequest testJoinRequest,
                                                  HttpServletRequest request) {
        if (testJoinRequest == null || testJoinRequest.getTestId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long testId = testJoinRequest.getTestId();
        boolean result = testService.joinTest(testJoinRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"密码错误");
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        TestUser testUser = new TestUser();
        testUser.setUserId(userId);
        testUser.setTestId(testId);
        QueryWrapper<TestUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("testId", testId);
        TestUser one = testUserService.getOne(queryWrapper);
        QueryWrapper<TestSubmit> testSubmitQueryWrapper = new QueryWrapper<>();
        testSubmitQueryWrapper.eq("userId", userId);
        testSubmitQueryWrapper.eq("testId", testId);
        TestSubmit testSubmit = testSubmitService.getOne(testSubmitQueryWrapper);
        if (testSubmit != null && Objects.equals(testSubmit.getStatus(), TestSubmitStatusEnum.SUCCEED.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不可再次参加考试");
        } else if(testSubmit == null){
            TestSubmit testSubmitAdd = new TestSubmit();
            testSubmitAdd.setTestId(testId);
            testSubmitAdd.setUserId(userId);
            testSubmitAdd.setStatus(TestSubmitStatusEnum.PENDING_QUESTION.getValue());
            testSubmitAdd.setBeginTime(new Date());
            boolean save = testSubmitService.save(testSubmitAdd);
            if (!save) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"用户试卷提交记录初始化");
            }
        }
        if (one == null) {
            boolean save = testUserService.save(testUser);
            if (!save) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"用户试卷关系创建失败");
            }
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get/detail")
    public BaseResponse<TestVO> GetTestDetail(String testId,
                                              HttpServletRequest request) {
        Long id = Long.valueOf(testId);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TestVO testVO = new TestVO();
        Test test = testService.getById(id);
        //
        BeanUtils.copyProperties(test, testVO);
        List<ChoiceQuestionTestDetailVO> choiceQuestionTestDetailVOList = choiceQuestionService.getChoiceQuestonTestDetailList(id);
        List<TrueOrFalseTestDetailVO> trueOrFalseTestDetailVOList = trueOrFalseService.getTrueOrFalseTestDetailList(id);
        List<QuestionTestDetailVO> questionTestDetailVOList = questionService.getQuestionTestDetailList(id, request);
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

//    @PostMapping("/test_submit/do")
//    public BaseResponse<Long> doTestSubmit(@RequestBody TestSubmitAddRequest testSubmitAddRequest,
//                                          HttpServletRequest request) {
//        if (testSubmitAddRequest == null || testSubmitAddRequest.getTestId() <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        List<TestSingleAnswer> trueOrFalseAnswerList = testSubmitAddRequest.getTrueOrFalseAnswerList();
//        List<TestSingleAnswer> choiceAnswerList = testSubmitAddRequest.getChoiceAnswerList();
//        Long testId = testSubmitAddRequest.getTestId();
//        User loginUser = userService.getLoginUser(request);
//        Long userId = loginUser.getId();
//        TestSubmit testSubmit = new TestSubmit();
//        testSubmit.setTestId(testSubmitAddRequest.getTestId());
//        testSubmit.setUserId(userId);
//        testSubmit.setStatus(1);
//        for (TestSingleAnswer testSingleAnswer : trueOrFalseAnswerList) {
//            long id = testSingleAnswer.getId();
//            Integer value = testSingleAnswer.getValue();
//            TrueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest = new TrueOrFalseSubmitAddRequest();
//            trueOrFalseSubmitAddRequest.setTestId(testId);
//            trueOrFalseSubmitAddRequest.setAnswer(value);
//            trueOrFalseSubmitAddRequest.setQuestionId(id);
//            long submitId = trueOrFalseSubmitService.doTrueOrFalseSubmit(trueOrFalseSubmitAddRequest, loginUser);
//            if (submitId <= 0) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR,"判断题判题失败");
//            }
//        }
//
//        for (TestSingleAnswer testChoiceSingleAnswer : choiceAnswerList) {
//            long id = testChoiceSingleAnswer.getId();
//            Integer value = testChoiceSingleAnswer.getValue();
//            ChoiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest = new ChoiceQuestionSubmitAddRequest();
//            choiceQuestionSubmitAddRequest.setTestId(testId);
//            choiceQuestionSubmitAddRequest.setAnswer(value);
//            choiceQuestionSubmitAddRequest.setQuestionId(id);
//            long submitId = choiceQuestionSubmitService.doChoiceQuestionSubmit(choiceQuestionSubmitAddRequest, loginUser);
//            if (submitId <= 0) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR,"选择题判题失败");
//            }
//        }
//        QueryWrapper<TrueOrFalseSubmit> trueOrFalseSubmitQueryWrapper = new QueryWrapper<>();
//        trueOrFalseSubmitQueryWrapper.eq("testId", testId);
//        trueOrFalseSubmitQueryWrapper.eq("userId", userId);
//        trueOrFalseSubmitQueryWrapper.eq("status", StatusEnum.TRUE.getValue());
//        List<TrueOrFalseSubmit> trueOrFalseSubmitList = trueOrFalseSubmitService.list(trueOrFalseSubmitQueryWrapper);
//        List<Integer> trueOrFalseScores = trueOrFalseSubmitList.stream().map(trueOrFalseSubmit -> {
//            if (trueOrFalseSubmit != null) {
//                Long questionId = trueOrFalseSubmit.getQuestionId();
//                QueryWrapper<TestQuestion> testQuestionQueryWrapper = new QueryWrapper<>();
//                testQuestionQueryWrapper.eq("testId", testId);
//                testQuestionQueryWrapper.eq("questionId", questionId);
//                return testQuestionService.getOne(testQuestionQueryWrapper).getScore();
//            }
//            return 0;
//        }).collect(Collectors.toList());
//        QueryWrapper<ChoiceQuestionSubmit> choiceQuestionSubmitQueryWrapper = new QueryWrapper<>();
//        choiceQuestionSubmitQueryWrapper.eq("testId", testId);
//        choiceQuestionSubmitQueryWrapper.eq("userId", userId);
//        choiceQuestionSubmitQueryWrapper.eq("status",  StatusEnum.TRUE.getValue());
//        List<ChoiceQuestionSubmit> choiceQuestionSubmitList = choiceQuestionSubmitService.list(choiceQuestionSubmitQueryWrapper);
//        List<Integer> choiceQuestionScores = choiceQuestionSubmitList.stream().map(choiceQuestionSubmit -> {
//            if (choiceQuestionSubmit != null) {
//                Long questionId = choiceQuestionSubmit.getQuestionId();
//                QueryWrapper<TestQuestion> testQuestionQueryWrapper = new QueryWrapper<>();
//                testQuestionQueryWrapper.eq("testId", testId);
//                testQuestionQueryWrapper.eq("questionId", questionId);
//                return testQuestionService.getOne(testQuestionQueryWrapper).getScore();
//            }
//            return 0;
//        }).collect(Collectors.toList());
//        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
//        questionSubmitQueryWrapper.eq("testId", testId);
//        questionSubmitQueryWrapper.eq("userId", userId);
//        List<QuestionSubmit> questionSubmitList = questionSubmitService.list(questionSubmitQueryWrapper);
//        List<Integer> questionScoreList = questionSubmitList.stream().map(questionSubmit -> {
//            if (questionSubmit != null){
//                String judgeInfoStr = questionSubmit.getJudgeInfo();
//                JudgeInfo judgeInfoObj = GSON.fromJson(judgeInfoStr, JudgeInfo.class);
//                if (Objects.equals(judgeInfoObj.getMessage(), "Accept")) {
//                    Long questionId = questionSubmit.getQuestionId();
//                    QueryWrapper<TestQuestion> testQuestionQueryWrapper = new QueryWrapper<>();
//                    testQuestionQueryWrapper.eq("testId", testId);
//                    testQuestionQueryWrapper.eq("questionId", questionId);
//                    TestQuestion testQuestion = testQuestionService.getOne(testQuestionQueryWrapper);
//                    return testQuestion.getScore();
//                }
//            }
//            return 0;
//        }).collect(Collectors.toList());
//        int sum = 0;
//        for (Integer trueOrFalseScore : trueOrFalseScores) {
//            sum += trueOrFalseScore;
//        }
//        for (Integer choiceQuestionScore : choiceQuestionScores) {
//            sum += choiceQuestionScore;
//        }
//        for (Integer questionScore : questionScoreList) {
//            sum += questionScore;
//        }
//        QueryWrapper<TestUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userId", userId);
//        queryWrapper.eq("testId", testId);
//        TestUser testUser = testUserService.getOne(queryWrapper);
//        if (testUser == null) {
//            testUser = new TestUser();
//            testUser.setScore(sum);
//            testUser.setTestId(testId);
//            testUser.setUserId(userId);
//            boolean save = testUserService.save(testUser);
//            if (!save) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷用户关系表保存失败");
//            }
//        } else {
//            testUser.setScore(sum);
//            boolean b = testUserService.updateById(testUser);
//            if (!b) {
//                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷用户关系表更改失败");
//            }
//        }
//        testSubmit.setTestId(testId);
//        testSubmit.setUserId(userId);
//        testSubmit.setScore(sum);
//        testSubmit.setStatus(2);
//        boolean save = testSubmitService.save(testSubmit);
//        if (!save) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷提交保存失败");
//        }
//        Long id = testSubmit.getId();
//        return ResultUtils.success(id);
//    }


    @PostMapping("/test_submit/do")
    public BaseResponse<Long> doTestSubmit(@RequestBody TestSubmitAddRequest testSubmitAddRequest,
                                           HttpServletRequest request) {
        if (testSubmitAddRequest == null || testSubmitAddRequest.getTestId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<TestSingleAnswer> trueOrFalseAnswerList = testSubmitAddRequest.getTrueOrFalseAnswerList();
        List<TestSingleAnswer> choiceAnswerList = testSubmitAddRequest.getChoiceAnswerList();
        Long testId = testSubmitAddRequest.getTestId();
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        TestSubmit testSubmit = new TestSubmit();
        testSubmit.setTestId(testSubmitAddRequest.getTestId());
        testSubmit.setUserId(userId);
        testSubmit.setStatus(1);
        QueryWrapper<TestSubmit> testSubmitQueryWrapper = new QueryWrapper<>();
        testSubmitQueryWrapper.eq("testId", testId);
        testSubmitQueryWrapper.eq("userId", userId);
        TestSubmit testSubmitSearched = testSubmitService.getOne(testSubmitQueryWrapper);
        if (testSubmitSearched != null) {
            testSubmit.setId(testSubmitSearched.getId());
            testSubmit.setEndTime(new Date());
            boolean update = testSubmitService.updateById(testSubmit);
            if (!update) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷提交更新失败");
            }
        } else {
            testSubmit.setBeginTime(new Date());
            testSubmit.setEndTime(new Date());
            boolean save = testSubmitService.save(testSubmit);
            if (!save) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷提交保存失败");
            }
        }
        List<TrueOrFalseSubmit> trueOrFalseSubmitList = new ArrayList<>();
        for (TestSingleAnswer testSingleAnswer : trueOrFalseAnswerList) {
            long id = testSingleAnswer.getId();
            Integer value = testSingleAnswer.getValue();
            TrueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest = new TrueOrFalseSubmitAddRequest();
            trueOrFalseSubmitAddRequest.setTestId(testId);
            trueOrFalseSubmitAddRequest.setAnswer(value);
            trueOrFalseSubmitAddRequest.setQuestionId(id);
            long submitId = trueOrFalseSubmitService.doTrueOrFalseSubmit(trueOrFalseSubmitAddRequest, loginUser);
            if (submitId <= 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"判断题判题失败");
            }
            TrueOrFalseSubmit trueOrFalseSubmit = trueOrFalseSubmitService.getById(submitId);
            trueOrFalseSubmitList.add(trueOrFalseSubmit);
        }
        List<ChoiceQuestionSubmit> choiceQuestionSubmitList = new ArrayList<>();
        for (TestSingleAnswer testChoiceSingleAnswer : choiceAnswerList) {
            long id = testChoiceSingleAnswer.getId();
            Integer value = testChoiceSingleAnswer.getValue();
            ChoiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest = new ChoiceQuestionSubmitAddRequest();
            choiceQuestionSubmitAddRequest.setTestId(testId);
            choiceQuestionSubmitAddRequest.setAnswer(value);
            choiceQuestionSubmitAddRequest.setQuestionId(id);
            long submitId = choiceQuestionSubmitService.doChoiceQuestionSubmit(choiceQuestionSubmitAddRequest, loginUser);
            if (submitId <= 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"选择题判题失败");
            }
            ChoiceQuestionSubmit choiceQuestionSubmit = choiceQuestionSubmitService.getById(submitId);
            choiceQuestionSubmitList.add(choiceQuestionSubmit);
        }
        List<Integer> trueOrFalseScores = trueOrFalseSubmitList.stream().map(trueOrFalseSubmit -> {
            if (trueOrFalseSubmit != null && trueOrFalseSubmit.getStatus().equals(StatusEnum.TRUE.getValue())) {
                Long questionId = trueOrFalseSubmit.getQuestionId();
                QueryWrapper<TestQuestion> testQuestionQueryWrapper = new QueryWrapper<>();
                testQuestionQueryWrapper.eq("testId", testId);
                testQuestionQueryWrapper.eq("questionId", questionId);
                return testQuestionService.getOne(testQuestionQueryWrapper).getScore();
            }
            return 0;
        }).collect(Collectors.toList());
        List<Integer> choiceQuestionScores = choiceQuestionSubmitList.stream().map(choiceQuestionSubmit -> {
            if (choiceQuestionSubmit != null &&choiceQuestionSubmit.getStatus().equals(StatusEnum.TRUE.getValue())) {
                Long questionId = choiceQuestionSubmit.getQuestionId();
                QueryWrapper<TestQuestion> testQuestionQueryWrapper = new QueryWrapper<>();
                testQuestionQueryWrapper.eq("testId", testId);
                testQuestionQueryWrapper.eq("questionId", questionId);
                return testQuestionService.getOne(testQuestionQueryWrapper).getScore();
            }
            return 0;
        }).collect(Collectors.toList());
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
        questionSubmitQueryWrapper.eq("testId", testId);
        questionSubmitQueryWrapper.eq("userId", userId);
        List<QuestionSubmit> questionSubmitList = questionSubmitService.list(questionSubmitQueryWrapper);
        List<Integer> questionScoreList = questionSubmitList.stream().map(questionSubmit -> {
            if (questionSubmit != null){
                String judgeInfoStr = questionSubmit.getJudgeInfo();
                JudgeInfo judgeInfoObj = GSON.fromJson(judgeInfoStr, JudgeInfo.class);
                if (Objects.equals(judgeInfoObj.getMessage(), JudgeInfoMessageEnum.ACCEPTED.getValue())) {
                    Long questionId = questionSubmit.getQuestionId();
                    QueryWrapper<TestQuestion> testQuestionQueryWrapper = new QueryWrapper<>();
                    testQuestionQueryWrapper.eq("testId", testId);
                    testQuestionQueryWrapper.eq("questionId", questionId);
                    TestQuestion testQuestion = testQuestionService.getOne(testQuestionQueryWrapper);
                    return testQuestion.getScore();
                }
            }
            return 0;
        }).collect(Collectors.toList());
        int sum = 0;
        for (Integer trueOrFalseScore : trueOrFalseScores) {
            sum += trueOrFalseScore;
        }
        for (Integer choiceQuestionScore : choiceQuestionScores) {
            sum += choiceQuestionScore;
        }
        for (Integer questionScore : questionScoreList) {
            sum += questionScore;
        }
        QueryWrapper<TestUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("testId", testId);
        TestUser testUser = testUserService.getOne(queryWrapper);
        if (testUser == null) {
            testUser = new TestUser();
            testUser.setScore(sum);
            testUser.setTestId(testId);
            testUser.setUserId(userId);
            boolean save = testUserService.save(testUser);
            if (!save) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷用户关系表保存失败");
            }
        } else {
            Integer score = testUser.getScore();
            if (score == null || score < sum){
                testUser.setScore(sum);
                boolean b = testUserService.updateById(testUser);
                if (!b) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷用户关系表更改失败");
                }
            }
        }
        TestSubmit testSubmitUpdate = new TestSubmit();
        if (testSubmitSearched == null) {
            testSubmitUpdate.setId(testSubmit.getId());
        }else {
            testSubmitUpdate.setId(testSubmitSearched.getId());
        }
        testSubmitUpdate.setScore(sum);
        testSubmitUpdate.setStatus(TestSubmitStatusEnum.SUCCEED.getValue());
        boolean updateSubmit = testSubmitService.updateById(testSubmitUpdate);
        if (!updateSubmit) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"试卷提交更新失败");
        }
        Long id = testSubmitUpdate.getId();
        return ResultUtils.success(id);
    }

    @GetMapping("/test_submit/get/final_detail")
    public BaseResponse<TestSubmitFinalDetailVO> getFinalDetail(Long testSubmitId){
        if (testSubmitId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入id错误");
        }
        TestSubmit testSubmit = testSubmitService.getById(testSubmitId);
        if (testSubmit == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入id错误");
        }
        TestSubmitFinalDetailVO testSubmitFinalDetailVO = new TestSubmitFinalDetailVO();
        Long testId = testSubmit.getTestId();
        if (testId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入id错误");
        }
        Test test = testService.getById(testId);
        if (test == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "传入id错误");
        }
        testSubmitFinalDetailVO.setTitle(test.getTitle());
        testSubmitFinalDetailVO.setContent(test.getContent());
        testSubmitFinalDetailVO.setQuestionNum(test.getQuestionNum());
        testSubmitFinalDetailVO.setScore(testSubmit.getScore());
        testSubmitFinalDetailVO.setBeginTime(testSubmit.getBeginTime());
        testSubmitFinalDetailVO.setEndTime(testSubmit.getEndTime());
        testSubmitFinalDetailVO.setId(testSubmit.getId());
        return ResultUtils.success(testSubmitFinalDetailVO);
    }


    /**
     * List取题目提交列表（管理员）
     *
     * @param testSubmitQueryRequest
     * @param request
     * @return 提交记录 id
     */
    @PostMapping("/test_submit/list")
    public BaseResponse<List<TestSubmit>> listTestSubmit(@RequestBody TestSubmitQueryRequest testSubmitQueryRequest,
                                                                 HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        List<TestSubmit> testSubmitList = testSubmitService.list(testSubmitService.getQueryWrapper(testSubmitQueryRequest));
        return ResultUtils.success(testSubmitList);
    }


    @PostMapping("/test_submit/delete")
    public BaseResponse<Boolean> deleteTestSubmit(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        TestSubmit oldTestSubmit = testSubmitService.getById(id);
        ThrowUtils.throwIf(oldTestSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldTestSubmit.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = testSubmitService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("test_submit/update/backend")
    public BaseResponse<Boolean> updateTestSubmitBackend(@RequestBody TestSubmitUpdateRequest testSubmitUpdateRequest) {
        if (testSubmitUpdateRequest == null || testSubmitUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TestSubmit testSubmit = new TestSubmit();
        BeanUtils.copyProperties(testSubmitUpdateRequest, testSubmit);
        //校验
        Integer status = testSubmit.getStatus();
        if (TestSubmitStatusEnum.getEnumByValue(status) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "状态不存在");
        }
        long id = testSubmitUpdateRequest.getId();
        // 判断是否存在
        TestSubmit oldTestSubmit = testSubmitService.getById(id);
        ThrowUtils.throwIf(oldTestSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = testSubmitService.updateById(testSubmit);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<Test> getTestSubmitById(String testId, HttpServletRequest request) {
        Long id = Long.valueOf(testId);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Test test = testService.getById(id);
        return ResultUtils.success(test);
    }

    @GetMapping("/get/trueOrFalse")
    public BaseResponse<List<TrueOrFalseTestDetailVO>> GetTestTrueOeFalseDetail(String testId,
                                              HttpServletRequest request) {
        Long id = Long.valueOf(testId);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<TrueOrFalseTestDetailVO> trueOrFalseTestDetailVOList = trueOrFalseService.getTrueOrFalseTestDetailList(id);
        return ResultUtils.success(trueOrFalseTestDetailVOList);
    }

    @GetMapping("/get/choiceQuestion")
    public BaseResponse<List<ChoiceQuestionTestDetailVO>> GetChoiceQuestionDetail(String testId,
                                              HttpServletRequest request) {
        Long id = Long.valueOf(testId);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ChoiceQuestionTestDetailVO> choiceQuestionTestDetailVOList = choiceQuestionService.getChoiceQuestonTestDetailList(id);
        return ResultUtils.success(choiceQuestionTestDetailVOList);
    }

    @GetMapping("/get/question")
    public BaseResponse<List<QuestionTestDetailVO>> GetQuestionDetail(String testId,
                                              HttpServletRequest request) {
        Long id = Long.valueOf(testId);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<QuestionTestDetailVO> questionTestDetailVOList = questionService.getQuestionTestDetailList(id, request);
        return ResultUtils.success(questionTestDetailVOList);
    }

    @PostMapping("/title/id/list")
    public BaseResponse<List<TestTitleIdVO>> getTestTitleIdList( HttpServletRequest request) {
        List<Test> testList = testService.list();
        List<TestTitleIdVO> testTitleIdVOS = testList.stream().map(test -> {
            TestTitleIdVO testTitleIdVO = new TestTitleIdVO();
            BeanUtils.copyProperties(test, testTitleIdVO);
            return testTitleIdVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(testTitleIdVOS);
    }






}
