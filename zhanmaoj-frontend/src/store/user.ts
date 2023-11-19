
// initial state
import {StoreOptions} from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";

// actions（支持异步）
// 执行异步操作，触发mutation的更改（actions调用mutation）


// mutations（尽量同步）
//更新变量的方法（增删改）

export default {
    namespaced: true,
    state:() => ({
        loginUser: {
            username: '未登录',
            userRole: ACCESS_ENUM.NOT_LOGIN,
        },
    }),
    actions: {
        getLoginUser ({ commit, state }, payload) {
            commit('updateUser', payload)
        }
    },
    mutations: {
        updateUser (state, payload) {
            state.loginUser = payload;
        },
    }
} as StoreOptions<any>