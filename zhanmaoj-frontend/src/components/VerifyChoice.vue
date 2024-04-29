<template>
  <div class="security-card" :style="divStyle">
    <div class="showVerify" >
      <div class="card-header">
        <h2 v-if="!final">安全验证</h2>
        <h2 v-else-if="final && type === 'phone'">修改手机</h2>
        <h2 v-else-if="final && type === 'password'">修改密码</h2>
        <h2 v-else-if="final && type === 'verify'">绑定手机</h2>
        <p v-if="!final">为了保护你的账号安全，需要验证你的身份，验证通过后即可正常使用。</p>
      </div>
      <div class="card-body" v-show="showVerifyMain">
        <div class="security-option" @click="doPhoneVerify" v-if="store.state.user?.loginUser?.phone">
          <div class="icon">
            <img src="../assets/phone.png" alt="Phone">
          </div>
          <div class="details">
            <p class="option-title">手机号验证</p>
            <p class="option-description">通过{{ formatPhoneNumber(store.state.user.loginUser.phone) }}发送短信验证</p>
          </div>
        </div>
        <div class="security-option" @click="doPasswordVerify">
          <div class="icon">
            <img src="../assets/password.png" alt="Password">
          </div>
          <div class="details">
            <p class="option-title">密码验证</p>
            <p class="option-description">提升账号信息与个人资料的安全性</p>
          </div>
        </div>
      </div>
      <div class="card-body" v-if="showVerifyPhone">
        <h2>通过手机号{{ formatPhoneNumber(store.state.user.loginUser.phone) }}接收短信验证码</h2>
        <div class="code">
          <a-input placeholder="请输入验证码" allow-clear size="large" v-model="smsCode"/>
          <a-button type="outline" size="large" :disabled="isCounting" @click="sendVerifyCode">{{ buttonText }}</a-button>
        </div>
        <a-button type="primary" style="margin-top: 20px" long size="large" @click="doPhoneSubmit">确定</a-button>
        <div class="tip" @click="doPasswordVerify">手机号无法使用?试试密码辅助验证</div>
      </div>
      <div class="card-body" v-if="showVerifyPassword">
        <h2>通过密码进行验证</h2>
        <div style="display: flex; align-items: center; justify-content: flex-start; margin-top: 20px;">
          <a-input-password placeholder="请输入原始密码" size="large" v-model="oldPassword"/>
        </div>
        <a-button type="primary" long size="large" style="margin-top: 20px" @click="doPasswordSubmit">确定</a-button>
        <div v-if="type !== 'verify'" class="tip" @click="doPhoneVerify">忘记密码?试试手机辅助验证</div>
      </div>
      <div class="card-body" v-if="showEditPhone">
        <h2>通过手机号{{ formatPhoneNumber(store.state.user.loginUser.phone) }}接收短信验证码</h2>
        <a-input placeholder="请输入手机号" allow-clear size="large" v-model="newPhone">
          <template #prepend>
            +86
          </template>
        </a-input>
        <div class="code">
          <a-input placeholder="请输入验证码" allow-clear size="large" v-model="smsCodeNew"/>
          <a-button type="outline" size="large" :disabled="isCountingEdit" @click="sendEditVerifyCode">{{ buttonTextEdit }}</a-button>
        </div>
        <a-button type="primary" style="margin-top: 20px" long size="large" @click="doEditPhoneSubmit">确定</a-button>
      </div>
      <div class="card-body" v-if="showEditPassword">
        <div style="display: flex; align-items: center; justify-content: flex-start; margin-top: 20px;">
          <a-input-password placeholder="请输入新密码" size="large" v-model="newPassword"/>
        </div>
        <div style="display: flex; align-items: center; justify-content: flex-start; margin-top: 20px;">
          <a-input-password placeholder="请确认密码" size="large" v-model="confirmPassword"/>
        </div>
        <a-button type="primary" long size="large" style="margin-top: 20px" @click="doEditPasswordSubmit">确定</a-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {useStore} from "vuex";
import {SmsControllerService, UserControllerService} from "../../generated";
import message from "@arco-design/web-vue/es/message";

const store = useStore();

const style = ref<String>('')

const smsCode = ref('')
const oldPassword = ref('')
const smsCodeNew = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const newPhone = ref('')


const props = defineProps({
  type: String
})

const formatPhoneNumber = (phone: string) => {
  return phone.slice(0, 3) + '******' + phone.slice(-3);
}

const doPhoneVerify = () => {
  style.value = props.type!;
  divStyle.value.height = '350px';
  showVerifyMain.value = false
  showVerifyPhone.value = !showVerifyPhone.value
  showVerifyPassword.value = false
}
const doPasswordVerify = () => {
  style.value = props.type!;
  divStyle.value.height = '350px';
  showVerifyMain.value = false
  showVerifyPassword.value = !showVerifyPassword.value
  showVerifyPhone.value = false
}

const showVerifyMain = ref(true)

const showVerifyPhone = ref(false)

const showVerifyPassword = ref(false)

const showEditPhone = ref(false)

const showEditPassword = ref(false)

const statusInfo = ref('')

