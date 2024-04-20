package com.hyl.zhanmaoj.model.dto.test;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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