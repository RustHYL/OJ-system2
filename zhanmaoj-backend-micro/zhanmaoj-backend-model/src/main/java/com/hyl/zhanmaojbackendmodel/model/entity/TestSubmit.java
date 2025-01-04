package com.hyl.zhanmaojbackendmodel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试卷提交
 * @TableName test_submit
 */
@TableName(value ="test_submit")
@Data
public class TestSubmit implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 -已判题）
     */
    private Integer status;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 题目 id
     */
    private Long testId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 做题开始时间
     */
    private Date beginTime;


    /**
     * 做题结束时间
     */
    private Date endTime;


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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}