package com.hyl.zhanmaojbackendmodel.model.dto.test;


import lombok.Data;

import java.io.Serializable;

/**
 * 加入试卷请求
 *

 */
@Data
public class TestJoinRequest implements Serializable {

    /**
     * 试卷id
     */
    Long testId;

    /**
     * 密码
     */
    private String password;


    private static final long serialVersionUID = 1L;
}