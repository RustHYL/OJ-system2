package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChoiceQuestionTestAdminVO implements Serializable {

    /**
     * id
     */
    private Long id;

    private Long questionId;

    /**
     * 内容
     */
    private String title;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 答案
     */
    private Integer answer;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;
}
