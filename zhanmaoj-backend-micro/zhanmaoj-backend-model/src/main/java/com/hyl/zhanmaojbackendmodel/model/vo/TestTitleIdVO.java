package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestTitleIdVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;


    private static final long serialVersionUID = 1L;
}
