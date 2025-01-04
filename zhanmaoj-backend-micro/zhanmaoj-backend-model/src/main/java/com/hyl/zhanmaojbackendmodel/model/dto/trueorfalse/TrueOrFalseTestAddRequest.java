package com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 */
@Data
public class TrueOrFalseTestAddRequest implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 分数
     */
    private Integer score;

    private String testId;

    private static final long serialVersionUID = 1L;
}