<template>
  <div class="container" >
    <a-row style="margin-bottom: 16px">
      <a-col :span="24">
        <UserPanel />
      </a-col>
    </a-row>
    <a-row class="wrapper">
      <a-col :span="24">
        <a-tabs default-active-key="1" type="rounded">
          <a-tab-pane key="1" title="基本信息">
            <BasicInformation />
          </a-tab-pane>
          <a-tab-pane key="2" title="安全设置">
            <SecuritySettings @styleStatus="handleStyleStatus"
                              :show-overlay="showOverlay"/>
          </a-tab-pane>
        </a-tabs>
      </a-col>
    </a-row>
    <div class="overlay" v-if="showOverlay" @click="changeShowOverlay"></div>
  </div>
</template>

<script lang="ts" setup>
  import UserPanel from '../../components/UserPanel.vue';
  import BasicInformation from '../../components/BasicInformation.vue';
  import SecuritySettings from "@/components/SecuritySettings.vue";
  import {ref} from "vue";

  const showOverlay = ref(false);

  const handleStyleStatus = (status: boolean) => {
    showOverlay.value = status
  }

  const changeShowOverlay = () => {
    showOverlay.value = false;
  }

</script>

<script lang="ts">
  export default {
    name: 'Setting',
  };


</script>

<style scoped lang="less">
  .container {
    width: 100%;
    height: 100%;
    position: relative;
    padding: 0 20px 20px 20px;
  }

  .wrapper {
    padding: 20px 0 0 20px;
    min-height: 580px;
    background-color: var(--color-bg-2);
    border-radius: 4px;
  }

  :deep(.section-title) {
    margin-top: 0;
    margin-bottom: 16px;
    font-size: 14px;
  }

  .overlay {
    position: fixed; /* 遮罩层固定在视口上 */
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5); /* 灰色半透明背景 */
    z-index: 998; /* 确保遮罩层在最顶层 */
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer; /* 使遮罩层可点击 */
  }
</style>
