import {UploadOutlined} from '@ant-design/icons';
import {ProForm, ProFormSelect, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {useModel, useRequest} from '@umijs/max';
import {Button, message, Upload} from 'antd';
import React, {useEffect, useRef} from 'react';
import useStyles from './index.style';
import {queryCurrent} from "@/pages/User/settings/service";
import {updateAvatarUrl, updateFile, updateMyInfo} from "@/services/ant-design-pro/user";
import {ProFormInstance} from "@ant-design/pro-form/lib";


const BaseView: React.FC = () => {
  const {styles} = useStyles();

  const { initialState } = useModel('@@initialState');
  const formRef = useRef<ProFormInstance>();


  // 头像组件 方便以后独立，增加裁剪之类的功能
  const AvatarView = ({userAvatar}: { userAvatar: string }) => (
      <>
        <div className={styles.avatar_title}>头像</div>
        <div className={styles.avatar}>
          <img src={userAvatar} alt="userAvatar"/>
        </div>
        <Upload
            name="avatar"
            showUploadList={false}
            onChange={handleFileChange} // 处理文件变化
        >
          <div className={styles.button_view}>
            <Button>
              <UploadOutlined/>
              更换头像
            </Button>
          </div>
        </Upload>
      </>
  );


    // 当文件选择变化时调用
    const handleFileChange = async (info: any) => {
        const file = info.file.originFileObj;
        if (file) {
            try {
                const response = await updateFile(file, 'user_avatar');
                if (response.code === 0) {
                  // @ts-ignore
                    const updateRes = await updateAvatarUrl(response.data);
                  if (updateRes.code === 0) {
                      message.success('头像上传成功');
                      window.location.reload();
                      // 这里可以设置新头像的URL，或者刷新页面以显示新头像
                  }else {
                      message.error('头像上传失败：' + updateRes.message);
                  }
                } else {
                    message.error('头像上传失败：' + response.message);
                }
            } catch (error) {
                message.error('头像上传失败：' + error.message);
            }
        } else {
            message.error('请先选择一个文件');
            return;
        }
    };

  const { data: currentUser, loading } = useRequest(async () => {
    const { currentUser } = initialState || {};
    if (currentUser) {
      return currentUser;
    }
    return currentUser;
  });


  const getAvatarURL = () => {
    const { initialState } = useModel('@@initialState');
    const { currentUser } = initialState || {};
    if (currentUser) {
      if (currentUser.userAvatar) {
        return currentUser.userAvatar;
      }
      const url = 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png';
      return url;
    }
    return '';
  };

  //回显
  useEffect(() => {
    queryCurrent().then(res => {
      formRef?.current?.setFieldsValue(
          {userName:res.data.userName,
            userProfile:res.data.userProfile,
            gender:res.data.gender,
            email:res.data.email});
      console.log(res)
    });
  })

  const handleFinish = async (
    initialValues: API.UserAdminVo
  ) => {
    const res = await updateMyInfo(initialValues);
    if (res.code === 0) {
      message.success('更新基本信息成功');
      window.location.reload();
      return;
    } else {
      message.error('更新基本信息失败:' + res.message);
    }
  };
  return (
      <div className={styles.baseView}>
        {currentUser ? null : (
            <>
              <div className={styles.left}>
                <ProForm
                    formRef={formRef}
                    layout="vertical"
                    onFinish={handleFinish}
                    submitter={{
                      searchConfig: {
                        submitText: '更新基本信息',
                      },
                      render: (_, dom) => dom[1],
                    }}
                    initialValues={{
                      ...currentUser,
                    }}
                >
                  <ProFormText
                      width="md"
                      name="userName"
                      label="昵称"
                  />
                  <ProFormTextArea
                      name="userProfile"
                      label="个人简介"
                      placeholder="个人简介"
                  />
                  <ProFormSelect
                      width="sm"
                      name="gender"
                      label="性别"
                      options={[
                        {
                          label: '男',
                          value: 0,
                        },
                        {
                          label: '女',
                          value: 1,
                        },
                      ]}
                  />
                  <ProFormText
                    width="md"
                    name="email"
                    label="邮箱"
                  />
                </ProForm>
              </div>
              <div className={styles.right}>
                <AvatarView userAvatar={getAvatarURL()}/>
              </div>
            </>
        )}
      </div>
  );
};
export default BaseView;
