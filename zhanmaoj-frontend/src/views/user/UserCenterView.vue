<template>
  <div class="container">
    <Breadcrumb :items="['menu.user', 'menu.user.info']" />
    <UserInfoHeader />
    <div class="content">
      <div class="content-left">
        <a-grid :cols="24" :col-gap="16" :row-gap="16">
          <a-grid-item :span="24">
            <MyError />
          </a-grid-item>
        </a-grid>
      </div>
      <div class="content-right">
        <a-grid :cols="24" :row-gap="16">
          <a-grid-item :span="24">
            <MyTest />
          </a-grid-item>
        </a-grid>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import UserInfoHeader from '../../components/UserInfoHeader.vue';
import MyError from '../../components/MyError.vue';
import MyTest from '../../components/MyTest.vue';
</script>

<script lang="ts">
export default {
  name: 'Info',
};
</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
  width: 100%;
}

.content {
  display: flex;
  margin-top: 12px;

  &-left {
    flex: 1;
    margin-right: 16px;
    overflow: hidden;
    // background-color: var(--color-bg-2);

    :deep(.arco-tabs-nav-tab) {
      margin-left: 16px;
    }
  }

  &-right {
    width: 332px;
  }

  .tab-pane-wrapper {
    padding: 0 16px 16px 16px;
  }
}
</style>

<style lang="less" scoped>
.mobile {
  .content {
    display: block;
    &-left {
      margin-right: 0;
      margin-bottom: 16px;
    }
    &-right {
      width: 100%;
    }
  }
}
</style>