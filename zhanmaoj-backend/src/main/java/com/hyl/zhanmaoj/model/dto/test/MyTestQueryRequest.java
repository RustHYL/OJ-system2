package com.hyl.zhanmaoj.model.dto.test;


import com.hyl.zhanmaoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyTestQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 题目
     */
    private String title;


    /**
     * 获取个数
     */
    private Long num;

    private static final long serialVersionUID = 1L;
}