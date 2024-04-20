package com.hyl.zhanmaoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 */
@Data
public class QuestionTestAddRequest implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 分数
     */
    private Integer score;

    private String testId;

    private static final long serialVersionUID = 1L;
}