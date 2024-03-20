package com.hyl.zhanmaoj.model.dto.choicequestion;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 */
@Data
public class ChoiceQuestionAddAdminRequest implements Serializable {


    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 题目答案A
     */
    private String optionA;

    /**
     * 题目答案B
     */
    private String optionB;

    /**
     * 题目答案C
     */
    private String optionC;

    /**
     * 题目答案B
     */
    private String optionD;

    /**
     * 答案
     */
    private Integer answer;

    private static final long serialVersionUID = 1L;
}