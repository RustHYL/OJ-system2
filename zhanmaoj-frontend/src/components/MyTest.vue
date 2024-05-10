<template>
  <a-card
    class="general-card"
    title="我的做卷记录"
    :header-style="{ paddingBottom: '18px' }"
    :body-style="{ paddingBottom: '12px' }"
  >
    <template #extra>
      <router-link to="/question/test/list/my/page">显示更多</router-link>
    </template>
    <a-list :bordered="false">
      <a-list-item
        v-for="test in testVOList"
        :key="test.id"
        action-layout="horizontal"
      >
        <a-list-item-meta :title="test.title" @click="handleClick(test)" style="cursor: pointer">
          <template #description> 共{{ test.questionNum }}题    得分:{{test.score}}</template>
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </a-card>
</template>

<script lang="ts" setup>
  import {TestControllerService} from "../../generated";
  import message from "@arco-design/web-vue/es/message";
  import {ref, onMounted} from "vue";
  import {useRouter} from "vue-router";

  const router = useRouter();

  const testVOList: any = ref([]);
  const loadData = async () => {
    const res = await TestControllerService.listTestVoMineUsingPost();
    if (res.code === 0) {
      testVOList.value = res.data;
    } else {
      message.error("加载失败，" + res.message);
    }
  };

  const handleClick = (test: any) => {
    router.push({
      path: `/collect/test/wrong/do/${test.id}`, // 使用模板字符串来插入参数
    })
  }

  onMounted(() => {
    loadData();
  });
</script>

<style scoped lang="less">
  .general-card {
    height: 356px;
    .arco-list-item {
      height: 72px;
      padding-left: 0;
      padding-bottom: 12px;
      border-bottom: 1px solid var(--color-neutral-3);
      &:last-child {
        border-bottom: none;
      }
      .arco-list-item-meta {
        padding: 0;
      }
    }
  }
</style>
