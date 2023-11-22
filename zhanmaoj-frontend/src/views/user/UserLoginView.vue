<template>
  <div id="userLogin">
    <h2 style="margin-bottom: 32px">用户登录</h2>
    <a-form label-align="left" auto-label-width style="max-width: 480px; margin: 0 auto" :model="form" @submit="handleSubmit">
      <a-form-item field="userAccount" label="账号">
        <a-input
            v-model="form.userAccount"
            placeholder="请输入账号..."
        />
      </a-form-item>
      <a-form-item field="userPassword" tooltip="密码不少于8位" label="密码">
        <a-input-password v-model="form.userPassword" placeholder="请输入密码..." allow-clear/>
      </a-form-item>
      <a-form-item field="isRemember">
        <a-checkbox v-model="isRemember"> 记住密码 </a-checkbox>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
      </a-form-item>
      <a-form-item>
        <a-button html-type="button" @click="toRegister" style="width: 100%">用户注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import {onBeforeUnmount, onMounted, reactive, ref} from "vue";
import {UserControllerService, UserLoginRequest} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import {useRouter} from "vue-router";
import {useStore} from "vuex";

const router = useRouter();
const store = useStore();

/**
 * 表单信息
 */
const form = reactive({
  userAccount: '',
  userPassword: '',
} as UserLoginRequest);

const isRemember = ref(false);


function onKeyUp(e: { key: string; }) {
  if (e.key == "Enter") {
    handleSubmit()
  }
}

onMounted(() => {

  //在setup中，用来加载页面时，查看账户密码是否存在
  if (localStorage.getItem("form")!=null && Object.keys(localStorage.getItem("form") as  string).length>2 ) {

    isRemember.value = true;
    const userPwdInfo = JSON.parse(localStorage.getItem("form") as  string);

    form.userAccount=userPwdInfo.userAccount;
    form.userPassword=userPwdInfo.userPassword;

  } else {
    isRemember.value = false;
  }

  document.addEventListener("keyup", onKeyUp)

})


// 移除键盘监听
onBeforeUnmount(() => {
  document.removeEventListener("keyup", onKeyUp)
})



/**
 * 提交表单
 */
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  if (res.code === 0) {
    await store.dispatch("user/getLoginUser")
    if (isRemember.value) {
      localStorage.setItem("form", JSON.stringify(form));
    } else {
      localStorage.removeItem("form");
    }
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error("登录失败：" + res.msg);
  }
};

const toRegister = () => {
  router.push({
    path: '/user/register',
  })
}

</script>

<style scoped>

</style>
