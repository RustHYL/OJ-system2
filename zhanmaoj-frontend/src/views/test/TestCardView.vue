<template>
  <div id="testCardView">
    <div class="header">
      <a-breadcrumb>
        <a-breadcrumb-item>试卷</a-breadcrumb-item>
        <a-breadcrumb-item>试卷列表</a-breadcrumb-item>
      </a-breadcrumb>
    </div>
    <div class="container" style="width: 100%; height: 100%">
      <Breadcrumb :items="['menu.list', 'menu.list.cardList']" />
      <a-row :gutter="20" align="stretch">
        <a-col :span="24">
          <a-card class="general-card" title="试卷列表">
            <a-row justify="space-between">
              <a-col :span="24">
                <a-tabs :default-active-tab="1" type="rounded">
                  <a-tab-pane key="1" title="试卷">
                    <TheService />
                  </a-tab-pane>
                </a-tabs>
              </a-col>
            </a-row>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import TheService from '../../components/TheService.vue';
</script>

<script lang="ts">
  export default {
    name: 'Card',
  };
</script>

<style scoped lang="less">
#testCardView {
  width: 100%;
  height: 100%;
  .header {
    padding: 0 20px;
  }
}
  .container {
    padding: 0 20px 20px 20px;
    :deep(.arco-list-content) {
      overflow-x: hidden;
    }

    :deep(.arco-card-meta-title) {
      font-size: 14px;
    }
  }
  :deep(.arco-list-col) {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-between;
  }

  :deep(.arco-list-item) {
    width: 33%;
  }

  :deep(.block-title) {
    margin: 0 0 12px 0;
    font-size: 14px;
  }
  :deep(.list-wrap) {
    // min-height: 140px;
    .list-row {
      align-items: stretch;
      .list-col {
        margin-bottom: 16px;
      }
    }
    :deep(.arco-space) {
      width: 100%;
      .arco-space-item {
        &:last-child {
          flex: 1;
        }
      }
    }
  }
</style>
