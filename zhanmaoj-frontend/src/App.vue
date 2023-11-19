<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<style>

</style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import {useRouter} from "vue-router";
import {useStore} from "vuex";
import ACCESS_ENUM from "@/access/accessEnum";
import {onMounted} from "vue";

const router = useRouter();
const store = useStore();

/**
 * 全局初始化函数，有全局单次调用的代码
 */
const doInit = () => {
  console.log('事已至此，那就吃饭吧 0.o')
}

onMounted(() => {
  doInit();
})

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
</script>