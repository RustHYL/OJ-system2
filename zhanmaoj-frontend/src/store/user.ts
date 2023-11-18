
// initial state
import {StoreOptions} from "vuex";


// getters
const getters = {}

// actions（支持异步）
// 执行异步操作，触发mutation的更改（actions调用mutation）


// mutations（尽量同步）
//更新变量的方法（增删改）

export default {
    namespaced: true,
    state:() => ({
        loginUser: {
            username: '未登录',
            role: 'notLogin',
        },
    }),
    getters,
    actions: {
        async getLoginUser ({ commit, state }, payload) {
            commit('updateUser', { username: 'bilibili'})
        }
    },
    mutations: {
        updateUser (state, payload) {
            state.loginUser = payload;
        },
    }
} as StoreOptions<any>