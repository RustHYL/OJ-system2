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
public class TestDetailGetRequest implements Serializable {


    /**
     * id
     */
    private Integer id;

    private static final long serialVersionUID = 1L;
}