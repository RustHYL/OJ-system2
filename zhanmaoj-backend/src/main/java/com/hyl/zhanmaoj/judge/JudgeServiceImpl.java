package com.hyl.zhanmaoj.judge;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaoj.common.ErrorCode;
import com.hyl.zhanmaoj.exception.BusinessException;
import com.hyl.zhanmaoj.judge.codesandbox.CodeSandbox;
import com.hyl.zhanmaoj.judge.codesandbox.CodeSandboxFactory;
import com.hyl.zhanmaoj.judge.codesandbox.CodeSandboxProxy;
import com.hyl.zhanmaoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.hyl.zhanmaoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.hyl.zhanmaoj.judge.strategy.JudgeContext;
import com.hyl.zhanmaoj.model.dto.question.JudgeCase;
import com.hyl.zhanmaoj.judge.codesandbox.model.JudgeInfo;
import com.hyl.zhanmaoj.model.entity.Question;
import com.hyl.zhanmaoj.model.entity.QuestionSubmit;
import com.hyl.zhanmaoj.model.entity.QuestionWrong;
import com.hyl.zhanmaoj.model.enums.JudgeInfoMessageEnum;
import com.hyl.zhanmaoj.model.enums.QuestionSubmitStatusEnum;
import com.hyl.zhanmaoj.service.QuestionService;
import com.hyl.zhanmaoj.service.QuestionSubmitService;
import com.hyl.zhanmaoj.service.QuestionWrongService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionWrongService questionWrongService;

    @Resource JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        //判定提交状态，防止重复执行
        if (!Objects.equals(questionSubmit.getStatus(), QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目已在判题");
        }
        //更改判题状态为判题中，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionSubmitUpdate.setId(questionSubmitId);
        boolean result = questionSubmitService.updateById(questionSubmitUpdate);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }
        //调用代码沙箱
        CodeSandbox codeSandBox = CodeSandboxFactory.newInstance(type);
        codeSandBox = new CodeSandboxProxy(codeSandBox);
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        //根据执行结果判断题目状态（是否执行错误)
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        //选择策略
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //修改数据库的判题记录
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        String message = judgeInfo.getMessage();
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }
        Question updateQuestion = new Question();
        updateQuestion.setId(questionId);
        updateQuestion.setSubmitNum(question.getSubmitNum() + 1);
        if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())){
            updateQuestion.setAcceptedNum(question.getAcceptedNum() + 1);
        }
        boolean updateQuestionResult = questionService.updateById(updateQuestion);
        if (!updateQuestionResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交数量更新失败");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        if (!judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())){
            Long questionId1 = questionSubmitResult.getQuestionId();
            String language1 = questionSubmitResult.getLanguage();
            String code1 = questionSubmitResult.getCode();
            String judgeInfo1 = questionSubmitResult.getJudgeInfo();
            Long userId = questionSubmitResult.getUserId();
            QuestionWrong questionWrong = new QuestionWrong();
            questionWrong.setLanguage(language1);
            questionWrong.setCode(code1);
            questionWrong.setJudgeInfo(judgeInfo1);
            questionWrong.setQuestionId(questionId1);
            questionWrong.setUserId(userId);
            questionWrong.setTags(question.getTags());
            questionWrong.setSubmitId(questionSubmitResult.getId());
            Question question1 = questionService.getById(questionId1);
            questionWrong.setTitle(question1.getTitle());
            boolean save = questionWrongService.save(questionWrong);
            if (!save){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "错题记录保存失败");
            }
        }        
        return questionSubmitResult;
    }
}
