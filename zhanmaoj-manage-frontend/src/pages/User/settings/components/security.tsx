import {Input, List, message, Modal} from 'antd';
import React, {useEffect, useState} from 'react';
import {queryCurrent} from "@/pages/User/settings/service";
import {EyeInvisibleOutlined, EyeTwoTone} from '@ant-design/icons';

type Unpacked<T> = T extends (infer U)[] ? U : T;

const passwordStrength = {
  strong: <span className="strong">强</span>,
  medium: <span className="medium">中</span>,
  weak: <span className="weak">弱 Weak</span>,
};


// const [currentUser, setCurrentUser] = useState<API.CurrentUser | null>(null);


// const fetchCurrentUser = async () => {
//   try {
//     // 模拟从API获取数据
//     const data = await queryCurrent();
//     setCurrentUser(data.data);
//   } catch (error) {
//     console.error('Error fetching current user:', error);
//   }
// };
//
// // 在组件挂载后获取当前用户信息
// useEffect(() => {
//   fetchCurrentUser();
// }, []); // 空数组表示这个effect只在组件挂载时运行一次


const SecurityView: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const getData = () => [
    {
      title: '账户密码',
      description: (
        <>
          当前密码强度：
          {passwordStrength.strong}
        </>
      ),
      actions: [<a key="Modify" onClick={openBox}>修改</a>],
    },
  ];

  const passwordData = {
      password: '',
      editPassword: '',
      checkPassword: '',
  };
  const openBox = () => {
    setIsModalOpen(true);
    message.info('修改密码');
  };

    const handleOk = () => {
      console.log()
        setIsModalOpen(false);
    };

    const handleCancel = () => {
        setIsModalOpen(false);
    };

  const data = getData();
  return (
    <>
      <Modal title="Basic Modal" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
          <Input.Password
              placeholder="原密码"
              name="password"
              iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
          />
          <Input.Password
              placeholder="修改密码"
              name="editPassword"
              iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
          />
          <Input.Password
              placeholder="确认密码"
              name="checkPassword"
              iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
          />
      </Modal>
      <List<Unpacked<typeof data>>
        itemLayout="horizontal"
        dataSource={data}
        renderItem={(item) => (
          <List.Item actions={item.actions}>
            <List.Item.Meta title={item.title} description={item.description} />
          </List.Item>
        )}
      />
    </>
  );
};

export default SecurityView;
