package com.hyl.zhanmaojbackendcommon.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 */
@Data
public class IdTitleVO implements Serializable {

    /**
     * id
     */
    private Long id;

    private String title;

    private static final long serialVersionUID = 1L;
}