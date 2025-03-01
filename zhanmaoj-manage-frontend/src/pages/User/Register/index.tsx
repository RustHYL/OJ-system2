import { Footer } from '@/components';
import {register} from '@/services/ant-design-pro/api';
import {
  LockOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormText,
} from '@ant-design/pro-components';
import { Helmet, history } from '@umijs/max';
import { Tabs, message } from 'antd';
import { createStyles } from 'antd-style';
import React, { useState } from 'react';
import Settings from '../../../../config/defaultSettings';
import { SYSTEM_LOGO } from '@/constants';
const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
          "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});
// const Lang = () => {
//   return;
// };

const Register: React.FC = () => {
  const [type, setType] = useState<string>('account');
  const { styles } = useStyles();

    const handleSubmit = async (values: API.RegisterParams) => {
        const {userPassword,checkPassword} = values;

        //校验
        if (userPassword !== checkPassword){
            message.error('两次输入密码不一致');
            return;
        }

        try {
            // 注册
            const res = await register(values);
            if (res.code === 0) {
                const defaultRegisterSuccessMessage = '注册成功！';
                message.success(defaultRegisterSuccessMessage);

                /** 此方法会跳转到 redirect 参数所在的位置 */
                if (!history) return;
                const query = history.location;
                history.push({
                    pathname: '/user/login',
                    // @ts-ignore
                    query,
                });
                return;
            }
        } catch (error: any) {
            const defaultRegisterFailureMessage = '注册失败，请重试！';
            message.error(defaultRegisterFailureMessage);
        }

    };
  return (
      <div className={styles.container}>
        <Helmet>
          <title>
            {'注册'}- {Settings.title}
          </title>
        </Helmet>
        {/*<Lang />*/}
        <div
            style={{
              flex: '1',
              padding: '32px 0',
            }}
        >
          <LoginForm
              contentStyle={{
                minWidth: 280,
                maxWidth: '75vw',
              }}
              logo={<img alt="logo" src={SYSTEM_LOGO} />}
              title="用户中心"
              subTitle={'最好的程序员学习交友平台'}
              submitter={{
                  searchConfig: {
                      submitText: '注册'
                  }
              }}
              initialValues={{
                autoRegister: true,
              }}
              onFinish={async (values) => {
                await handleSubmit(values as API.RegisterParams);
              }}
          >
            <Tabs
                activeKey={type}
                onChange={setType}
                centered
                items={[
                  {
                    key: 'account',
                    label: '用户注册',
                  },
                ]}
            />
            {type === 'account' && (
                <>
                  <ProFormText
                      name="userAccount"
                      fieldProps={{
                        size: 'large',
                        prefix: <UserOutlined />,
                      }}
                      placeholder={'请输入账号'}
                      rules={[
                          {
                              required: true,
                              message: '账号是必填项！',
                          },
                          {
                              min: 4,
                              type: 'string',
                              message: '账号长度不能小于4位！',
                          },
                      ]}
                  />
                  <ProFormText.Password
                      name="userPassword"
                      fieldProps={{
                        size: 'large',
                        prefix: <LockOutlined />,
                      }}
                      placeholder={'请输入密码'}
                      rules={[
                        {
                          required: true,
                          message: '密码是必填项！',
                        },
                        {
                          min: 8,
                          type: 'string',
                          message: '密码长度不能小于8位！',
                        },
                      ]}
                  />
                    <ProFormText.Password
                        name="checkPassword"
                        fieldProps={{
                            size: 'large',
                            prefix: <LockOutlined />,
                        }}
                        placeholder={'请确认密码'}
                        rules={[
                            {
                                required: true,
                                message: '确认密码是必填项！',
                            },
                            {
                                min: 8,
                                type: 'string',
                                message: '确认密码长度不能小于8位！',
                            },
                        ]}
                    />
                </>
            )}
            <div
                style={{
                  marginBottom: 24,
                }}
            >
            </div>
          </LoginForm>
        </div>
        <Footer />
      </div>
  );
};
export default Register;
