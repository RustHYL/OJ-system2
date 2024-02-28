package com.hyl.zhanmaojcodesandbox.security;

import java.security.Permission;

//禁用所有的java安全管理器实现
public class DenySecurityManager extends SecurityManager{
    @Override
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
        throw new SecurityException("权限异常" + perm.toString());
    }
}
