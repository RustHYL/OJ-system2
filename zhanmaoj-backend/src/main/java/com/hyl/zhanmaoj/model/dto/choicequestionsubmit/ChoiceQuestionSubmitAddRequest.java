package com.hyl.zhanmaoj.model.dto.choicequestionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *

 */
@Data
public class ChoiceQuestionSubmitAddRequest implements Serializable {

    /**
     * 用户代码
     */
    private Integer answer;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 题目 id
     */
    private Long testId;

    private static final long serialVersionUID = 1L;
}