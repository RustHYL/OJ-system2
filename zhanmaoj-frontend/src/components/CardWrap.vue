<template>
  <div class="card-wrap">
    <a-card :bordered="false" hoverable>
      <a-space align="start">
        <a-card-meta>
          <template #title>
            <a-typography-text style="margin-right: 10px">
              {{ title }}
            </a-typography-text>
            <a-tag v-if="isExpires" size="small" color="red">
              <template #icon>
                <icon-check-circle-fill />
              </template>
              <span>已过期</span>
            </a-tag>
          </template>
          <template #description style="color: black">
            {{ content }}
            <slot></slot>
          </template>
        </a-card-meta>
        <a-space>
          <a-button @click="handleToggle" style="position: absolute;bottom: 10px; right: 10px">
            加入考试
          </a-button>
          <a-modal v-model:visible="showPasswordDialog" @ok="handleOk" @cancel="handleCancel">
            <template #title>
              填写密码
            </template>
            <a-input v-model="password" :style="{width:'320px'}" default-value="content" placeholder="请输入密码" allow-clear />
          </a-modal>
        </a-space>
      </a-space>
      <template #actions>
      </template>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import message from "@arco-design/web-vue/es/message";
  import {TestControllerService} from "../../generated";
  import {useRouter} from "vue-router";

  const props = defineProps({
    title: {
      type: String,
      default: '',
    },
    content: {
      type: String,
      default: '',
    },
    icon: {
      type: String,
      default: '',
    },
    beginTime: {
      type: Date,
    },
    expiredTime: {
      type: Date,
    },
    expire:{
      type: Boolean,
      default: false
    },
    status: {
      type: Number,
      default: 0,
    },
    id: {
      type: Number,
      default: 0,
    }
  });

  const password = ref('');

  const nowTime = ref(new Date());

  const router = useRouter();

  const showPasswordDialog = ref(false);

  const isExpires = ref(props.expire);

  const handleToggle = async () => {
    console.log("开始时间:" + props.beginTime, "结束时间：" + props.expiredTime, nowTime.value)
    if (props.beginTime > nowTime.value) {
      message.info('考试未开始');
    } else if (props.expiredTime < nowTime.value) {
      message.info('考试已结束');
    } else if (props.status === 0) {
      await router.push({
        path: `/collect/test/do/${props.id}`, // 使用模板字符串来插入参数
      })
    } else if (props.status === 1) {
      //打开弹窗
      showPasswordDialog.value = true;
    }
  }


  const handleOk = async () => {
    if (!props.id) {
      return;
    }
    const res = await TestControllerService.joinTestUsingPost({
      testId: props.id,
      password: password.value
    })
    if (res.code === 0) {
      password.value = '';
      await router.push({
        path: `/collect/test/do/${props.id}`, // 使用模板字符串来插入参数
      })
    } else {
      message.error('加入失败' + (res.message ? `，${res.message}` : ''))
    }
  };
  const handleCancel = () => {
    password.value = '';
    showPasswordDialog.value = false;
  }


</script>

<style scoped lang="less">
  .card-wrap {
    height: 100%;
    transition: all 0.3s;
    border: 1px solid var(--color-neutral-3);
    border-radius: 4px;
    &:hover {
      transform: translateY(-4px);
      // box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.1);
    }
    :deep(.arco-card) {
      height: 100%;
      border-radius: 4px;
      .arco-card-body {
        height: 100%;
        .arco-space {
          width: 100%;
          height: 100%;
          .arco-space-item {
            height: 100%;
            &:last-child {
              flex: 1;
            }
            .arco-card-meta {
              height: 100%;
              display: flex;
              flex-flow: column;
              .arco-card-meta-content {
                flex: 1;
                .arco-card-meta-description {
                  margin-top: 8px;
                  color: rgb(var(--gray-6));
                  line-height: 20px;
                  font-size: 12px;
                }
              }
              .arco-card-meta-footer {
                margin-top: 0;
              }
            }
          }
        }
      }
    }
    :deep(.arco-card-meta-title) {
      display: flex;
      align-items: center;

      // To prevent the shaking
      line-height: 28px;
    }
    :deep(.arco-skeleton-line) {
      &:last-child {
        display: flex;
        justify-content: flex-end;
        margin-top: 20px;
      }
    }
  }
</style>
