
// initial state
import {StoreOptions} from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import {UserControllerService} from "../../generated";

// actions（支持异步）
// 执行异步操作，触发mutation的更改（actions调用mutation）


// mutations（尽量同步）
//更新变量的方法（增删改）

export default {
    namespaced: true,
    state:() => ({
        loginUser: {
            userName: '未登录',
        },
    }),
    actions: {
        async getLoginUser ({ commit, state }, payload) {
            const res = await UserControllerService.getLoginUserUsingGet();
            if (res.code === 0){
                commit('updateUser', res.data)
            } else {
                commit('updateUser', { ...state.loginUser, userRole: ACCESS_ENUM.NOT_LOGIN })
            }
        }
    },
    mutations: {
        updateUser (state, payload) {
            state.loginUser = payload;
        },
    }
} as StoreOptions<any>