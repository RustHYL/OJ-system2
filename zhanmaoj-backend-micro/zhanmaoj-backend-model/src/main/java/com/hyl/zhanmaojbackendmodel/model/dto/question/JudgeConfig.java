package com.hyl.zhanmaojbackendmodel.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目配置
 */

@Data
public class JudgeConfig implements Serializable {
    /**
     * 时间限制（ms）
     */
    private Long timeLimit;

    /**
     * 内存限制（kb）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（kb）
     */
    private Long stackLimit;

    private static final long serialVersionUID = 1L;
}
