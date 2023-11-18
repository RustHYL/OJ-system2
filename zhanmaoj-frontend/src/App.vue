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

const router = useRouter();
const store = useStore();

router.beforeEach((to, from, next) =>{
  //仅管理员可见，判定当前用户权限
  if (to.meta.access === 'canAdmin'){
    if (store.state.user?.loginUser?.role !== 'admin'){
      next('/noAuth');
      return;
    }
  }
  next();

})
</script>