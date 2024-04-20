package com.hyl.zhanmaoj.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试卷
 * @TableName test
 */
@TableName(value ="test")
@Data
public class MyTestVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 题目数量
     */
    private Integer questionNum;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 过期时间
     */
    private Date expiredTime;

    /**
     * 做题时间
     */
    private Long examTime;

    /**
     * 总分
     */
    private Integer totalScore;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 分数
     */
    private Integer score;

    private static final long serialVersionUID = 1L;
}