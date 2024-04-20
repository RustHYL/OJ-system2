<template>
  <a-card class="general-card" title="考试">
    <template #extra>
      <router-link to="/question/test/list">显示更多</router-link>
    </template>
    <a-row :gutter="16">
      <a-col
        v-for="(test, index) in dataList"
        :key="index"
        :xs="12"
        :sm="12"
        :md="12"
        :lg="12"
        :xl="8"
        :xxl="8"
        class="my-project-item"
      >
        <a-card size="small" @click="handleToggle(test)">
          <a-space direction="vertical">
            <a-typography-text bold>{{ test.id }}</a-typography-text>
            <a-typography-text type="secondary">
              {{ test.title }}
            </a-typography-text>
            <a-space>
              <p>共{{test.questionNum}}&nbsp;&nbsp;&nbsp;&nbsp;题总分:{{test.totalScore}}</p>
<!--              <p>{{beginTime}}&#45;&#45;{{expiredTime}}</p>-->
            </a-space>
          </a-space>
        </a-card>
      </a-col>
    </a-row>
    <a-modal v-model:visible="showPasswordDialog" @ok="handleOk" @cancel="handleCancel">
      <template #title>
        填写密码
      </template>
      <a-input v-model="password" :style="{width:'320px'}" default-value="content" placeholder="请输入密码" allow-clear />
    </a-modal>
  </a-card>
</template>

<script lang="ts" setup>
import {Test, TestControllerService} from "../../generated";
import message from "@arco-design/web-vue/es/message";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const dataList = ref([]);
  const total = ref(0);
  const searchParams = ref({
    // pageSize: 10,
    // current: 1,
    num: 6,
  });

const loadData = async () => {
    const res = await TestControllerService.listTestUsingPost(searchParams.value);
    if (res.code === 0) {
      dataList.value = res.data;
      // beginTime.value = res.data.beginTime.format("YYYY-MM-DD");
      // expiredTime.value = res.data.expiredTime.format("YYYY-MM-DD");
      // total.value = res.data.total;
      console.log(JSON.stringify(res.data))
    } else {
      message.error("加载失败，" + res.message);
    }
  };

  onMounted(() => {
    loadData();
  });


const handleOk = async () => {
  if (!testChoose.value?.id) {
    return;
  }
  const res = await TestControllerService.joinTestUsingPost({
    testId: testChoose.value?.id,
    password: password.value
  })
  if (res.code === 0) {
    password.value = '';
    await router.push({
      path: `/collect/test/do/${testChoose.value?.id}`, // 使用模板字符串来插入参数
    })
  } else {
    message.error('加入失败' + (res.message ? `，${res.message}` : ''))
  }
};
const handleCancel = () => {
  password.value = '';
  showPasswordDialog.value = false;
}
const testChoose = ref<Test>();


const showPasswordDialog = ref(false);

const password = ref('');

const nowTime = ref(new Date());

const handleToggle = async (testNow: Test) => {
  console.log("开始时间:" + testNow.beginTime, "结束时间：" + testNow.expiredTime, JSON.stringify(testNow));
  testChoose.value = testNow;
  console.log("testChoose:" + JSON.stringify(testChoose.value))
  const beginTimeObj = new Date(testNow.beginTime);
  const expiredTimeObj = new Date(testNow.expiredTime);
  console.log("beginTimeObj:" + beginTimeObj, "expiredTimeObj:" + expiredTimeObj, "nowTime:" + nowTime.value);
  if (beginTimeObj > nowTime.value) {
    message.info('考试未开始');
  } else if (expiredTimeObj < nowTime.value) {
    message.info('考试已结束');
  } else if (testNow.status === 0) {
    const res = await TestControllerService.joinTestUsingPost({
      testId: testNow.id,
    })
    if (res.code === 0){
      await router.push({
        path: `/collect/test/do/${testNow.id}`, // 使用模板字符串来插入参数
      })
    }else {
      message.error('加入失败' + (res.message ? `，${res.message}` : ''))
    }

  } else if (testNow.status === 1) {
    //打开弹窗
    showPasswordDialog.value = true;
  }
}


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
