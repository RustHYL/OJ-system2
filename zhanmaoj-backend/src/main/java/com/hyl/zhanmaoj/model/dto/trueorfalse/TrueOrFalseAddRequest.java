package com.hyl.zhanmaoj.model.dto.trueorfalse;


import com.hyl.zhanmaoj.model.dto.question.JudgeCase;
import com.hyl.zhanmaoj.model.dto.question.JudgeConfig;
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
    private String content;

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