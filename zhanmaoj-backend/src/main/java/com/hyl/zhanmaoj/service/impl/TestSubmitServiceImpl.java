package com.hyl.zhanmaoj.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.model.dto.choicequestionsubmit.ChoiceQuestionSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.testSubmit.TestSubmitAddRequest;
import com.hyl.zhanmaoj.model.dto.trueorfalsesubmit.TrueOrFalseSubmitAddRequest;
import com.hyl.zhanmaoj.model.entity.*;
import com.hyl.zhanmaoj.model.vo.ChoiceTestVO;
import com.hyl.zhanmaoj.model.vo.TrueOrFalseTestVO;
import com.hyl.zhanmaoj.service.*;
import com.hyl.zhanmaoj.mapper.TestSubmitMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author Alan
* @description 针对表【test_submit(试卷提交)】的数据库操作Service实现
* @createDate 2024-03-17 16:50:59
*/
@Service
public class TestSubmitServiceImpl extends ServiceImpl<TestSubmitMapper, TestSubmit>
    implements TestSubmitService{

    private static final Gson GSON = new Gson();

    @Resource
    private TrueOrFalseSubmitService trueOrFalseSubmitService;

    @Resource
    private ChoiceQuestionSubmitService choiceQuestionSubmitService;


    @Override
    public long doTestSubmit(TestSubmitAddRequest testSubmitAddRequest, User loginUser) {
        String trueOrFalseRequest = testSubmitAddRequest.getTrueOrFalseRequest();
        List<TrueOrFalseSubmitAddRequest> trueOrFalseSubmitAddRequestList = new ArrayList<>();
        Long testId = testSubmitAddRequest.getTestId();
        List<Object> trueOrFalseRequestList = new ArrayList<>();
        if (StringUtils.isNotBlank(trueOrFalseRequest)) {
            try {
                trueOrFalseRequestList = GSON.fromJson(trueOrFalseRequest, List.class);
                for (Object o : trueOrFalseRequestList) {
                    String json = GSON.toJson(o, Object.class);
                    TrueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest = GSON.fromJson(json, TrueOrFalseSubmitAddRequest.class);
                    trueOrFalseSubmitAddRequestList.add(trueOrFalseSubmitAddRequest);
                }
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + trueOrFalseRequestList);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入格式错误");
            }
        }
        String choiceRequest = testSubmitAddRequest.getChoiceRequest();
        List<ChoiceQuestionSubmitAddRequest> choiceQuestionSubmitAddRequestList = new ArrayList<>();
        List<Object> choiceRequestRequestList = new ArrayList<>();
        if (StringUtils.isNotBlank(choiceRequest)) {
            try {
                choiceRequestRequestList = GSON.fromJson(choiceRequest, List.class);
                for (Object o : choiceRequestRequestList) {
                    String json = GSON.toJson(o, Object.class);
                    ChoiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest = GSON.fromJson(json, ChoiceQuestionSubmitAddRequest.class);
                    choiceQuestionSubmitAddRequestList.add(choiceQuestionSubmitAddRequest);
                }
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + choiceRequestRequestList);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入格式错误");
            }
        }
        String questionAnswer = testSubmitAddRequest.getQuestionAnswer();
        List<QuestionSubmit> questionSubmitList = new ArrayList<>();
        List<Object> questionSubmitObjList = new ArrayList<>();
        if (StringUtils.isNotBlank(choiceRequest)) {
            try {
                questionSubmitObjList = GSON.fromJson(questionAnswer, List.class);
                for (Object o : questionSubmitObjList) {
                    String json = GSON.toJson(o, Object.class);
                    QuestionSubmit questionSubmit = GSON.fromJson(json, QuestionSubmit.class);
                    questionSubmitList.add(questionSubmit);
                }
                // 如果转换成功，这里会执行
                System.out.println("Conversion successful: " + questionSubmitList);
            } catch (JsonSyntaxException e) {
                // 如果转换失败，这里会捕获异常并执行
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "输入格式错误");
            }
        }
        TestSubmit testSubmit = new TestSubmit();
        testSubmit.setStatus(0);
        List<TrueOrFalseTestVO> trueOrFalseTestVOList = new ArrayList<>();
        for (TrueOrFalseSubmitAddRequest trueOrFalseSubmitAddRequest : trueOrFalseSubmitAddRequestList) {
            TrueOrFalseTestVO trueOrFalseTestVO = new TrueOrFalseTestVO();
            trueOrFalseTestVO.setAnswer(trueOrFalseSubmitAddRequest.getAnswer());
            trueOrFalseTestVO.setQuestionId(trueOrFalseSubmitAddRequest.getQuestionId());
            trueOrFalseSubmitAddRequest.setTestId(testId);
            long l = trueOrFalseSubmitService.doTrueOrFalseSubmit(trueOrFalseSubmitAddRequest, loginUser);
            TrueOrFalseSubmit obj = trueOrFalseSubmitService.getById(l);
            trueOrFalseTestVO.setStatus(obj.getStatus());
            trueOrFalseTestVOList.add(trueOrFalseTestVO);
        }
        testSubmit.setTrueOrFalseAnswers(JSONUtil.toJsonStr(trueOrFalseTestVOList));
        List<ChoiceTestVO> choiceTestVOList = new ArrayList<>();
        for (ChoiceQuestionSubmitAddRequest choiceQuestionSubmitAddRequest : choiceQuestionSubmitAddRequestList) {
            ChoiceTestVO choiceTestVO = new ChoiceTestVO();
            choiceTestVO.setAnswer(choiceQuestionSubmitAddRequest.getAnswer());
            choiceTestVO.setQuestionId(choiceQuestionSubmitAddRequest.getQuestionId());
            choiceQuestionSubmitAddRequest.setTestId(testId);
            long l = choiceQuestionSubmitService.doChoiceQuestionSubmit(choiceQuestionSubmitAddRequest, loginUser);
            ChoiceQuestionSubmit obj = choiceQuestionSubmitService.getById(l);
            choiceTestVO.setStatus(obj.getStatus());
            choiceTestVOList.add(choiceTestVO);
        }
        testSubmit.setChoiceQuestionAnswers(JSONUtil.toJsonStr(choiceTestVOList));
        testSubmit.setStatus(2);
        //score获取和question
        testSubmit.setTestId(testId);
        testSubmit.setUserId(loginUser.getId());
        boolean save = this.save(testSubmit);
        return testSubmit.getId();
    }
}




