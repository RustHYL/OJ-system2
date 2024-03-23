<template>
  <a-card class="general-card" title="我的错题">
    <template #extra>
      <a-link>显示更多</a-link>
    </template>
    <a-row :gutter="16">
      <a-col
        v-for="(question, index) in dataList"
        :key="index"
        :xs="12"
        :sm="12"
        :md="12"
        :lg="12"
        :xl="8"
        :xxl="8"
        class="my-project-item"
      >
        <a-card size="small">
          <a-space direction="vertical">
            <a-typography-text bold>{{ question.id }}</a-typography-text>
            <a-typography-text type="secondary">
              {{ question.title }}
            </a-typography-text>
            <a-space>
              <a-tag v-for="(tag, index) of question.tags" :key="index" color="orange">{{ tag }}</a-tag>
            </a-space>
          </a-space>
        </a-card>
      </a-col>
    </a-row>
  </a-card>
</template>

<script lang="ts" setup>
import {QuestionControllerService} from "../../generated";
import message from "@arco-design/web-vue/es/message";
import {onMounted, ref} from "vue";

const dataList = ref([]);
  const total = ref(0);
  const searchParams = ref({
    pageSize: 10,
    current: 1,
  });



const loadData = async () => {
    const res = await QuestionControllerService.getMyErrorQuestionUsingPost();
    if (res.code === 0) {
      dataList.value = res.data;
      total.value = res.data.total;
    } else {
      message.error("加载失败，" + res.message);
    }
  };

  onMounted(() => {
    loadData();
  });


</script>

<style scoped lang="less">
  :deep(.arco-card-body) {
    min-height: 100px;
    padding-bottom: 0;
  }
  .my-project {
    &-header {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
    }

    &-title {
      margin-top: 0 !important;
      margin-bottom: 18px !important;
    }

    &-list {
      display: flex;
      justify-content: space-between;
    }

    &-item {
      // padding-right: 16px;
      margin-bottom: 16px;

      &:last-child {
        padding-right: 0;
      }
    }
  }
</style>
