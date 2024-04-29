<template>
  <a-form
    ref="formRef"
    :model="formData"
    class="form"
    :label-col-props="{ span: 8 }"
    :wrapper-col-props="{ span: 16 }"
  >
    <a-form-item
        field="id"
        label="ID"
        disabled
    >
      <a-input
          v-model="formData.id"
          :placeholder=store.state.user?.loginUser?.id
      />
    </a-form-item>
    <a-form-item
        field="userAccount"
        label="账号"
        disabled
    >
      <a-input
          v-model="formData.userAccount"
          :placeholder=store.state.user?.loginUser?.userAccount
      />
    </a-form-item>
    <a-form-item
        field="userName"
        label="名称"
    >
      <a-input
          v-model="formData.userName"
          :placeholder="store.state.user?.loginUser?.userName || '请填写名称'"
      />
    </a-form-item>
    <a-form-item
        field="gender"
        label="性别"
    >
      <a-select
          v-model="formData.gender"
          :placeholder= "store.state.user?.loginUser?.gender === 0 ? '男' : '女'"
      >
        <a-option value= 0 >男</a-option>
        <a-option value= 1 >女</a-option>
      </a-select>
    </a-form-item>
    <a-form-item
      field="email"
      label="邮箱"
    >
      <a-input
        v-model="formData.email"
        :placeholder="store.state.user?.loginUser?.email || '请填写邮箱'"
      />
    </a-form-item>
    <a-form-item
      field="userProfile"
      label="简介"
      :rules="[
        {
          maxLength: 200,
          message: 'profile is too long',
        },
      ]"
      row-class="keep-margin"
    >
      <a-textarea
        v-model="formData.userProfile"
        :placeholder="store.state.user?.loginUser?.userProfile || '请填写简介'"
      />
    </a-form-item>
    <a-form-item>
      <a-space>
        <a-button type="primary" @click="updateMine">
          保存
        </a-button>
        <a-button type="secondary" @click="reset">
          重置
        </a-button>
      </a-space>
    </a-form-item>
  </a-form>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import {useStore} from "vuex";
  import {UserControllerService} from "../../generated";
  import message from "@arco-design/web-vue/es/message";
  const store = useStore();

  const formRef = ref<FormInstance>();
  const formData = ref<any>({
    id: store.state.user?.loginUser?.id,
  });
  const updateMine = async () => {
    const res = await UserControllerService.updateMyUserUsingPost(formData.value);
    if (res.code === 0) {
      message.success('更新成功');
      await store.dispatch('user/getLoginUser');
      location.reload();
    }else {
      message.error('更新失败:' + res.message);
    }
  };
  const reset = async () => {
    await formRef.value?.resetFields();
  };
</script>

<style scoped lang="less">
  .form {
    width: 540px;
    margin: 0 auto 0 50px;
  }
</style>
