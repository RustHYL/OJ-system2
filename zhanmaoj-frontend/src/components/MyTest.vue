<template>
  <a-card
    class="general-card"
    title="我的试卷"
    :header-style="{ paddingBottom: '18px' }"
    :body-style="{ paddingBottom: '12px' }"
  >
    <a-list :bordered="false">
      <a-list-item
        v-for="test in testList"
        :key="test.id"
        action-layout="horizontal"
      >
          <a-row :gutter="6">
            <a-col :span="6">
              <a-skeleton-shape shape="circle" />
            </a-col>
            <a-col :span="16">
              <a-skeleton-line :widths="['100%', '40%']" :rows="2" />
            </a-col>
          </a-row>
        <a-list-item-meta :title="test.title">
          <template #description> 共{{ test.questionNum }}题 </template>
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </a-card>
</template>

<script lang="ts" setup>
  import {TestControllerService} from "../../generated";
  import message from "@arco-design/web-vue/es/message";
  import {ref, onMounted} from "vue";

  const testList: any = ref([]);
  const loadData = async () => {
    const res = await TestControllerService.listTestMineUsingPost();
    if (res.code === 0) {
      testList.value = res.data;
    } else {
      message.error("加载失败，" + res.message);
    }
  };

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
