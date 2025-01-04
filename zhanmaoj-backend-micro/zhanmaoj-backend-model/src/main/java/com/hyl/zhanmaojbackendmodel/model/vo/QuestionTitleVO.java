package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目封装类
 * @TableName question
 */
@Data
public class QuestionTitleVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private Integer type;


    private static final long serialVersionUID = 1L;
}