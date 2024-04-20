package com.hyl.zhanmaoj.model.vo;

import com.hyl.zhanmaoj.model.dto.choicequestion.ChoiceQuestionTestAddRequest;
import com.hyl.zhanmaoj.model.dto.question.QuestionTestAddRequest;
import com.hyl.zhanmaoj.model.dto.trueorfalse.TrueOrFalseTestAddRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class QueryRequest implements Serializable {
    private QuestionTestAddRequest questionTestAddRequest;

    private ChoiceQuestionTestAddRequest choiceQuestionTestAddRequest;

    private TrueOrFalseTestAddRequest trueOrFalseTestAddRequest;
}