const final = ref(false)


// 倒计时秒数
const countdown = ref(180);
// 控制按钮是否处于倒计时状态
const isCounting = ref(false);
// 按钮文本
const buttonText = ref('发送验证码');

// 重置倒计时状态
const resetCountdown = () => {
  isCounting.value = false;
  countdown.value = 180;
  buttonText.value = '发送验证码';
};

// 启动倒计时
const sendVerifyCode = async () => {
  if (props.type == "phone"){
    statusInfo.value = 'MODIFYPHONE';
  } else if (props.type == "password"){
    statusInfo.value = 'MODIFYPASSWORD';
  }
  const res = await SmsControllerService.sendSmsUsingPost({
    phoneNumber: store.state.user?.loginUser?.phone,
    status: statusInfo.value,
  })
  if (res.code === 0) {
    if (isCounting.value) {
      // 如果已经在倒计时，则不执行任何操作
      return;
    }

    isCounting.value = true; // 设置按钮为倒计时状态
    buttonText.value = `${countdown.value}秒`; // 更新按钮文本为倒计时

    const intervalId = setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--;
        buttonText.value = `${countdown.value}秒`;
      } else {
        clearInterval(intervalId);
        resetCountdown(); // 倒计时结束，重置状态
      }
    }, 1000);
  } else {
    message.error("发送失败，" + res.message);
  }
};


// 倒计时秒数
const countdownEdit = ref(180);
// 控制按钮是否处于倒计时状态
const isCountingEdit = ref(false);
// 按钮文本
const buttonTextEdit = ref('发送验证码');

// 重置倒计时状态
const resetCountdownEdit = () => {
  isCountingEdit.value = false;
  countdownEdit.value = 180;
  buttonTextEdit.value = '发送验证码';
};

const sendEditVerifyCode = async () => {
  statusInfo.value = 'EDITPHONE';
  const res = await SmsControllerService.sendSmsUsingPost({
    phoneNumber: newPhone.value,
    status: statusInfo.value,
  })
  if (res.code === 0) {
    if (isCountingEdit.value) {
      // 如果已经在倒计时，则不执行任何操作
      return;
    }

    isCountingEdit.value = true; // 设置按钮为倒计时状态
    buttonTextEdit.value = `${countdownEdit.value}秒`; // 更新按钮文本为倒计时

    const intervalIdEdit = setInterval(() => {
      if (countdownEdit.value > 0 ) {
        countdownEdit.value--;
        buttonTextEdit.value = `${countdownEdit.value}秒`;
      } else {
        clearInterval(intervalIdEdit);
        resetCountdownEdit(); // 倒计时结束，重置状态
      }
    }, 1000);
  } else {
    message.error("发送失败，" + res.message);
  }
};


const doPhoneSubmit = async () =>{
  if (props.type == "phone" || props.type == "verify"){
    statusInfo.value = 'MODIFYPHONE';
  } else if (props.type == "password"){
    statusInfo.value = 'MODIFYPASSWORD';
  }
  const res = await SmsControllerService.validateSmsUsingPost({
    phoneNumber: store.state.user?.loginUser?.phone,
    status: statusInfo.value,
    code:smsCodeNew.value,
  })
  if (res.code === 0){
    if (res.data){
      showVerifyPhone.value = false;
      final.value = true
      if (props.type === 'phone'){
        showEditPhone.value = true;
      }else {
        showEditPassword.value = true;
      }
    }else {
      message.error("验证码不正确，请重试")
    }
  } else {
    message.error("系统错误:" + res.message)
  }
}

const doPasswordSubmit = async () =>{
  const res = await UserControllerService.verifyUserPasswordUsingPost(
    oldPassword.value
  )
  if (res.code === 0){
    if (res.data){
      showVerifyPassword.value = false;
      final.value = true
      if (props.type === 'password'){
        showEditPassword.value = true;
      }else {
        showEditPhone.value = true;
      }
    }else {
      message.error("密码错误，请重新输入")
    }
  } else {
    message.error("系统错误:" + res.message)
  }
}

const doEditPhoneSubmit = async () =>{
  statusInfo.value = 'EDITPHONE';
  const res = await UserControllerService.updateMyPhoneUsingPost({
    phoneNumber: newPhone.value,
    status: statusInfo.value,
    code:smsCodeNew.value,
  })
  if (res.code === 0){
    message.success("修改成功")
    window.location.reload()
  } else {
    message.error(res.message)
  }
}

const doEditPasswordSubmit = async () =>{
  const res = await UserControllerService.updateMyPasswordUsingPost({
    userPassword:newPassword.value,
    confirmPassword:confirmPassword.value
  })
  if (res.code === 0 && res.data){
    message.success("修改成功")
    window.location.reload()
  } else {
    message.error(res.message)
  }
}


const divStyle = ref({
  height: '550px', // 初始高度
});

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

.code {
  display: flex;
  gap: 15px;
  margin-top: 20px;
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

.tip {
  font-size: 18px;
  color: #36aafd;
  margin-top: 10px;
  display: flex;
  justify-content: center; /* 水平居中 */
}
</style>