<template>
  <div id="manageQuestionView">
    <div class="header">
      <a-breadcrumb>
        <a-breadcrumb-item>题目</a-breadcrumb-item>
        <a-breadcrumb-item>题目管理</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <div class="container">
      <a-table
          :ref="tableRef"
          :columns="columns"
          :data="dataList"
          :scroll="scroll"
          :expandable="expandable"
          :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
          @page-change="onPageChange"
      >
        <template #tags="{ record }">
          <a-space wrap>
            <a-tag v-for="(tag, index) in record.tags" :key="index" color="green">{{ tag }}</a-tag>
          </a-space>
        </template>
        <template #optional="{ record }">
          <a-space>
            <a-button type="primary" @click="doUpdate(record)"> 修改</a-button>
            <a-button status="danger" @click="doDelete(record)">删除</a-button>
          </a-space>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref, watchEffect} from "vue";
import {Question, QuestionControllerService,} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import {useRouter} from "vue-router";

const tableRef = ref();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref({
  pageSize: 10,
  current: 1,
});

const scroll = {
  x: 1850,
  y: 450
}

const expandable = reactive({
  title: '更多',
  width: 80,
  expandedRowRender: (record) => {
    return `判题规则（ 时间限制:${record.judgeConfig.timeLimit}    内存限制:${record.judgeConfig.memoryLimit}    时间限制:${record.judgeConfig.stackLimit}）`
  }
});

const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
      searchParams.value
  );
  if (res.code === 0) {
    // 将解析后的记录数组赋值给 dataList.value
    dataList.value = res.data.records.map((record: { tags: string; judgeConfig: string; }) => {
      const tags = JSON.parse(record.tags);
      const judgeConfig = JSON.parse(record.judgeConfig);
      return {...record, tags, judgeConfig};
    });
    total.value = res.data.total;
  }else {
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
    title: "id",
    dataIndex: "id",
    ellipsis: true,
    tooltip: true,
    fixed: 'left',
  },
  {
    title: "标题",
    dataIndex: "title",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "内容",
    dataIndex: "content",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "标签",
    dataIndex: "tags",
    slotName: "tags",
  },
  {
    title: "答案",
    dataIndex: "answer",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "提交数",
    dataIndex: "submitNum",
    width: 80,
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
    width: 80,
  },
  {
    title: "判题用例",
    dataIndex: "judgeCase",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "用户id",
    dataIndex: "userId",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
    fixed: 'right',
  },
];

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const doDelete = async (question: Question) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: question.id,
  });
  if (res.code === 0) {
    message.success("删除成功");
    await loadData();
  } else {
    message.error("删除失败");
  }
};

const router = useRouter();

const doUpdate = (question: Question) => {
  router.push({
    path: "/question/update",
    query: {
      id: question.id,
    },
  });
};
</script>

<style scoped>
#manageQuestionView {
  width: 100%;
  margin: 0 auto;
}

#manageQuestionView .container{
  width: 80%;
  margin: 0 auto;
}
</style>
