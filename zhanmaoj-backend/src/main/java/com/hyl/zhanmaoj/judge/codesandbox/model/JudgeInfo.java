package com.hyl.zhanmaoj.judge.codesandbox.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题信息
 */

@Data
public class JudgeInfo implements Serializable {
    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间
     */
    private Long time;

    /**
     * 程序运行详细信息信息
     */
    private String detailErrorMessage;

    private static final long serialVersionUID = 1L;
}
