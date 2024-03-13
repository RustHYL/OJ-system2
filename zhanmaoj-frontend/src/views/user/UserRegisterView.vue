<template>
  <div id="userRegister">
    <h1 style="margin-bottom: 32px">用户注册</h1>
    <a-form label-align="left" auto-label-width style="max-width: 480px; margin: 0 auto" :model="form" @submit="handleSubmit">
      <a-form-item field="userAccount" label="账号"
                   :rules="[{required:true,message:'账号是必填项'},{minLength:4,message:'账号至少4位'}]"
      >
        <a-input
            v-model="form.userAccount"
            placeholder="请输入账号..."
        />
      </a-form-item>
      <a-form-item field="userPassword" tooltip="密码不少于8位" label="密码"
                   :rules="[{required:true,message:'密码是必填项'},{minLength:8,message:'密码至少8位'}]"
      >
        <a-input-password v-model="form.userPassword" placeholder="请输入密码..." allow-clear/>
      </a-form-item>
      <a-form-item field="checkPassword" tooltip="密码不少于8位" label="确认密码"
                   :rules="[{required:true,message:'密码是必填项'},{minLength:5,message:'密码至少8位'}]"
      >
        <a-input-password v-model="form.checkPassword" placeholder="请确认密码..." allow-clear/>
      </a-form-item>
      <a-form-item field="isChecked">
        <a-checkbox v-model="isChecked">我已阅读并同意<a src="https://www.baidu.com/" class="agreement">服务协议</a>和<a src="https://www.baidu.com" class="agreement">战码OJ隐私保护指引</a></a-checkbox>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from "vue";
import {UserControllerService} from "../../../generated";
import {useRouter} from "vue-router";
import message from "@arco-design/web-vue/es/message";

const router = useRouter();

const handleSubmit = async () => {
  if (!isChecked.value){
    message.error("请先阅读并同意协议");
  } else {
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
}

const form = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
});

const isChecked = ref(false);

</script>

<style scoped>
#userRegister .agreement {
  color: blueviolet
}

</style>
