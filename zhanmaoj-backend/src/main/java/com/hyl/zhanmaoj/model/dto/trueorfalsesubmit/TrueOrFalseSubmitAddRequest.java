package com.hyl.zhanmaoj.model.dto.trueorfalsesubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *

 */
@Data
public class TrueOrFalseSubmitAddRequest implements Serializable {

    /**
     * 用户答案
     */
    private Integer answer;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 试卷 id
     */
    private Long testId;

    private static final long serialVersionUID = 1L;
}