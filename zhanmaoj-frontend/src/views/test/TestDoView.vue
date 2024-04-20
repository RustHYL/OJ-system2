<template>
  <div class="maxBox">
    <div class="treeBox">
      <p style="font-weight: 700;color: #505050;display: flex;justify-content: space-between;line-height: 40px;margin: 0;border-bottom: 1px solid #eee;">
        <span>题目 {{examTimeShow}}</span>
        <span>总分数:{{ totalScore }}</span>
      </p>
      <div style="height: calc(100% - 40px);overflow: auto">
        <a-tree :field-names="fieldNames" showLine :only-check-leaf="true" blockNode :data="treeData" @select="checkTree"/>
      </div>
    </div>
    <div class="contentBox">
      <p style="background-color: #f6f7f9;height: 40px;margin: 0">
        <span
            style="display: block;font-weight: 700;background-color: #fff;line-height: 40px;font-size: 16px;width: 100px;text-align: center;color: #505050">题目描述</span>
      </p>
      <div class="subject">
        <p class="title">{{selectedTree.title}}
          <a-tag style="margin-left: 10px">分数 {{ selectedTree.score }}</a-tag>
        </p>
        <a-divider />
        <div v-if="selectedType === 'question'">
          <a-descriptions
              title="判题条件"
              :column="{ xs: 1, md: 2, lg: 3 }"
          >
            <a-descriptions-item label="时间限制">
              {{ judgeConfig.timeLimit ?? 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="内存限制">
              {{ judgeConfig.memoryLimit ?? 0 }}
            </a-descriptions-item>
            <br>
            <a-descriptions-item label="堆栈限制">
              {{ judgeConfig.stackLimit ?? 0 }}
            </a-descriptions-item>
          </a-descriptions>
        </div>
        <a-divider />
        <div class="trueOrFalse" v-if="selectedType === 'trueOrFalse'">
          <a-radio-group v-model="myTrueOrFalseAnswer[answerIndex].value">
            <a-radio style="margin-bottom: 15px" :value="1">A, True</a-radio>
            <a-radio style="margin-bottom: 15px" :value="2">B, False</a-radio>
          </a-radio-group>
        </div>
        <div class="choice" v-if="selectedType === 'choice'">
          <a-radio-group v-model="myChoiceAnswer[answerIndex].value">
            <a-radio style="margin-bottom: 15px" :value="1">A, {{selectedTree.optionA}}</a-radio>
            <a-radio style="margin-bottom: 15px" :value="2">B, {{selectedTree.optionB}}</a-radio>
            <a-radio style="margin-bottom: 15px" :value="3">C, {{selectedTree.optionC}}</a-radio>
            <a-radio style="margin-bottom: 15px" :value="4">D, {{selectedTree.optionD}}</a-radio>
          </a-radio-group>
        </div>
        <div class="question" v-if="selectedType === 'question'">
          <MarkDownViewer :value="selectedTree.content || ''"/>
          <div class="submit">
            <a-button type="primary" @click="doQuestionSubmit">提交本题作答</a-button>
          </div>
        </div>
      </div>
    </div>
    <div class="answerBox">
      <div class="answer">
        <div v-show="selectedType === 'question'">
          <a-form :model="form" layout="inline">
            <a-form-item
                field="language"
                label="编程语言"
                style="width: 100%"
            >
              <a-select
                  v-model="form.language"
                  :style="{ width: '320px' }"
                  placeholder="选择编程语言"
              >
                <a-option>java</a-option>
<!--                <a-option>cpp</a-option>-->
<!--                <a-option>go</a-option>-->
              </a-select>
            </a-form-item>
          </a-form>
          <CodeEditor :value="form.code as string" :language="form.language" :handle-change="changeCode"/>
        </div>
      </div>
      <div class="submit">
        <a-button type="primary" @click="doSubmit">提交试卷</a-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {
  QuestionControllerService,
  QuestionSubmitAddTestRequest,
  TestControllerService, TestSubmitAddRequest
} from "../../../generated";
import MarkDownViewer from "@/components/MarkDownViewer.vue";
import CodeEditor from "@/components/CodeEditor.vue";
import message from "@arco-design/web-vue/es/message";

const treeData = ref([]);

const route = useRoute();

const router = useRouter();

const fieldNames = {
  children: 'children',
  title: 'title',
  key: 'id',
};

const myChoiceAnswer = ref([])

const myTrueOrFalseAnswer = ref([])

const myQuestionAnswer = ref([])

let totalScore = ref(0)

let examTime = ref(600)

let examTimeShow = ref('00:00')

function changeTime(){

  let time = examTime.value
  let min = Math.floor(time / 60)
  let sec = Math.floor(time % 60)
  examTimeShow.value = (min < 10 ? ('0' + min) : min) + ':' + (sec < 10 ? ('0' + sec) : sec)
  examTime.value --
  if (min === 0 && sec === 0) {
    message.info('考试时间到，自动提交试卷')
    doSubmit()
    return
  }else{
    setTimeout(changeTime, 1000)
  }
}

async function getTest() {
  let testId = route.params.id
  const res = await TestControllerService.getTestDetailUsingGet(testId)
  console.log(res.data)
  examTime.value = res.data?.examTime * 60
  changeTime()
  let choiceQuestionTestDetailVOS = res.data?.choiceQuestionTestDetailVOS
  let trueOrFalseTestDetailVOS = res.data?.trueOrFalseTestDetailVOS
  let questionTestDetailVOS = res.data?.questionTestDetailVOS
  totalScore = res.data.totalScore
  choiceQuestionTestDetailVOS?.forEach(item => {
    myChoiceAnswer.value.push({
      id:item.id,
      value: 0
    })
    item.id = 'choice' + item.id
  })
  trueOrFalseTestDetailVOS?.forEach(item => {
    myTrueOrFalseAnswer.value.push({
      id:item.id,
      value: 0
    })
    item.id = 'trueOrFalse' + item.id
  })
  questionTestDetailVOS?.forEach(item => {
    myQuestionAnswer.value.push({
      id:item.id
    })
    item.id = 'question' + item.id
  })
  treeData.value = [
    {
      title: '选择题',
      id: '1',
      children: choiceQuestionTestDetailVOS,
    },
    {
      title: '判断题',
      id: '2',
      children: trueOrFalseTestDetailVOS,
    },
    {
      title: '编程题',
      id: '3',
      children: questionTestDetailVOS,
    },
  ]
}

const form = ref<QuestionSubmitAddTestRequest>({
  language: "java",
  code: "",
  testId: route.params.id as any,
});

const changeCode = (value: string) => {
  form.value.code = value;
};

const selectedTree = ref({})

const selectedType = ref('')

const judgeConfig = ref({
  timeLimit: 999,
  memoryLimit: 999,
  stackLimit: 999,
})

let answerIndex = ref(0)

const questionId = ref('')

function checkTree(selected,node){
  if (node.node.id == '1' || node.node.id == '2' || node.node.id == '3') {
    return
  }
  selectedTree.value = node.node
  console.log("selectedTree:"+JSON.stringify(selectedTree.value))
  let choice = selectedTree.value.id.indexOf('choice')
  let trueOrFalse = selectedTree.value.id.indexOf('trueOrFalse')
  let question = selectedTree.value.id.indexOf('question')
  if(choice !== -1){
    selectedType.value = 'choice'
    answerIndex = myChoiceAnswer.value.findIndex(item => item.id === selectedTree.value.id.split('choice')[1])
  }else if(trueOrFalse !== -1){
    selectedType.value = 'trueOrFalse'
    answerIndex = myTrueOrFalseAnswer.value.findIndex(item => item.id === selectedTree.value.id.split('trueOrFalse')[1])
  }else if(question !== -1){
    judgeConfig.value = selectedTree.value.judgeConfig
    questionId.value = selectedTree.value.id.split('question')[1]
    selectedType.value = 'question'
    answerIndex = myQuestionAnswer.value.findIndex(item => item.id === selectedTree.value.id.split('question')[1])
  }
  console.log(answerIndex)
}

const testSubmitAddRequest = ref<TestSubmitAddRequest>({
  trueOrFalseAnswerList: [],
  choiceAnswerList:[],
  testId: route.params.id as any,
})
async function doSubmit() {
  testSubmitAddRequest.value.trueOrFalseAnswerList = myTrueOrFalseAnswer.value
  testSubmitAddRequest.value.choiceAnswerList = myChoiceAnswer.value
  const res = await TestControllerService.doTestSubmitUsingPost(testSubmitAddRequest.value);
  if (res.code === 0) {
    message.success("提交成功");
    const id = res.data
    // 跳转到分数界面
    await router.push({path: `/question/test/score/${id}`})
  } else {
    message.error("提交失败，" + res.message);
  }
}

async function doQuestionSubmit() {
  if (!questionId.value) {
    return;
  }
  const res = await QuestionControllerService.doQuestionTestSubmitUsingPost({
    ...form.value,
    questionId: questionId.value as any,
  });
  if (res.code === 0) {
    message.success("提交成功," + res.message);
    form.value.code = '';
  } else {
    message.error("提交失败，" + res.message);
  }
}

getTest()
</script>


<style scoped>
.maxBox {
  width: 100%;
  height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
}

.treeBox {
  width: 20%;
  height: 100%;
  border-right: 1px solid #eee;
  padding: 0 4px;
}

.contentBox{
  width: 40%;
  height: 100%;
  border-right: 1px solid #eee;
}
.answerBox{
  width: 40%;
  height: 100%;
}

.contentBox .subject {
  padding: 10px 20px;
  height: calc(100% - 40px);
  overflow-y: auto;
}

.contentBox .subject .title {
  font-size: 18px;
  font-weight: 700
}

.answerBox{
  padding: 10px 20px;
  box-sizing: border-box;
}

.answerBox .answer{
  height: calc(100% - 40px);
  overflow: auto;

}
.answerBox .submit{
  height: 40px;
  border-top: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: right;
  padding-top: 10px;
  margin-bottom: 10px;
}
.question{
  height: calc(100% - 105px);
  overflow: auto;
  position: relative;
}
.question .submit{
  height: 40px;
  border-top: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: right;
  padding-top: 10px;
  position: absolute;
  width: 100%;
  bottom: 0;
}

</style>
