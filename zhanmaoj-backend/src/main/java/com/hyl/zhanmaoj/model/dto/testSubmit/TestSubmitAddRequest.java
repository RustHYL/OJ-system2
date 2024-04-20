package com.hyl.zhanmaoj.model.dto.testSubmit;

import com.hyl.zhanmaoj.model.dto.test.TestSingleAnswer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestSubmitAddRequest implements Serializable {


    /**
     * List<TestSingleAnswer> id answer
     */
    private List<TestSingleAnswer> trueOrFalseAnswerList;

    /**
     * List<TestSingleAnswer> id answer
     */
    private List<TestSingleAnswer> choiceAnswerList;

    /**
     * 试卷 id
     */
    private Long testId;

    private static final long serialVersionUID = 1L;
}
