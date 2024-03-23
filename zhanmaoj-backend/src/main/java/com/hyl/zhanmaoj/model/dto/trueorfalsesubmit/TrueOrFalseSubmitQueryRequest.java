package com.hyl.zhanmaoj.model.dto.trueorfalsesubmit;

import com.hyl.zhanmaoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TrueOrFalseSubmitQueryRequest extends PageRequest implements Serializable {


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

    private static final long serialVersionUID = 1L;
}