package com.hyl.zhanmaojbackendmodel.model.dto.test;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 */
@Data
public class TestUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 状态 0-表示公开，1-表示私有，2-表示加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

    /**
     * 内容
     */
    private String content;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 过期时间
     */
    private Date expiredTime;

    /**
     * 做题时间
     */
    private Long examTime;



    private static final long serialVersionUID = 1L;
}