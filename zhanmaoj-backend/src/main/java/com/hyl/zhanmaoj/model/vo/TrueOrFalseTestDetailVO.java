package com.hyl.zhanmaoj.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TrueOrFalseTestDetailVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目
     */
    private String content;

    /**
     * 题目分数
     */
    private Integer score;

    /**
     * 答案
     */
    private Integer answer;

    private static final long serialVersionUID = 1L;
}
