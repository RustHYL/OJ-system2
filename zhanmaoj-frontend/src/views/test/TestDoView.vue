<template>
  <div class="home">
    <div class="tab">
      <div class="tab__type">
        <div class="tab__type_header">
          <img src="../../assets/left.png" alt="">
          <span style="flex: 1">题型</span>
          <span>作答 / 题数</span>
        </div>
        <div class="tab__type_item" @click="selectType(1)" :style="type == 1 ? 'color:#0078d7' : ''">
          <img src="../../assets/icon1.png" alt="">
          <span class="tab__type_item_text">判断题</span>
          <span>{{trueOrFalseList.length}} / {{trueOrFalseList.length}}</span>
        </div>
        <div class="tab__type_item" @click="selectType(2)" :style="type == 2 ? 'color:#0078d7' : ''">
          <img src="../../assets/icon1.png" alt="">
          <span class="tab__type_item_text">单选题</span>
          <span>{{choiceQuestionList.length}} / {{choiceQuestionList.length}}</span>
        </div>
        <div class="tab__type_item" @click="selectType(4)" :style="type == 4 ? 'color:#0078d7' : ''">
          <img src="../../assets/icon1.png" alt="">
          <span class="tab__type_item_text">编程填空题</span>
          <span>26 / 26</span>
        </div>
        <div class="btn" @click="submit()">提交</div>
      </div>
      <div class="tab__text" v-if="type == 1">
        <div v-for="item in trueOrFalseList">
          <img v-if="item.answer" src="../../assets/no.png" alt="">
          <img v-else src="../../assets/noting.png" alt="">
        </div>
      </div>
      <div class="tab__text" v-if="type == 2">
        <div v-for="item in choiceQuestionList">
          <img v-if="item.answer" src="../../assets/no.png" alt="">
          <img v-else src="../../assets/noting.png" alt="">
        </div>
      </div>
    </div>
    <div class="main" v-if="type == 1">
      <a-collapse>
        <a-collapse-item v-for="(item,index) in trueOrFalseList" :key="index" :header="item.id + '.' + item.content + '   题目(' + item.score + ')分'" key="1">
          <div>{{item.content}}</div>
          <a-space direction="vertical" size="large">  
            <a-radio-group direction="vertical" v-model="item.answer">
              <a-radio value=0>正确</a-radio>
              <a-radio value=1>错误</a-radio>
            </a-radio-group>
          </a-space>
        </a-collapse-item>
      </a-collapse>
    </div>
    <div class="main" v-if="type == 2">
      <a-collapse>
        <a-collapse-item v-for="(item,index) in choiceQuestionList" :key="index" :header="item.id + '.' + item.content + '   题目(' + item.score + ')分'" key="1">
          <div>{{item.content}}</div>
          <a-space direction="vertical" size="large">
            <a-radio-group v-model="item.answer">
              <a-radio :value="item.optionA">{{item.optionA}}</a-radio>
              <a-radio :value="item.optionB">{{item.optionB}}</a-radio>
              <a-radio :value="item.optionC">{{item.optionC}}</a-radio>
              <a-radio :value="item.optionD">{{item.optionD}}</a-radio>
            </a-radio-group>
          </a-space>
        </a-collapse-item>
      </a-collapse>
    </div>
    <div class="main" v-if="type == 3">
      <div class="main__item">
        编程题第一题
      </div>
      <div class="main__item">
        编程题第二题
      </div>
      <div class="main__item">
        编程题第三题
      </div>
      <div class="main__item">
        编程题第四题
      </div>
      <div class="main__item">
        编程题第五题
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {QuestionControllerService, TestControllerService, TestDetailGetRequest, TestVO} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import {useRoute} from "vue-router";

const value = ref<string>('asd');
const route = useRoute();

const type = ref<number>(1);

const selectType = (val: number): void => {
  type.value = val;
};

const testDetail = ref<TestVO>();

interface TestDetailGetRequest{
  id: string;
}

// const loadData = async () => {
//   const id = route.query.id;
//   if (!id) {
//     return;
//   }
//   console.log(route.query.id)
//   const testObj:TestDetailGetRequest = {
//     id: id as string
//   }
//   console.log(JSON.stringify(testObj))
//   const res = await TestControllerService.getTestDetailUsingPost(
//       testObj
//   );
//   if (res.code === 0) {
//     testDetail.value = res.data;
//     console.log(JSON.stringify(res.data));
//   } else {
//     message.error("加载失败，" + res.message);
//   }
// };

