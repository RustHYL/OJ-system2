<template>
  <div id="questionsView">
    <div class="header">
      <a-breadcrumb>
        <a-breadcrumb-item>题目</a-breadcrumb-item>
        <a-breadcrumb-item>我的错题</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <div class="container">
      <a-form :model="searchParams" layout="inline">
        <a-form-item field="title" label="名称" style="min-width: 240px">
          <a-input v-model="searchParams.title" />
        </a-form-item>
        <a-form-item field="tags" label="标签" style="min-width: 240px">
          <a-input-tag v-model="searchParams.tags" />
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
        <template #optional="{ record }">
          <a-space>
            <a-button type="primary" @click="toQuestionPage(record)"> 做题</a-button>
          </a-space>
        </template>
        <template #tags="{ record }">
          <a-space wrap>
            <a-tag v-for="(tag, index) of record.tags" :key="index" color="green">{{ tag }}</a-tag>
          </a-space>
        </template>
        <template #judgeInfo="{ record }">
          {{`信息:${record.judgeInfo.message}\n内存消耗:${record.judgeInfo.memory}\n耗时:${record.judgeInfo.time}`}}
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
  QuestionWrongControllerService, QuestionWrongQueryRequest, QuestionWrongVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import moment from "moment";
import {useStore} from "vuex";

const tableRef = ref();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref<QuestionWrongQueryRequest>({
  title: "",
  tags:[],
  pageSize: 8,
  current: 1,
});

const loadData = async () => {
  const res = await QuestionWrongControllerService.listMyQuestionWrongVoByPageUsingPost(
      searchParams.value
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
    title: "题目名称",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "答案",
    dataIndex: "code",
    ellipsis: true,
  },
  {
    title: "判题信息",
    slotName: "judgeInfo",
    width: 170,
  },
  {
    title: "错题时间",
    slotName: "createTime",
  },
  {
    slotName: "optional",
  },
];

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const store = useStore()
const router = useRouter();

const toQuestionPage = (questionWrongVO: QuestionWrongVO) => {
  store.state.questionWrong = questionWrongVO;
  router.push({
    path: `/question/answer/${questionWrongVO.questionId}`,
    // query: {
    //   code: questionWrongVO.code,
    // },
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
#questionsView {
  width: 100%;
  margin: 0 auto;
}

#questionsView .container{
  width: 60%;
  margin: 0 auto;
}

</style>
