package com.hyl.zhanmaoj.model.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class ChoiceQuestionTestDetailVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String title;

    /**
     * 选项 A
     */
    private String optionA;

    /**
     * 选项 B
     */
    private String optionB;

    /**
     * 选项 C
     */
    private String optionC;

    /**
     * 选项 D
     */
    private String optionD;

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
