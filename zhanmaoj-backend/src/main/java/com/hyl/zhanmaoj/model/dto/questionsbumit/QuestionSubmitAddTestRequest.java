package com.hyl.zhanmaoj.model.dto.questionsbumit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *

 */
@Data
public class QuestionSubmitAddTestRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * testId
     */
    private Long testId;

    private static final long serialVersionUID = 1L;
}