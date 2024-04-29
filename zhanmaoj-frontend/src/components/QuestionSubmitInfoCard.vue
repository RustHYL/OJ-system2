<template>
  <a-card :style="{ width: '100%', height: '100%' }">
    <template #cover>
      <div :style="{ display: 'flex', justifyContent: 'center' }">
        <h2>{{ props.title }}</h2>
      </div>
    </template>
    <template #extra>
      <a-link @click="close">×</a-link>
    </template>
    <a-card-meta title="代码详情">
      <template #avatar>

        <div
            :style="{ display: 'flex', alignItems: 'center', color: '#1D2129' }"
        >
        </div>
      </template>
    </a-card-meta>
    <div class="submitInfo" style="display: flex;justify-content: center;flex-direction: column;width: 100%; align-items: center">
      <div class="status" >
        状态:&nbsp;<a-tag checkable color="arcoblue" :default-checked="true">{{ props.questionSubmitResult.judgeInfo.message }}</a-tag>&nbsp;&nbsp;语言:&nbsp;{{ props.questionSubmitResult.language }}
      </div>
      <div class="use">
        时间:&nbsp;{{ props.questionSubmitResult.judgeInfo.time ? (props.questionSubmitResult.judgeInfo.time + 'ms') : '0ms' }}&nbsp;&nbsp;内存:&nbsp;{{ getFormattedMemory() }}
      </div>
    </div>
    <div
        :style="{
          height: getHeight(),
          overflow: 'hidden',
          marginTop: '20px',
        }"
    >
      <code-editor :value="props.questionSubmitResult.code" :language="props.questionSubmitResult.language"/>
    </div>
    <div v-if="props.questionSubmitResult.judgeInfo.detailErrorMessage"
        :style="{
          height: '110px',
          overflow: 'hidden',
        }"
    >
      <p>错误信息</p>
      <code-editor :value="props.questionSubmitResult.judgeInfo.detailErrorMessage" :language="props.questionSubmitResult.language"/>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import CodeEditor from "@/components/CodeEditor.vue";
import {onMounted} from "vue";


const props = defineProps({
  questionSubmitResult: {
    type: Object,
  },
  title: {
    type: String
  }
})

onMounted(() => {
  console.log("questionSubmitResult========" + JSON.stringify(props.questionSubmitResult))
});

const emit = defineEmits(['closeEvent']);

const getFormattedMemory = () => {
  const memory = props.questionSubmitResult?.judgeInfo.memory;
  // 检查 memory 是否为 null 或 undefined
  if (memory == null) {
    return '0 MB';
  }
  // 如果不是 null 或 undefined，则进行转换并返回格式化的字符串
  return (memory / 1024 / 1024).toFixed(2) + 'MB';
};

const close = () => {
  // 假设你想要传递一个参数给父组件，这里我们传递一个对象

  // 触发名为 'closeEvent' 的自定义事件，并将参数传递给父组件
  emit('closeEvent');
};

const getHeight = () => {

  if (props.questionSubmitResult?.judgeInfo.detailErrorMessage != null) {
    return "220px"
  } else {
    return "330px"
  }
}


</script>
<style scoped>
.icon-hover {
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.1s;
}
.icon-hover:hover {
  background-color: rgb(var(--gray-2));
}
</style>