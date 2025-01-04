package com.hyl.zhanmaojbackendmodel.model.dto.test;


import com.hyl.zhanmaojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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