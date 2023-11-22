import ACCESS_ENUM from "@/access/accessEnum";
import router from "@/router";
import store from "@/store";
import checkAccess from "@/access/checkAccess";

router.beforeEach(async (to, from, next) => {
    const loginUser = store.state.user?.loginUser;

    // 自动登录
    if (!loginUser || !loginUser.userRole) {
        await store.dispatch("user/getLoginUser");
    }
    const needAccess = to.meta.access ?? ACCESS_ENUM.NOT_LOGIN;
    //如果没有登录，跳转到登录页面
    if (needAccess !== ACCESS_ENUM.NOT_LOGIN) {
        if (!loginUser || !loginUser.userRole){
            next(`user/login?redirect=${to.fullPath}`);
            return;
        }
        //如果已经登录了，但是没有对应的权限，跳转到无权限页面
        if (!checkAccess(loginUser, needAccess as string)){
            next('/noAuth');
        }
    }

    next();
})