package com.hyl.zhanmaoj.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChoiceTestVO  implements Serializable {

    /**
     * 答案 0 A 1 B 2 C 3 D
     */
    private Integer answer;

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
