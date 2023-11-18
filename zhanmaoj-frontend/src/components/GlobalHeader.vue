<template>
  <a-row id="globalHeader" align="center">
    <a-col flex="auto">
      <a-menu mode="horizontal" :selected-keys="selectedKeys" @menu-item-click="doMenuClick">
        <a-menu-item key="0" :style="{ padding: 0, marginRight: '38px' }" disabled>
          <div class="title-bar">
            <img class="logo" src="../assets/oj-logo.jpg"/>
            <div class="title">战码OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
      <div>{{ store.state.user?.loginUser?.username ?? '未登录' }}</div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import {routes} from "@/router/routes"
import {useRouter} from "vue-router";
import {ref} from "vue";
import {useStore} from "vuex";

const router = useRouter();
const selectedKeys = ref(['/']);
const store = useStore();
//展示的路由
const visibleRoutes = routes.filter((item, index) => {
  return !item.meta?.hideInMenu;
})

router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
})

const doMenuClick = (key: string) =>{
  router.push({
    path: key
  });
}
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}
.title {
  color: #444;
  margin-left: 16px;
}
.logo {
  width: 48px;
}

</style>
