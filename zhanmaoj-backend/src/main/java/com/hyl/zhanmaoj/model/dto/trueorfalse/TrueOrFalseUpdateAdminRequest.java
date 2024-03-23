package com.hyl.zhanmaoj.model.dto.trueorfalse;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *

 */
@Data
public class TrueOrFalseUpdateAdminRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 题目答案
     */
    private Integer answer;


    private static final long serialVersionUID = 1L;
}