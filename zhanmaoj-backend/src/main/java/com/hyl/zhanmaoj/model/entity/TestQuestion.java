package com.hyl.zhanmaoj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目题目关联
 * @TableName test_question
 */
@TableName(value ="test_question")
@Data
public class TestQuestion implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 试卷 id
     */
    private Long testId;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 题目分数
     */
    private Integer score;

    /**
     * 类型 0-判断题，1-选择题，2-编程题
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}