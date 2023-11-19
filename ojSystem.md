# 1-实现方案和项目初始化

## 项目介绍

OJ = Online Judge 在线判题测评系统

用户可以选择题目，在线做题，编写代码并且提交代码；系统会对用户提交的代码，根据我们出题人设置的答案来判断用户的提交结果是否正确。

ACM（程序设计竞赛）：需要依赖判题系统

**难点**：判题系统

用于再按评测变成题目代码的系统，能够更具用户提交的代码、出题人预设的题目输入和输出用例，进行编译代码、运行代码、判断代码h运行及如果是否正确。

## OJ系统的常用概念

ac表示你的题目通过，结果正确

题目限制：时间限制、内存限制

题目介绍

题目输入

题目输出

题目输入用例

题目输出用例



普通测评：管理员设置题目的输入和输出用例，交给判题机去执行用户给的代码，给用户的代码喂输入用例，看用户程序的执行结果是否和标准答案输出一致（比对用例文件）



特殊测评（SP）：管理员上设置题目的输入和输出，，比如我输入1，用户答案只要是>2或者<0都是正确；特判程序，不是通过比对用例文件是否一致，而是根据这道题目写一个特殊的判题程序，程序接收题目的输入、标准输出用例、用户结果，特判程序根据这些值来比较是否正确。

交互测评：让用户输入一个例子，就给一个输出结果，比较灵活，没法用死板的输入输出文件来解决

---

## 项目流程

1.项目介绍、项目调研、需求分析

2.核心业务流程

3.项目要做的功能（功能模块）

4.技术选型（技术预研）

5.项目初始化

6.项目开发

7.测试

8.优化

9.代码提交、代码审核

10.产品验收

11.上线



## 现有系统

https://github.com/HimitZH/HOJ（可以学习）

https://github.com/QingdaoU/OnlineJudge（python，很成熟）

https://github.com/hzxie/voj（好学，不成熟）

https://github.com/zhblue/hustoj（成熟，但是php实现）

https://github.com/hydro-dev/Hydro（node.js实现，功能很多）



## 实现核心

1）权限校验

2）**代码沙箱（安全沙箱）**

用户代码藏毒：用户写了木马文件、修改系统的权限



沙箱：隔离的、安全的环境、用户的代码不会影响到沙箱之外的系统的运行



资源分配：限制用户程序占用的资源



3）判题规则

题目用例的比对，结果的验证



4）任务调度

服务器资源有限，用户要排队，按照顺序依次执行判题，而不是直接拒绝



## 核心业务流程

![image-20231120025103927](C:\Users\Alan\AppData\Roaming\Typora\typora-user-images\image-20231120025103927.png)

> 编译的原因：有些语言不编译无法运行

![image-20231120025208000](C:\Users\Alan\AppData\Roaming\Typora\typora-user-images\image-20231120025208000.png)

判题服务：获取题目信息，预计的输入输出结果，返回给主业务后端：用户的答案是否正确

代码沙箱：只负责运行代码，给出结果，不管什么结果都是正确的。



## 功能

1.题目模块

​	a.创建题目（管理员）

​	b.删除题目（管理员）

​	c.修改题目（管理员）

​	d.搜索题目（用户）

​	e.在线做题

​	f.提交题目代码

2.用户模块

​	a.注册

​	b.登录

3.判题模块

​	a.提交判题（结果是否正确与错误）

​	b.错误处理（内存溢出、安全性、超时）

​	c.**自主实现** 代码沙箱（安全沙箱）

​	d.开放接口（提供一个独立的新服务）



## 项目扩展思路

1.支持多种语言

2.Remote Judge

3.完善的评测功能：普通评测、特殊评测、交互评测、在线自测、子任务分组测评、文件IO

4.统计分析用户判题记录

5.权限校验



## 技术选型

前端：Vue3、Arco Design 组件库、在线代码编辑器、在线文档浏览

Java进程控制，Java安全管理器，JVM知识

虚拟机（云服务器）、Docker（代码沙箱实现）

Spring Cloud 微服务、消息队列、设计模式



## 架构设计

![image-20231120030410496](C:\Users\Alan\AppData\Roaming\Typora\typora-user-images\image-20231120030410496.png)



## 主流OJ系统实现方案

### 1.用现有的OJ系统

网上很多开源的OJ项目：judge0（60多种编程语言）

https://github.com/judge0/judge0

### 2.用现成的服务

可以用现成的判题API、或者现成的代码沙箱等服务。

比如judge0提供的判题API。通过HTTP调用submissions判题接口，把用户的代码、输入值、预期的执行结果作为请求参数发送给judge0的服务器，能自动帮你编译执行程序，并且返回程序的运行结果。

> API的作用：接收代码、返回执行结果

Judge0 API地址：https://rapidapi.com/judge0-official/api/judge0-ce

官方文档：https://ce.judge0.com/#submissions-submission-post

#### 流程

1.先注册

2.再开通订阅

3.然后测试 language 接口

4.测试执行代码接口submissions



### 3.自主开发



### 3.把AI当作代码沙箱

chatGPT、chatGLM、文心一言...

### 5.搞事的方法

那就是可以通过让程序来操作模拟浏览器的方式，用别人已经开发好的OJ系统来帮咱们判题。

比如使用Puppeteer +无头浏览器，把咱们系统用户提交的代码，像人一样输入到别人的OJ网页中，让程序点击提交按钮，并且等别人的OJ系统返回判题结果后，再把这个结果返回给我们自己的用户。

这种方式的缺点就是把核心流程交给了别人，如果别人服务挂了，你的服务也就挂了;而且别人ОJ系统不支持的题目，可能你也支持不了。



## 前端项目初始化（通用模板）

