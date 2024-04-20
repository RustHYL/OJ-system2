<template>
  <div id="answerQuestionView">
    <a-row :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目内容">
            <a-card v-if="question" :title="question.title">
              <a-descriptions
                  title="判题条件"
                  :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig.timeLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }}
                </a-descriptions-item>
              </a-descriptions>
              <MarkDownViewer :value="question.content || ''"/>
              <template #extra>
                <a-space wrap>
                  <a-tag v-for="(tag, index) of question.tags" :key="index" color="green">{{ tag }}</a-tag>
                </a-space>
              </template>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="answer" title="答案">
            <MarkDownViewer :value="questionAnswer"/>
          </a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <a-form :model="form" layout="inline">
          <a-form-item
              field="language"
              label="编程语言"
              style="min-width: 240px"
          >
            <a-select
                v-model="form.language"
                :style="{ width: '320px' }"
                placeholder="选择编程语言"
            >
              <a-option>java</a-option>
              <a-option>cpp</a-option>
              <a-option>go</a-option>
            </a-select>
          </a-form-item>
        </a-form>
        <div class="card-container" v-if="showForm">
          <div class="card">
            <QuestionSubmitInfoCard :questionSubmitResult="questionSubmitResult" :title="question.title" @closeEvent="handleCloseEvent"/>
          </div>
        </div>
        <CodeEditor :value="form.code as string" :language="form.language" :handle-change="changeCode"/>
        <a-divider size="0"/>
        <a-button type="primary" style="min-width: 160px" @click="doSubmit">提交</a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest, QuestionSubmitVO,
  QuestionVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import CodeEditor from "@/components/CodeEditor.vue";
import MarkDownViewer from "@/components/MarkDownViewer.vue";
import QuestionSubmitInfoCard from "@/components/QuestionSubmitInfoCard.vue";

const question = ref<QuestionVO>();

const submitId = ref();

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
});

interface props {
  id: string;
}
const showForm = ref(false);
const changeShowForm = () => {
  showForm.value = !showForm.value;
};

const props = withDefaults(defineProps<props>(), {
  id: () => "",
});

const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id as any
  );
  console.log("questionVO:" + JSON.stringify(res.data))
  if (res.code === 0) {
    question.value = res.data;
    questionAnswer.value = question.value?.answer;
  } else {
    message.error("加载失败，" + res.message);
  }
};

const questionAnswer = ref();

const questionSubmitResult = ref<QuestionSubmitVO>();

/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const changeCode = (value: string) => {
  form.value.code = value;
};

const router = useRouter();

const doSubmit = async () => {
  if (!question.value?.id) {
    return;
  }
  const res = await QuestionControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    message.success("提交成功,正在判题...");
  } else {
    message.error("提交失败，" + res.message);
  }
  submitId.value = res.data;
  console.log("submitId:" + submitId.value)
  // 开始轮询判题结果，直到完成或出错
  let result = null;
  let pollActive = true;
  while (pollActive) {
    try {
      const statusRes = await QuestionControllerService.getQuestionSubmitStatusUsingGet(submitId.value);
      if (statusRes.code === 0) {
        const { status, result: judgementResult } = statusRes.data;
        if (status === 'COMPLETED') {
          // 判题完成
          result = judgementResult;
          pollActive = false; // 停止轮询
        } else {
          // 判题还在进行中，稍微等待一下
          await new Promise(resolve => setTimeout(resolve, 2000));
        }
      } else {
        // 查询状态失败，处理错误
        console.error('查询判题状态失败：', statusRes.message);
        pollActive = false; // 停止轮询
      }
    } catch (error) {
      // 发生其他错误，停止轮询
      console.error('轮询过程中发生错误：', error);
      pollActive = false;
    }
  }

  // 判题完成后，获取完整的判题结果
  const finalResultRes = await QuestionControllerService.getQuestionSubmitVoUsingGet(submitId.value);
  console.log("finalResultRes:", finalResultRes);
  if (finalResultRes.code === 0) {
    questionSubmitResult.value = finalResultRes.data;
    changeShowForm(); // 判题完成后调用
  } else {
    message.error("获取判题结果失败，" + finalResultRes.message);
  }
}


const handleCloseEvent = () => {
  showForm.value = false;
  // 在这里你可以根据需要对参数进行处理
};

</script>

<style>
#answerQuestionView {
  width: 1420px;
  margin: 0 auto;
}

#answerQuestionView .arco-space-horizontal .arco-space-item {
  margin-bottom: 0 !important;
}

.card-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000; /* 确保卡片在其他内容之上 */
}

.card {
  width: 900px;
  height: 580px;
}

</style>
