package com.hyl.zhanmaojbackendmodel.model.dto.test;


import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *

 */
@Data
public class TestSingleAnswer implements Serializable {


    /**
     * 标题
     */
    private long id;

    /**
     * 状态 0-表示未填写，1-正确/A，2-表示错误/B，3-表示C，4-表示D
     */
    private Integer value;


    private static final long serialVersionUID = 1L;
}