> 可以做一套通用前端模板

### 初始化

使用vue-cli 脚手架：https://cli.vuejs.org/zh/

创建项目：

```shell
vue create xxx
```

### 组件引入

组件库：https://arco.design/vue/docs/start

执行安装：

```shell
# npm
npm install --save-dev @arco-design/web-vue
# yarn
yarn add --dev @arco-design/web-vue
```

引入（改变mian.ts）

```typescript
import { createApp } from 'vue'
import ArcoVue from '@arco-design/web-vue';
import App from './App.vue';
import '@arco-design/web-vue/dist/arco.css';

const app = createApp(App);
app.use(ArcoVue);
app.mount('#app');
```

### 项目通用布局

新建布局，在app.vue中引入

```vue
<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>
```

选用arco design 的layout组件（https://arco.design/vue/component/layout）



#### 实现路由菜单

菜单组件：https://arco.design/vue/component/menu

目标：根据路由配置信息，自动生成菜单内容。实现更通用、更自动的菜单配置



步骤：

1）提取通用路由文件

2）菜单绑定读取路由，动态渲染菜单项

3）绑定跳转事件

4）同步路由的更新到菜单项高亮

同步高亮原理：首先点击菜单项 =》 触发点击时间，跳转更新路由 =》 更新路由后，同步更新菜单栏的高亮状态。



使用Vue Router的afterEach 路由钩子实现：

```javascript
const router = useRouter();
//默认主页
const selectedKeys = ref(['/']);
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
})
```



### 全局状态管理

> 所有页面全局共享的变量，而不是局限在某一个页面中。

vuex：https://vuex.vuejs.org/zh/guide/

适合作为全局状态的数据：已登录用户信息（每个页面几乎都需要用）

Vuex 的本质：给你提供了一套增删改查全局变量的API，加上一些功能

![image-20231120033755208](C:\Users\Alan\AppData\Roaming\Typora\typora-user-images\image-20231120033755208.png)

可以参考购物车示例：https://github.com/vuejs/vuex/tree/main/examples/classic/shopping-cart

state:存储的状态信息，比如用户信息

mutation(尽量同步)︰定义了对变量进行增删改(更新)的方法

actions (支持异步)︰执行异步操作，并且触发mutation的更改(actions调用mutation)

modules(模块)︰把一个大的state(全局变量)划分为多个小模块，比如user专门存用户的状态信息

#### 实现

在store下定义user模块，存储用户信息

```typescript
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
```

然后再 store 目录下定义index.ts文件，导入 user 模块：

```typescript
import { createStore } from 'vuex'
import user from './user'
export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user,
  }
})
```

Vue 页面中可以获取已经存储的状态变量：

```tsx
const store = useStore();
store.state.user?.loginUser
```

在 Vue 页面中可以修改状态变量

> 使用 dipatch 来调用之前定义好的actions

```typescript
store.dispatch("user/getLoginUser", {
  username: "管理员",
  userRole: ACCESS_ENUM.ADMIN,
});
```



### 全局权限管理

> 通用的机制去定义页面需要的权限，不是每个页面独立判断权限，提高效率

思路：

1．在路由配置文件，定义某个路由的访问权限

2.在全局页面组件 app.vue 中，绑定一个全局路由监听。每次访问页面时，根据用户要访问页面的路由信息，先判断用户是否有对应的访问权限。

3．如果有，跳转到原页面;如果没有，拦截或跳转到401鉴权或登录页

```typescript
const router = useRouter();
const store = useStore();

router.beforeEach((to, from, next) =>{
  //仅管理员可见，判定当前用户权限
  if (to.meta.access === ACCESS_ENUM.ADMIN){
    if (store.state.user?.loginUser?.userRole !== ACCESS_ENUM.ADMIN){
      next('/noAuth');
      return;
    }
  }
  next();
})
```



### 通过导航栏组件-根据配置控制菜单的显影

1）routes.ts 给路由新增一个标志位，用于判断路由是否显隐

```typescript
{
    path: '/hide',
    name: '隐藏页面',
    component: HomeView,
    meta: {
        hideInMenu: true,
    },
},
```

2）不要用v-for + v-if去条件渲染元素，这样会先循环所有的元素，导致性能的浪费（v-for和v-if都存在的时候优先v-for）

推荐：先过滤只需要展示的元素数组

```typescript
const visibleRoutes = routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
	return true;
})
```



### 根据权限隐藏菜单

需求： 只有具有权限的菜单，才对用户可见

原理：类似上面控制路由显隐，只要判断用户有没有这个权限，没有就过滤



1）新建 access 目录，专门用一个文件来定义权限

```typescript
/**
 * 权限定义
 */

const ACCESS_ENUM = {
    NOT_LOGIN: 'notLogin',
    USER: 'user',
    ADMIN: 'admin',
}

export default ACCESS_ENUM
```

2）定义一个公用的权限校验方法（菜单组件需要使用，权限拦截也要使用）

创建checkAccess.ts文件，专门定义检测权限的函数：

```typescript
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
}

export default checkAccess
```

3）修改菜单组件，根据权限来过滤菜单

注意：使用计算属性，便于当登录用户信息发生变更的时候，触发菜单栏的重新渲染，展示新增或减少的权限菜单

```typescript
const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单(不要将const store.state.user.loginUser，会导致loginUser不变)
    return checkAccess(store.state.user.loginUser, item?.meta?.access as string);
  });
});
```



### 全局项目入口

app.vue 中预留一个可以编写全局初始化逻辑的代码：

```typescript
const doInit = () => {
  console.log('事已至此，那就吃饭吧 0.o')
}

onMounted(() => {
  doInit();
})
```

## 后端项目初始化

