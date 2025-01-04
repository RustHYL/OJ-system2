package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页
 */
@Data
public class PageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer current;

    private Integer size;


}
