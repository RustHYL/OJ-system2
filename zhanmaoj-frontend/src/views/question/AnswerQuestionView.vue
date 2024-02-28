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
          <a-tab-pane key="comment" title="评论区">
            <!-- todo 评论区-->
            评论区
          </a-tab-pane>
          <a-tab-pane key="answer" title="答案">
            <!--todo 答案-->
            答案展示
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
  QuestionSubmitAddRequest,
  QuestionVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import CodeEditor from "@/components/CodeEditor.vue";
import MarkDownViewer from "@/components/MarkDownViewer.vue";

const question = ref<QuestionVO>();

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code: "",
});

interface props {
  id: string;
}

const props = withDefaults(defineProps<props>(), {
  id: () => "",
});

const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id as any
  );
  if (res.code === 0) {
    question.value = res.data;
  } else {
    message.error("加载失败，" + res.message);
  }
};

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
    message.success("提交成功");
  } else {
    message.error("提交失败，" + res.message);
  }
}

</script>

<style>
#answerQuestionView {
  max-width: 1420px;
  margin: 0 auto;
}

#answerQuestionView .arco-space-horizontal .arco-space-item {
  margin-bottom: 0 !important;
}

</style>
