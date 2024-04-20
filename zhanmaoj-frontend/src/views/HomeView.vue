<template>
  <div class="container">
    <div class="top">
      <a-carousel
          :autoPlay="true"
          animation-name="card"
          show-arrow="never"
          indicator-position="outer"
          :style="{
      width: '100%',
      height: '300px',
    }"
      >
        <a-carousel-item v-for="image in images" :style="{ width: '60%' }">
          <img
              :src="image"
              :style="{
          width: '100%',
        }"
          />
        </a-carousel-item>
      </a-carousel>
    </div>
    <div class="questions">
<!--      <a-divider orientation="left">题目</a-divider>-->
      <QuestionsShow />
    </div>
    <div class="test">
      <TestsShow />
    </div>
  </div>



</template>

<script setup lang="ts">

import QuestionsShow from "@/components/QuestionsShow.vue";
import TestsShow from "@/components/TestsShow.vue";

const images = [
  'http://unity.haoracle.com/content/uploads/2018/09/17/1537154109126595.jpg',
  'https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/2/19/1705c89cbd6e6388~tplv-t2oaga2asx-jj-mark:3024:0:0:0:q75.png',
  'https://assets.bdqn.cn/upload/image/20200316/1584341566685395.jpg',
  'https://img.qinxue365.com/uploads/allimg/1801/4086-1P124102023301.jpg',
];

</script>

<style scoped>
  .container {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .top {
    width: 100%;
    height: 300px;
  }

  .questions {
    width: 100%;
    height: 300px;
    margin-top: 30px;
  }
  .test {
    width: 100%;
    height: 300px;
    margin-top: 40px;
    margin-bottom: 30px;
  }
</style>
