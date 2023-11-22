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
      <div>{{ store.state.user?.loginUser?.userName ?? '未登录' }}</div>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import {routes} from "@/router/routes"
import {useRouter} from "vue-router";
import {computed, ref} from "vue";
import {useStore} from "vuex";
import checkAccess from "@/access/checkAccess";
import ACCESS_ENUM from "@/access/accessEnum";

const router = useRouter();
const selectedKeys = ref(['/']);
const store = useStore();


const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单(不要将const store.state.user.loginUser，会导致loginUser不变)
    return checkAccess(store.state.user.loginUser, item?.meta?.access as string);
  });
});



// setTimeout(() =>{
//   store.dispatch("user/getLoginUser", {
//     username: "管理员",
//     userRole: ACCESS_ENUM.ADMIN,
//   });
// }, 3000)

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
