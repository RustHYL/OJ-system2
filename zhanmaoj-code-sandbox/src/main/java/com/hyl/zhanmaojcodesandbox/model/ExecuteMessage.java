package com.hyl.zhanmaojcodesandbox.model;

import lombok.Data;

/**
 * 执行信息
 */
@Data
public class ExecuteMessage {
    private Integer exitValue;
    private String message;
    private String errorMessage;
    private Long time;
    private Long memory;
}
