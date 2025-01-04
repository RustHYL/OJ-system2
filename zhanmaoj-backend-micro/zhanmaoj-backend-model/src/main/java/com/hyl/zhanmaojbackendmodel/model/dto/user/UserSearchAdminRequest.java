package com.hyl.zhanmaojbackendmodel.model.dto.user;


import com.hyl.zhanmaojbackendmodel.model.vo.PageVO;
import com.hyl.zhanmaojbackendmodel.model.vo.UserAdminVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserSearchAdminRequest implements Serializable {
    private PageVO pageVO;
    private UserAdminVO userAdminVO;
}
