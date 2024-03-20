<template>
  <div class="list-wrap">
    <a-row class="list-row" :gutter="24">
      <a-col
        v-for="item in testList"
        :key="item.id"
        :xs="12"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="6"
        :xxl="6"
        class="list-col"
        style="min-height: 162px"
      >
        <CardWrap
          loading=true
          :title="item.title"
          :content="item.content"
          :icon="item.icon"
          :beginTime="item.beginTime"
          :expiredTime="item.expiredTime"
          :status="item.status"
          :id="item.id"
          :expire="item.expiredTime < nowTime"
        >
          <template #skeleton>
            <a-skeleton :animation="true">
              <a-skeleton-line :widths="['100%', '40%', '100%']" :rows="3" />
              <a-skeleton-line :widths="['40%']" :rows="1" />
            </a-skeleton>
          </template>
        </CardWrap>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import CardWrap from './card-wrap.vue';
  import {onMounted, ref} from "vue";
  import {
    TestControllerService,
  } from "../../generated";
  import message from "@arco-design/web-vue/es/message";

  const testList = ref([]);

  const nowTime=new Date().getTime();

  const loading = ref(false);
  const loadData = async () => {
    const res = await TestControllerService.listTestSimpleUsingPost();
    if (res.code === 0) {
      testList.value = res.data;
      loading.value = true;
    } else {
      message.error("加载失败，" + res.message);
    }
  };

  /**
   * 页面加载时，请求数据
   */
  onMounted(() => {
    loadData();
  });


</script>

<style scoped lang="less"></style>