// onMounted(() => {
//   loadData();
// });

interface Question {
  id: number;
  content: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
  answer: string;
  score: string;
}

const choiceQuestionList = ref<Question[]>([
  {
    id: 1,
    content: 'text1',
    optionA: 'AAA1',
    optionB: 'bb1',
    optionC: 'cc1',
    optionD: 'dd1',
    answer: '',
    score: '0',
  },
  {
    id:2,
    content:'text2',
    optionA:'AAA2',
    optionB:'bb2',
    optionC:'cc2',
    optionD:'dd2',
    answer:'',
    score:'0',
  },
  {
    id:3,
    content:'text3',
    optionA:'AAA3',
    optionB:'bb3',
    optionC:'cc3',
    optionD:'dd3',
    answer:'',
    score:'0',
  },
  {
    id:4,
    content:'text4',
    optionA:'AAA4',
    optionB:'bb4',
    optionC:'cc4',
    optionD:'dd4',
    answer:'',
    score:'0',
  },
  {
    id:5,
    content:'text4',
    optionA:'AAA4',
    optionB:'bb4',
    optionC:'cc4',
    optionD:'dd4',
    answer:'',
    score:'0',
  },
  {
    id:6,
    content:'text4',
    optionA:'AAA4',
    optionB:'bb4',
    optionC:'cc4',
    optionD:'dd4',
    answer:'',
    score:'0',
  },
  // Add other questions here
]);

interface GivenAnswer {
  id: number;
  content: string;
  answer: string;
  score: string;
}

const trueOrFalseList = ref<GivenAnswer[]>([
  {
    id: 1,
    content: 'text1',
    answer: '',
    score: '0',
  },
  {
    id:2,
    content:'text1',
    answer:'',
    score:'0',
  },
  {
    id:3,
    content:'text1',
    answer:'',
    score:'0',
  },
  {
    id:4,
    content:'text1',
    answer:'',
    score:'0',
  },
  {
    id:5,
    content:'text1',
    answer:'',
    score:'0',
  },
  // Add other given answers here
]);

const toLocation = (url: string): void => {
  location.href = url;
};

const submit = (): void => {
  let trueOrFalseAnswer = trueOrFalseList.value.map(item => {
    return {
      'id': item.id,
      'answer': item.answer
    };
  });
  let choiceQuestionAnswer = choiceQuestionList.value.map(item => {
    return {
      'id': item.id,
      'answer': item.answer
    };
  });
  let dataObj = {
    'testId': testDetail.value?.id,
    'givenAnswer': trueOrFalseAnswer,
    'multipleAnswer': choiceQuestionAnswer
  };
  console.log(dataObj);
};


</script>

<style scoped>
  .home {
    display: flex;
    width: 100%;
  }
  .tab {
    height: 100vh;
    width: 350px;
    box-shadow: 5px 0 10px rgba(0, 0, 0, 0.1);
  }
  .tab__type {
    height: 350px;
    overflow-y: scroll
  }
  .tab__type_header {
    padding: 10px;
    font-size: 18px;
    color: #5a5a5a;
    display: flex;
    align-items: center;
  }
  .tab__type_header img {
    width: 20px;
    margin-right: 10px;
  }
  .tab__type_item {
    padding: 10px;
    font-size: 18px;
    color: #5a5a5a;
    display: flex;
    align-items: center;
  }
  .tab__type_item img {
    width: 16px;
    margin-right:8px;
    height: 16px;
  }
  .tab__type_item_text {
    flex: 1;
  }
  .tab__text {
    padding-top: 20px;
    padding-left: 12px;
    border-top: 1px solid #CCC;
    display: grid;
    grid-template-columns: repeat(5, 1fr); /* Five columns with equal width */
    gap: 12px; /* Gap between grid items */
  }
  .tab__text img {
    width: 40px;
    margin-right: 20px;
  }
  .main {
    width: 100%;
    background: #FFF;
    height: 100vh;
  }
  .main__item {
    border-top: 1px solid #f5f5f5;
    border-bottom: 1px solid #f5f5f5;
    height: 50px;
    width: 100%;
    line-height: 50px;
    padding-left: 10px;
  }
  .btn {
    width: 300px;
    margin: 0 auto;
    margin-top: 40px;
    background: linear-gradient(90deg, #2382ff, #4bb9fe);
    border-radius: 40px;
    height: 40px;
    text-align: center;
    line-height: 40px;
    color:#FFF;
    font-weight: bold;
  }
</style>
