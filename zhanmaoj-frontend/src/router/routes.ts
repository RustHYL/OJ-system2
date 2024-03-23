import {RouteRecordRaw} from "vue-router";
import HomeView from "@/views/HomeView.vue";
import NoAuthView from "@/views/NoAuthView.vue";
import ACCESS_ENUM from "@/access/accessEnum";
import UserLayout from "@/layouts/UserLayout.vue";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";
import UserSettingView from "@/views/user/UserSettingView.vue";
import UserCenterView from "@/views/user/UserCenterView.vue";
import AddQuestionView from "@/views/question/AddQuestionView.vue";
import ManageQuestionView from "@/views/question/ManageQuestionView.vue";
import QuestionsView from "@/views/question/QuestionsView.vue";
import QuestionSubmitView from "@/views/question/QuestionSubmitView.vue";
import QuestionSubmitMyView from "@/views/question/QuestionSubmitMineView.vue";
import AnswerQuestionView from "@/views/question/AnswerQuestionView.vue";
import TestView from "@/views/test/TestCardView.vue";
import TestDoView from "@/views/test/TestDoView.vue";


export const routes: Array<RouteRecordRaw> = [
    {
        path: '/user',
        name: '用户',
        component: UserLayout,
        children:[
            {
                path: '/user/login',
                name: '用户登录',
                component: UserLoginView
            },
            {
                path: '/user/register',
                name: '用户注册',
                component: UserRegisterView
            }
        ],
        meta: {
            hideInMenu: true
        }
    },
    {
        path: '/',
        name: '主页',
        component: HomeView
    },
    {
        path: '/question/add',
        name: '创建题目',
        component: AddQuestionView,
        meta: {
            access: ACCESS_ENUM.ADMIN,
        }
    },
    {
        path: '/question/manage',
        name: '管理题目',
        component: ManageQuestionView,
        meta: {
            access: ACCESS_ENUM.ADMIN,
        }
    },
    {
        path: '/questions',
        name: '浏览题目',
        component: QuestionsView,
    },
    {
        path: '/question_submit',
        name: '提交列表',
        component: QuestionSubmitView,
        meta: {
            access: ACCESS_ENUM.USER,
        }
    },
    {
        path: '/question_submit/my',
        name: '我的提交列表',
        component: QuestionSubmitMyView,
        meta: {
            access: ACCESS_ENUM.USER,
        }
    },
    {
        path: '/question/update',
        name: '更新题目',
        component: AddQuestionView,
        meta: {
            hideInMenu: true
        }
    },
    {
        path: '/question/answer/:id',
        name: '在线做题',
        component: AnswerQuestionView,
        props: true,
        meta: {
            hideInMenu: true,
            access: ACCESS_ENUM.USER
        }
    },
    {
        path: '/question/user/setting',
        name: '用户设置',
        component: UserSettingView,
        meta: {
            access: ACCESS_ENUM.USER,
            hideInMenu: true
        }
    },
    {
        path: '/question/user/center',
        name: '用户中心',
        component: UserCenterView,
        meta: {
            hideInMenu: true
        }
    },
    {
        path: '/question/test/list',
        name: '试卷信息',
        component: TestView,
        meta: {
            access: ACCESS_ENUM.USER,
        }
    },
    {
        path: '/collect/test/do/:id',
        name: '试题汇总',
        component: TestDoView,
        meta: {
            hideInMenu: true
        }
    },
    {
        path: '/noAuth',
        name: '无权限',
        component: NoAuthView,
        meta: {
            hideInMenu: true
        }
    },
]