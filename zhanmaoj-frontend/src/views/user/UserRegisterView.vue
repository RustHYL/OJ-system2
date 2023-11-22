<template>
  <div id="userRegister">
    <a-form ref="formRef" size="medium" :model="form" :style="{width:'600px'}" @submit="handleSubmit" label-align="left">
      <a-form-item field="userAccount" label="账号"
                   :rules="[{required:true,message:'账号是必填项'},{minLength:4,message:'账号长度不能小于4位'}]"
                   :validate-trigger="['change','input']"
      >
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码"
                   :rules="[{required:true,message:'密码是必填项'},{minLength:8,message:'密码长度不能小于8位'}]"
                   :validate-trigger="['change','input']"
      >
        <a-input v-model="form.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <a-form-item field="checkPassword" label="确认密码"
                   :rules="[{required:true,message:'请确认密码'},{minLength:8,message:'密码长度不能小于8位'}]"
                   :validate-trigger="['change','input']"
      >
        <a-input v-model="form.checkPassword" placeholder="请确认密码" />
      </a-form-item>

      <a-form-item>
        <a-space>
          <a-button html-type="submit">注册</a-button>
          <a-button @click="$refs.formRef.resetFields()">重置</a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {reactive} from "vue";
import {UserControllerService} from "../../../generated";
import {useRouter} from "vue-router";
import message from "@arco-design/web-vue/es/message";

const router = useRouter();

const handleSubmit = async () => {
  const res = await UserControllerService.userRegisterUsingPost(form);
  if (res.code === 0){
    await router.push({
      path: '/user/login',
      replace: true
    })
  } else {
    form.userAccount = '';
    form.userPassword = '';
    form.checkPassword = '';
    message.error("注册失败" + res.msg)
  }
}
const form = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
});

</script>

<style scoped>
#userRegister {
  display: flex;
  justify-content: center;
}
</style>
