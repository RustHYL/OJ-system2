<!--todo 每间隔一定时间刷新提交状态-->
<template>
  <div id="questionSubmitView">
    <div class="header">
      <a-breadcrumb>
        <a-breadcrumb-item>题目</a-breadcrumb-item>
        <a-breadcrumb-item>提交列表</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <div class="container">
      <a-form :model="searchParams" layout="inline">
        <!--      todo 题目id可以作为跳转 插槽实现-->
        <a-form-item field="questionId" label="题号" style="min-width: 240px">
          <a-input v-model="searchParams.questionId" />
        </a-form-item>
        <a-form-item
            field="language"
            label="编程语言"
            style="min-width: 240px"
        >
          <a-select
              v-model="searchParams.language"
              :style="{ width: '320px' }"
              placeholder="选择编程语言"
          >
            <a-option>java</a-option>
            <a-option>cpp</a-option>
            <a-option>go</a-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="onSearch">搜索</a-button>
        </a-form-item>
      </a-form>
      <a-table
          :ref="tableRef"
          :columns="columns"
          :data="dataList"
          :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
          @page-change="onPageChange"
      >
        <template #judgeInfo="{ record }">
          {{JSON.stringify(record.judgeInfo)}}
        </template>
        <template #createTime="{ record }">
          {{moment(record.createTime).format("YYYY-MM-DD")}}
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
  QuestionSubmitQueryRequest,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";

const tableRef = ref();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionSubmitQueryRequest>({
  questionId: undefined,
  language: undefined,
  pageSize: 8,
  current: 1,
});

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionSubmitByPageUsingPost(
      {
        ...searchParams.value,
        sortField: "createTime",
        sortOrder: "descend"}
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("加载失败，" + res.message);
  }
};

/**
 * 监听 searchParams 变量，改变时触发页面的重新加载
 */
watchEffect(() => {
  loadData();
});

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const columns = [
  {
    title: "提交编号",
    dataIndex: "id",
  },
  {
    title: "编程语言",
    dataIndex: "language",
  },
  {
    title: "判题信息",
    slotName: "judgeInfo",
  },
    // todo 状态切换为文字
  {
    title: "判题状态",
    dataIndex: "status",
  },
  {
    title: "题目编号",
    dataIndex: "questionId",
  },
  {
    title: "提交用户",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
];

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};


const router = useRouter();

const toQuestionPage = (question: Question) => {
  router.push({
    path: `/question/answer/${question.id}`,
  });
};

const onSearch = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
}
</script>

<style scoped>
#questionSubmitView {
  width: 100%;
  margin: 0 auto;
}

#questionSubmitView .container{
  width: 80%;
  margin: 0 auto;
}

</style>
