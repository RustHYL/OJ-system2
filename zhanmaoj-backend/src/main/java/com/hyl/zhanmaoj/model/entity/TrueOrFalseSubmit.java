package com.hyl.zhanmaoj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 判断题提交信息
 * @TableName true_or_false_submit
 */
@TableName(value ="true_or_false_submit")
@Data
public class TrueOrFalseSubmit implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 答案 0 正确 1错误
     */
    private Integer answer;

    /**
     * 判题状态（0 - 错误、1 - 正确）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 试卷 id
     */
    private Long testId;

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