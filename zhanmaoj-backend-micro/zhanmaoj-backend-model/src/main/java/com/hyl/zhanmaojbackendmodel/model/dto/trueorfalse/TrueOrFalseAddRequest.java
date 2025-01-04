package com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 */
@Data
public class TrueOrFalseAddRequest implements Serializable {


    /**
     * 内容
     */
    private String title;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private Integer answer;

    private static final long serialVersionUID = 1L;
}