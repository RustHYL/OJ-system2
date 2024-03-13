import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查权限
 * @param loginUser 当前登录用户
 * @param needAccess 需要的权限
 * @return boolean 有无权限
 */
const checkAccess = (loginUser: any, needAccess = ACCESS_ENUM.NOT_LOGIN) =>{
    const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;
    //用户未登录也能访问
    if (needAccess === ACCESS_ENUM.NOT_LOGIN){
        return true
    }
    //需要用户登录才能访问
    if (needAccess === ACCESS_ENUM.USER){
        return loginUserAccess !== ACCESS_ENUM.NOT_LOGIN;
    }
    //需要管理员权限
    if (needAccess === ACCESS_ENUM.ADMIN){
        return loginUserAccess === ACCESS_ENUM.ADMIN;
    }
    //需要超级管理员
    if (needAccess === ACCESS_ENUM.SUPER){
        return loginUserAccess === ACCESS_ENUM.SUPER;
    }
}

export default checkAccess