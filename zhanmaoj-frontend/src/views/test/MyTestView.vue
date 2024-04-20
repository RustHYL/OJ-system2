<template>
  <div id="myTestView">
    <div class="header">
      <a-breadcrumb>
        <a-breadcrumb-item>试卷</a-breadcrumb-item>
        <a-breadcrumb-item>我的试卷</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <div class="container">
      <a-form :model="searchParams" layout="inline">
        <a-form-item field="title" label="名称" style="min-width: 240px">
          <a-input v-model="searchParams.title" />
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
        <template #beginTime="{ record }">
          {{moment(record.beginTime).format("YYYY-MM-DD")}}
        </template>
        <template #expiredTime="{ record }">
          {{moment(record.expiredTime).format("YYYY-MM-DD")}}
        </template>
      </a-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref, watchEffect} from "vue";
import {TestControllerService,} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import {useRouter} from "vue-router";
import moment from "moment/moment";

const tableRef = ref();

const dataList = ref([]);
const total = ref(0);
const searchParams = ref({
  pageSize: 10,
  current: 1,
  title: "",
});

const scroll = {
  x: 1850,
  y: 450
}

const onSearch = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
}

const loadData = async () => {
  const res = await TestControllerService.listTestVoMinePageUsingPost(
      searchParams.value
  );
  if (res.code === 0) {
    // 将数组赋值给 dataList.value
    dataList.value = res.data.records;
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
    width: 200
  },
  {
    title: "题目数量",
    dataIndex: "questionNum",
    ellipsis: true,
    tooltip: true,
    width: 100
  },
  {
    title: "开始时间",
    slotName: "beginTime",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "过期时间",
    slotName: "expiredTime",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "总分",
    dataIndex: "totalScore",
    ellipsis: true,
    tooltip: true,
    width: 80,
  },
  {
    title: "得分",
    dataIndex: "score",
    ellipsis: true,
    tooltip: true,
  },
];

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

const router = useRouter();

</script>


<style scoped>
#myTestView {
  width: 100%;
  margin: 0 auto;
}

#myTestView .container{
  width: 60%;
  margin: 0 auto;
}

</style>
