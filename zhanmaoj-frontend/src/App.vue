<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view/>
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<style>

</style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import {useRoute, useRouter} from "vue-router";
import {onMounted} from "vue";

const router = useRouter();
const route = useRoute();

/**
 * 全局初始化函数，有全局单次调用的代码
 */
const doInit = () => {
  console.log('事已至此，那就吃饭吧 0.o')
}

onMounted(() => {
  doInit();
})


</script>