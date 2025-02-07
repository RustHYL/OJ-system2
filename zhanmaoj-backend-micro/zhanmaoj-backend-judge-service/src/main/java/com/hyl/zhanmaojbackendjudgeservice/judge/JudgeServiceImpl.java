package com.hyl.zhanmaojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.hyl.zhanmaojbackendcommon.common.ErrorCode;
import com.hyl.zhanmaojbackendcommon.exception.BusinessException;
import com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.hyl.zhanmaojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.hyl.zhanmaojbackendjudgeservice.judge.strategy.JudgeContext;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.JudgeInfo;
import com.hyl.zhanmaojbackendmodel.model.dto.question.JudgeCase;
import com.hyl.zhanmaojbackendmodel.model.entity.Question;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionSubmit;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionWrong;
import com.hyl.zhanmaojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.hyl.zhanmaojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.hyl.zhanmaojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;


    @Resource JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
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
        boolean result = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
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
        JudgeInfo judgeInfo = new JudgeInfo();
        String message = executeCodeResponse.getMessage();
        if (message != null){
            //两种错误 编译错误 运行错误
            if (message.contains("compile error")){
                judgeInfo.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getValue());
            } else {
                judgeInfo.setMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getValue());
                judgeInfo.setMemory(executeCodeResponse.getJudgeInfo().getMemory());
                judgeInfo.setTime(executeCodeResponse.getJudgeInfo().getTime());
                judgeInfo.setDetailErrorMessage(executeCodeResponse.getMessage());
            }
        } else {
            //选择策略
            judgeInfo = judgeManager.doJudge(judgeContext);
        }
        //修改数据库的判题记录
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }
        Question updateQuestion = new Question();
        updateQuestion.setId(questionId);
        updateQuestion.setSubmitNum(question.getSubmitNum() + 1);
        if (judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())){
            updateQuestion.setAcceptedNum(question.getAcceptedNum() + 1);
        }
        boolean updateQuestionResult = questionFeignClient.updateQuestionById(updateQuestion);
        if (!updateQuestionResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交数量更新失败");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionSubmitId);
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
            Question question1 = questionFeignClient.getQuestionById(questionId1);
            questionWrong.setTitle(question1.getTitle());
            boolean save = questionFeignClient.saveQuestionWrong(questionWrong);
            if (!save){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "错题记录保存失败");
            }
        }        
        return questionSubmitResult;
    }
}
