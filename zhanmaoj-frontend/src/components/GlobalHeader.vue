<template>
  <a-row id="globalHeader" align="center">
    <a-col flex="auto">
      <a-menu mode="horizontal" :selected-keys="selectedKeys" @menu-item-click="doMenuClick">
        <a-menu-item key="0" :style="{ padding: 0, marginRight: '38px' }" disabled>
          <div class="title-bar">
            <img class="logo" src="../assets/oj-logo.jpg" alt="OJSystem"/>
            <div class="title">战码OJ</div>
          </div>
        </a-menu-item>
        <a-menu-item v-for="item in visibleRoutes" :key="item.path">
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </a-col>
    <a-col flex="100px">
        <a-dropdown trigger="click">
          <a-avatar
              :size="40"
              :style="{ marginRight: '8px', cursor: 'pointer' }"
          >
            <img alt="avatar" :src="store.state.user?.loginUser?.userAvatar || defaultUserAvatar" />
          </a-avatar>
          <template #content>
            <a-doption v-if="store.state.user?.loginUser.id">
              <a-space @click="$router.push('/question/user/center')">
                <icon-user />
                <span>
                  用户中心
                </span>
              </a-space>
            </a-doption>
            <a-doption v-if="store.state.user?.loginUser.id">
              <a-space @click="$router.push('/question/user/setting')">
                <icon-settings />
                <span>
                  设置中心
                </span>
              </a-space>
            </a-doption>
            <a-doption v-if="store.state.user?.loginUser.id">
              <a-space @click="handleLogout">
                <icon-export />
                <span>
                  退出登陆
                </span>
              </a-space>
            </a-doption>
            <a-doption v-if="!store.state.user?.loginUser.id">
              <a-space @click="handleLogin">
                <icon-export />
                <span>
                  登陆
                </span>
              </a-space>
            </a-doption>
          </template>
        </a-dropdown>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
import {routes} from "@/router/routes"
import {useRouter} from "vue-router";
import {computed, ref} from "vue";
import {useStore} from "vuex";
import checkAccess from "@/access/checkAccess";
import {UserControllerService} from "../../generated";
import message from "@arco-design/web-vue/es/message";

const router = useRouter();
const selectedKeys = ref(['/']);
const store = useStore();
const defaultUserAvatar = require('@/assets/notLogin.svg');

const visibleRoutes = computed(() => {
  return routes.filter((item, index) => {
    if (item.meta?.hideInMenu) {
      return false;
    }
    // 根据权限过滤菜单(不要将const store.state.user.loginUser，会导致loginUser不变)
    return checkAccess(store.state.user.loginUser, item?.meta?.access as string);
  });
});



router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
})

const doMenuClick = (key: string) =>{
  router.push({
    path: key
  });
}

const handleLogout = async () => {
  const res = await UserControllerService.userLogoutUsingPost();
  if (res.code === 0) {
    message.success("退出成功");
    await router.push('/user/login');
  } else {
    message.error("退出失败:" + res.message);
  }
};

const handleLogin = async () => {
  await router.push('/user/login');
};

</script>

<style scoped>
#globalHeader {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 64px;
  padding: 0 24px;
  color: #444;
}

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
