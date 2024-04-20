<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {TestControllerService, TestSubmitFinalDetailVO} from "../../../generated";
import message from "@arco-design/web-vue/es/message";

  const route = useRoute();

  const router = useRouter();

  const form = ref({});

  const isTransition = ref(false);
  const score = ref(0);
  const beginTime = ref();
  const endTime = ref();

  const transition = () => {
    isTransition.value = true;
    setTimeout(() => {
      isTransition.value = false;
    }, 1000);
  }

  onMounted(() => {
    loadData();
  });

  const doConfirm = () => {
    router.push({path: `/question/test/list`})
  }

  const finalDetail = ref<TestSubmitFinalDetailVO>();
const loadData = async () => {
    const res = await TestControllerService.getFinalDetailUsingGet(
        route.params.id as any,
    );
    console.log("finalDetail:" + JSON.stringify(res.data))
    if (res.code === 0) {
      finalDetail.value = res.data;
      score.value = res.data?.score;
      beginTime.value = formatDate(res.data?.beginTime);
      endTime.value = formatDate(res.data?.endTime);
      transition();
    } else {
      message.error("加载失败，" + res.message);
    }
  };

function formatDate(isoString: string): string {
  const date = new Date(isoString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份是从0开始的，所以要加1
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
}


</script>

<template>
  <div class="score">
    <br />
    <div class="total">
      <div class="look">
        <i class="el-icon-a-061" style="font-size: 32px;"> 本次考试成绩 </i>
      </div>
      <div class="show">
        <div class="number" :class="{ border: isTransition }">
          <span> {{ score }} </span>
          <span>分数</span>
        </div>
      </div>
      <ul class="time">
        <li class="start">
          <span>开始时间</span> <span> {{ beginTime }} </span>
        </li>
        <li class="end">
          <span>结束时间</span> <span> {{endTime}} </span>
        </li>
      </ul>
      <div class="button">
        <a-button type="Dashed" @click="doConfirm">确认</a-button>
      </div>
    </div>
  </div>
</template>

<style lang="css" scoped>
.show {
  display: flex;
  justify-content: center;
  align-items: center;
  img {
    width: 160px;
    height: 160px;
  }
}
.time {
  padding: 0 70px;
  li {
    display: flex;
    justify-content: space-around;
    padding: 10px;
    margin: 20px 0px;
  }
  li:nth-child(1) {
    background-color: #fcf8e3;
  }
  li:nth-child(2) {
    background-color: #e9f5e9;
  }
}
.border {
  border: 6px solid #36aafd !important;
  transition: all 2s ease;
  width: 160px !important;
  height: 160px !important;
  transform: rotate(360deg) !important;
  opacity: 1 !important;
}
.button {
  display: flex;
  justify-content: center;
  padding-top: 30px;
}

.score {
  max-width: 800px;
  margin: 0 auto;
  .title {
    margin: 60px 0 30px 0;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    .name {
      font-size: 26px;
      color: inherit;
      font-weight: 500;
    }
    .description {
      font-size: 14px;
      color: #888;
    }
  }
  .total {
    border: 1px solid #dbdbdb;
    background-color: #fff;
    padding: 40px;
    .look {
      border-bottom: 1px solid #dbdbdb;
      padding: 0 0 14px 14px;
      color: #36aafd;
    }
    .number {
      opacity: 0;
      border: 6px solid #fff;
      transform: rotate(0deg);
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      width: 160px;
      height: 160px;
      border-radius: 50%;
      margin: 80px auto 20px;
      transition: all 1s ease;

      span:nth-child(1) {
        font-size: 36px;
        font-weight: 600;
      }
      span:nth-child(2) {
        font-size: 14px;
      }
    }
  }
}
</style>