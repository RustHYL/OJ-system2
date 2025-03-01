<template>
  <div id="addQuestionView">
    <div class="header">
      <a-breadcrumb>
        <a-breadcrumb-item>题目</a-breadcrumb-item>
        <a-breadcrumb-item>创建题目</a-breadcrumb-item>
      </a-breadcrumb>
      <h2 v-if="!updatePage">创建题目</h2>
      <h2 v-else>编辑题目</h2>
    </div>
    <div class="container">
    <div class="layout">
      <a-form :model="form" label-align="left" style="max-width: 640px">
        <a-form-item field="title" label="标题">
          <a-input v-model="form.title" placeholder="请输入标题" allow-clear/>
        </a-form-item>
        <a-form-item field="tags" label="标签">
          <a-input-tag v-model="form.tags" placeholder="请输入标签，回车键生成标签" allow-clear />
        </a-form-item>
        <a-form-item field="content" label="题目内容">
          <MarkdownEditor :value="form.content" :handle-change="onContentChange" />
        </a-form-item>
        <a-form-item field="answer" label="答案">
          <MarkdownEditor :value="form.answer" :handle-change="onAnswerChange" />
        </a-form-item>
        <a-form-item label="判题配置" :content-flex="false" :merge-props="false">
          <a-space direction="vertical" style="min-width: 480px">
            <a-form-item field="judgeConfig.timeLimit" label="时间限制" style="width: 350px">
              <a-input-number
                  v-model="form.judgeConfig.timeLimit"
                  placeholder="请输入时间限制(ms)"
                  mode="button"
                  min="0"
                  max="950"
                  size="large"
              >
                <template #suffix>
                  ms
                </template>
              </a-input-number>
            </a-form-item>
            <a-form-item field="judgeConfig.memoryLimit" label="内存限制" style="width: 350px">
              <a-input-number
                  v-model="form.judgeConfig.memoryLimit"
                  placeholder="请输入内存限制(kb)"
                  mode="button"
                  min="0"
                  max="950"
                  size="large"
              >
                <template #suffix>
                  KB
                </template>
              </a-input-number>
            </a-form-item>
            <a-form-item field="judgeConfig.stackLimit" label="堆栈限制" style="width: 350px">
              <a-input-number
                  v-model="form.judgeConfig.stackLimit"
                  placeholder="请输入堆栈限制"
                  mode="button"
                  min="0"
                  max="950"
                  size="large"
              >
                <template #suffix>
                  Byte
                </template>
              </a-input-number>
            </a-form-item>
          </a-space>
        </a-form-item>
        <a-form-item
            label="测试用例配置"
            :content-flex="false"
            :merge-props="false"
        >
          <a-form-item
              v-for="(judgeCaseItem, index) of form.judgeCase"
              :key="index"
              no-style
          >
            <a-space direction="vertical" style="min-width: 640px">
              <a-form-item
                  :field="`form.judgeCase[${index}].input`"
                  :label="`输入用例-${index}`"
                  :key="index"
              >
                <a-input
                    v-model="judgeCaseItem.input"
                    placeholder="请输入测试输入用例"
                    style="width: 300px"
                    allow-clear
                />
              </a-form-item>
              <a-form-item
                  :field="`form.judgeCase[${index}].output`"
                  :label="`输出用例-${index}`"
                  :key="index"
              >
                <a-input
                    v-model="judgeCaseItem.output"
                    placeholder="请输入测试输出用例"
                    style="width: 300px"
                    allow-clear
                />
              </a-form-item>
              <a-button status="danger" @click="handleDelete(index)" style="margin-bottom: 10px">
                删除
              </a-button>
            </a-space>
          </a-form-item>
          <div style="margin-top: 32px">
            <a-button @click="handleAdd" type="outline" status="success"
            >新增测试用例
            </a-button>
          </div>
        </a-form-item>
        <div style="margin-top: 16px" />
        <a-form-item>
          <a-button type="primary" style="min-width: 200px" @click="doSubmit"
          >提交
          </a-button>
        </a-form-item>
      </a-form>
    </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import MarkdownEditor from "@/components/MarkdownEditor.vue";
import { QuestionControllerService } from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();
// 如果页面地址包含 update，视为更新页面
const updatePage = route.path.includes("update");

let form = ref({
  title: "",
  tags: [],
  answer: "",
  content: "",
  judgeConfig: {
    memoryLimit: 1000,
    stackLimit: 1000,
    timeLimit: 1000,
  },
  judgeCase: [
    {
      input: "",
      output: "",
    },
  ],
});


/**
 * 根据题目 id 获取老的数据
 */
const loadData = async () => {
  const id = route.query.id;
  if (!id) {
    return;
  }
  const res = await QuestionControllerService.getQuestionByIdUsingGet(
      id as any
  );
  if (res.code === 0) {
    form.value = res.data as any;
    // json 转 js 对象
    if (!form.value.judgeCase) {
      form.value.judgeCase = [
        {
          input: "",
          output: "",
        },
      ];
    } else {
      form.value.judgeCase = JSON.parse(form.value.judgeCase as any);
    }
    if (!form.value.judgeConfig) {
      form.value.judgeConfig = {
        memoryLimit: 1000,
        stackLimit: 1000,
        timeLimit: 1000,
      };
    } else {
      form.value.judgeConfig = JSON.parse(form.value.judgeConfig as any);
    }
    if (!form.value.tags) {
      form.value.tags = [];
    } else {
      form.value.tags = JSON.parse(form.value.tags as any);
    }
  } else {
    message.error("加载失败，" + res.message);
  }
};

onMounted(() => {
  loadData();
});

const doSubmit = async () => {
  console.log(form.value);
  // 区分更新还是创建
  if (updatePage) {
    const res = await QuestionControllerService.updateQuestionUsingPost(
        form.value
    );
    if (res.code === 0) {
      message.success("更新成功");
      await router.push('/question/manage');
    } else {
      message.error("更新失败，" + res.message);
    }
  } else {
    const res = await QuestionControllerService.addQuestionUsingPost(
        form.value
    );
    if (res.code === 0) {
      message.success("创建成功");
      await router.push('/question/manage');
    } else {
      message.error("创建失败，" + res.message);
    }
  }
};

/**
 * 新增判题用例
 */
const handleAdd = () => {
  form.value.judgeCase.push({
    input: "",
    output: "",
  });
};

/**
 * 删除判题用例
 */
const handleDelete = (index: number) => {
  form.value.judgeCase.splice(index, 1);
};

const onContentChange = (value: string) => {
  form.value.content = value;
};

const onAnswerChange = (value: string) => {
  form.value.answer = value;
};
</script>

<style scoped>
#addQuestionView {
  flex-direction: column;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
}

#addQuestionView .header {
  width: 100%;
  height: 5%;
}

#addQuestionView .container {
  max-width: 800px;
  display: flex;
  justify-content: center;
  margin: 10px auto 0;
  padding: 70px;
  background-color: #FFFFFF;
}

#addQuestionView .layout {
  padding-top: 16px;
  display: flex;
  justify-content: center;
}
</style>
