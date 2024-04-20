package com.hyl.zhanmaoj.model.dto.test;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 */
@Data
public class TestQuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 分值
     */
    private Integer score;




    private static final long serialVersionUID = 1L;
}