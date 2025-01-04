package com.hyl.zhanmaojbackendmodel.model.vo;

import com.hyl.zhanmaojbackendmodel.model.dto.choicequestion.ChoiceQuestionTestAddRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.question.QuestionTestAddRequest;
import com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse.TrueOrFalseTestAddRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class QueryRequest implements Serializable {
    private QuestionTestAddRequest questionTestAddRequest;

    private ChoiceQuestionTestAddRequest choiceQuestionTestAddRequest;

    private TrueOrFalseTestAddRequest trueOrFalseTestAddRequest;
}
