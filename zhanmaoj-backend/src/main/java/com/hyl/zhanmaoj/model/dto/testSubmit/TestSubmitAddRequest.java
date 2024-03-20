package com.hyl.zhanmaoj.model.dto.testSubmit;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestSubmitAddRequest implements Serializable {


    /**
     * List<addRequest> JSON(answer, questionId, testId?)
     */
    private String trueOrFalseRequest;

    /**
     * List<addRequest> JSON(answer, questionId, testId?)
     */
    private String choiceRequest;

    /**
     * List<Long> submitId
     */
    private String questionAnswer;


    /**
     * 试卷 id
     */
    private Long testId;

    private static final long serialVersionUID = 1L;
}
