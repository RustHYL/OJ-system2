package com.hyl.zhanmaoj.model.dto.choicequestionsubmit;

import com.hyl.zhanmaoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求（管理员）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChoiceQuestionSubmitQueryAdminRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户 id
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

    private static final long serialVersionUID = 1L;
}