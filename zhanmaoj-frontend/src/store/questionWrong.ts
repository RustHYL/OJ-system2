
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
        questionWrongNow: {},
    }),
    actions: {

    },
    mutations: {
        updateQuestionWrong (state, payload) {
            state.questionWrong = payload;
        },
    }
} as StoreOptions<any>