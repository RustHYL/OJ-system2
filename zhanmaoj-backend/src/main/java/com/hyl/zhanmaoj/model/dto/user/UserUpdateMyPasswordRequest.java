package com.hyl.zhanmaoj.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新个人信息请求
 */
@Data
public class UserUpdateMyPasswordRequest implements Serializable {

    /**
     * 新密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String confirmPassword;

    private static final long serialVersionUID = 1L;
}