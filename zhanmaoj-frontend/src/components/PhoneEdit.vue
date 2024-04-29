<template>
  <div class="security-card">
    <div class="showVerify" >
      <div class="card-header">
        <h2>安全验证</h2>
        <p>为了保护你的账号安全，需要验证你的身份，验证通过后即可正常使用。</p>
      </div>
      <div class="card-body">
        <div class="security-option" @click="doSubmit" v-if="store.state.user?.loginUser?.phone">
          <div class="icon">
            <img src="../assets/phone.png" alt="Phone">
          </div>
          <div class="details">
            <p class="option-title">手机号验证</p>
            <p class="option-description">通过{{ formatPhoneNumber(store.state.user.loginUser.phone) }}发送短信验证</p>
          </div>
        </div>
        <div class="security-option" @click="doSubmit">
          <div class="icon">
            <img src="../assets/password.png" alt="Password">
          </div>
          <div class="details">
            <p class="option-title">密码验证</p>
            <p class="option-description">提升账号信息与个人资料的安全性</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from "vue";
import message from "@arco-design/web-vue/es/message";
import {useStore} from "vuex";

const store = useStore();

const form = reactive({
  name: '',
  post: '',
  isRead: false,
})

const props = defineProps({
  type: String
})

const formatPhoneNumber = (phone: string) => {
  return phone.slice(0, 3) + '******' + phone.slice(-3);
}

const doSubmit = () => {
  message.success('点击了');
}

</script>

<style scoped>

.showVerify {
  padding-left: 40px;
  padding-right: 40px;
}

.security-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin: 20px auto;
  width: 550px;
  height: 550px;
}

.card-header h2 {
  color: #333;
  margin-bottom: 8px;
}

.card-header p {
  color: #666;
  font-size: 14px;
}

.card-body {
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.security-option {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  cursor: pointer;
  border: 1px solid #d3d3d3;
  border-radius: 4px;
  padding: 10px;
}

.icon {
  margin-right: 10px;
  /* Adjust size as needed */
  width: 40px;
  height: 40px;
}

.icon img {
  width: 100%;
  height: auto;
}

.details {
  flex-grow: 1;
}

.option-title {
  color: #333;
  font-weight: bold;
}

.option-description {
  color: #666;
  font-size: 14px;
}
</style>