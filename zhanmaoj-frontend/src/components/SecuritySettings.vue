<template>
  <a-list :bordered="false" style="background-color: white">
    <a-list-item>
      <a-list-item-meta>
        <template #avatar>
          <a-typography-paragraph>
            登陆密码
          </a-typography-paragraph>
        </template>
        <template #description>
          <div class="content">
            <a-typography-paragraph>
              已设置。密码至少8位字符，支持数字、字母和除空格外的特殊字符。
            </a-typography-paragraph>
          </div>
          <div class="operation">
            <a-link :class="{'is-bound': store.state.user?.loginUser?.password, 'is-unbound': !store.state.user?.loginUser?.password}" @click="handleNumberClick('password')">
              修改
            </a-link>
          </div>
        </template>
      </a-list-item-meta>
    </a-list-item>
    <a-list-item>
      <a-list-item-meta>
        <template #avatar>
          <a-typography-paragraph>
            安全手机
          </a-typography-paragraph>
        </template>
        <template #description>
          <div class="content">
            <a-typography-paragraph>
              {{ store.state.user?.loginUser?.phone ? '已绑定：' + formatPhoneNumber(store.state.user.loginUser.phone) : '您暂未设置手机号码，绑定手机号码可以用来找回密码、接收通知等。' }}
            </a-typography-paragraph>
          </div>
          <div class="operation">
            <a-link v-if="store.state.user?.loginUser?.phone" :class="{'is-bound': store.state.user?.loginUser?.phone, 'is-unbound': !store.state.user?.loginUser?.phone}" @click="handleNumberClick('phone')">
              修改
            </a-link>
            <a-link v-if="!store.state.user?.loginUser?.phone" :class="{'is-bound': store.state.user?.loginUser?.phone, 'is-unbound': !store.state.user?.loginUser?.phone}" @click="handleNumberClick('verify')">
              绑定
            </a-link>
          </div>
        </template>
      </a-list-item-meta>
    </a-list-item>
    <div class="center-form-container" v-if="showOverlay">
      <VerifyChoice :type="type"/>
    </div>
  </a-list>
</template>

<script lang="ts" setup>
import {useStore} from "vuex";
import {ref} from "vue";
import VerifyChoice from "@/components/VerifyChoice.vue";

const store = useStore();

const showForm = ref(false);
const type = ref<String>('')

const props = defineProps({
  showOverlay: {
    type: Boolean,
    default: false
  },
})

const formatPhoneNumber = (phone: string) => {
  return phone.slice(0, 3) + '******' + phone.slice(-3);
}

const emit = defineEmits([
  'styleStatus'
]);
const handleNumberClick = (value: string) => {
  type.value = value
  showForm.value = !showForm.value
  emit('styleStatus', showForm.value)
}

</script>

<style scoped lang="less">
  :deep(.arco-list-item) {
    border-bottom: none !important;
    .arco-typography {
      margin-bottom: 20px;
    }
    .arco-list-item-meta-avatar {
      margin-bottom: 1px;
    }
    .arco-list-item-meta {
      padding: 0;
    }
  }
  :deep(.arco-list-item-meta-content) {
    flex: 1;
    border-bottom: 1px solid var(--color-neutral-3);

    .arco-list-item-meta-description {
      display: flex;
      flex-flow: row;
      justify-content: space-between;

      .tip {
        color: rgb(var(--gray-6));
      }
      .operation {
        margin-right: 6px;
      }
    }
  }
  .center-form-container {
    position: fixed; /* 使用fixed定位，这样表单会停留在视图的中间，即使滚动页面也不会移动 */
    top: 50%; /* 将容器的顶部设置在视图的中间 */
    left: 50%; /* 将容器的左边设置在视图的中间 */
    transform: translate(-50%, -50%); /* 通过translate将容器自身的中心与视图的中心对齐 */
    z-index: 999; /* 确保表单在其他内容之上 */
    //width: 650px /* 设置表单的宽度 */;
    //height: 550px/* 设置表单的高度 */;
    //background-color: blue;
    /* 添加其他需要的样式，如背景色、边框等 */
  }
</style>
