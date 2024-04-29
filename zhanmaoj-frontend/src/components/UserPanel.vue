<template>
  <a-card :bordered="false">
    <a-space :size="54">
      <a-upload
        :custom-request="customRequest"
        list-type="picture-card"
        :file-list="fileList"
        :show-upload-button="true"
        :show-file-list="false"
        @change="uploadChange"
      >
        <template #upload-button>
          <a-avatar :size="100" class="info-avatar">
            <template #trigger-icon>
              <icon-camera />
            </template>
            <img v-if="fileList.length" :src="fileList[0].url" />
          </a-avatar>
        </template>
      </a-upload>
      <a-descriptions
        :data="renderData"
        :column="2"
        align="right"
        layout="inline-horizontal"
        :label-style="{
          width: '140px',
          fontWeight: 'normal',
          color: 'rgb(var(--gray-8))',
        }"
        :value-style="{
          width: '200px',
          paddingLeft: '8px',
          textAlign: 'left',
        }"
      >
        <template #label="{ label }">{{ label }} :</template>
        <template #value="{ value, data }">
          <a-tag
            v-if="data.label === '角色'"
            color="green"
            size="small"
          >
            {{value}}
          </a-tag>
          <span v-else>{{ value }}</span>
        </template>
      </a-descriptions>
    </a-space>
  </a-card>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import type {
    FileItem,
    RequestOption,
  } from '@arco-design/web-vue/es/upload/interfaces';
  import {useStore} from "vuex";
  import type { DescData } from '@arco-design/web-vue/es/descriptions/interface';
  import {FileControllerService, UserControllerService} from "../../generated";
  import message from "@arco-design/web-vue/es/message";

  const store = useStore();

  const avatarUrl = ref('');
  const file = {
    uid: '-2',
    name: 'avatar.png',
    url: store.state.user?.loginUser?.userAvatar,
  };
  const renderData = [
    {
      label: '名称',
      value: store.state.user?.loginUser?.userName,
    },
    {
      label: '账号',
      value: store.state.user?.loginUser?.userAccount,
    },
    {
      label: '邮箱',
      value: store.state.user?.loginUser?.email,
    },
    {
      label: '电话',
      value: store.state.user?.loginUser?.phone,
    },
    {
      label: '性别',
      value: (store.state.user?.loginUser?.gender === 0) ? '男' : (store.state.user?.loginUser?.gender === 1) ? '女' : undefined,
    },
    {
      label: '角色',
      value: (store.state.user?.loginUser?.userRole === 'user') ? '普通用户' :
          (store.state.user?.loginUser?.userRole === 'admin') ? '管理员' :
              (store.state.user?.loginUser?.userRole === 'super') ? '超级管理员' :
                  '未知角色',// 或者你可以设置一个默认值
    },
  ] as DescData[];
  const fileList = ref<FileItem[]>([file]);
  const uploadChange = (fileItemList: FileItem[], fileItem: FileItem) => {
    fileList.value = [fileItem];
  };
  const customRequest = (options: RequestOption) => {
    // docs: https://axios-http.com/docs/cancellation
    const controller = new AbortController();

    (async function requestWrap() {
      const {
        onProgress,
        onError,
        onSuccess,
        fileItem,
        name = 'file',
      } = options;
      onProgress(20);
      const formData = new FormData();
      formData.append(name as string, fileItem.file as Blob);
      const onUploadProgress = (event: ProgressEvent) => {
        let percent;
        if (event.total > 0) {
          percent = (event.loaded / event.total) * 100;
        }
        onProgress(parseInt(String(percent), 10), event);
      };

      try {
        const res = await FileControllerService.uploadFileUsingPost("user_avatar",formData.get("file"));
        if (res.code === 0){
          avatarUrl.value = res.data;
          console.log("url:" + avatarUrl.value)
          const resUpdate = await UserControllerService.updateMyAvatarUrlUsingPost(avatarUrl.value);
          if (resUpdate.code === 0){
            message.success("修改成功")
            window.location.reload()
          } else {
            message.error(res.message)
          }
        } else {
          message.error(res.message)
        }
        onSuccess(res);
      } catch (error) {
        onError(error);
      }
    })();
    return {
      abort() {
        controller.abort();
      },
    };
  };
</script>

<style scoped lang="less">
  .arco-card {
    padding: 14px 0 4px 4px;
    border-radius: 4px;
  }
  :deep(.arco-avatar-trigger-icon-button) {
    width: 32px;
    height: 32px;
    line-height: 32px;
    background-color: #e8f3ff;
    .arco-icon-camera {
      margin-top: 8px;
      color: rgb(var(--arcoblue-6));
      font-size: 14px;
    }
  }
</style>
