package com.hyl.zhanmaoj.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionAddAdminRequest implements Serializable {


    /**
     * 标题
     */
    private String title;

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
    private String answer;

    /**
     * 判题用例
     */
    private String judgeCase;

    /**
     * 判题配置
     */
    private String judgeConfig;


    private static final long serialVersionUID = 1L;
}
