package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TrueOrFalseTitleVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目
     */
    private String title;

    /**
     * 类型
     */
    private Integer type;

    private static final long serialVersionUID = 1L;
}
