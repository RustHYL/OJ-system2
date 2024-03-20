package com.hyl.zhanmaoj.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProgramQuestionTestVO implements Serializable {

    /**
     * 答案
     */
    private String code;

    /**
     * 判题状态（0 - 错误、1 - 正确）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
