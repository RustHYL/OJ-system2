package com.hyl.zhanmaoj.model.vo;

import com.hyl.zhanmaoj.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class QuestionTestAdminVO extends PageRequest implements Serializable {
    private Long id;

    private Long questionId;

    /**
     * 标题
     */
    private String title;


    /**
     * 标签列表（json 数组）
     */
    private String tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例（json 数组）
     */
    private String judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private String judgeConfig;

    /**
     * 分数
     */
    private Integer score;

    private static final long serialVersionUID = 1L;

}
