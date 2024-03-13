package com.hyl.zhanmaoj.model.dto.user;

import com.hyl.zhanmaoj.model.vo.PageVO;
import com.hyl.zhanmaoj.model.vo.UserAdminVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserSearchAdminRequest implements Serializable {
    private PageVO pageVO;
    private UserAdminVO userAdminVO;
}
