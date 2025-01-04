package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TrueOrFalseTestAdminVO implements Serializable {
    /**
     * id
     */
    private Long id;

    private Long questionId;

    /**
     * 标题
     */
    private String title;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 答案
     */
    private Integer answer;

    /**
     * 分数
     */
    private Integer score;

    private static final long serialVersionUID = 1L;
}
