import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import {Image, message, Tag} from 'antd';
import { useRef } from 'react';
import {deleteUserInfo, searchUsers, updateUserInfo} from "@/services/ant-design-pro/user";
export const waitTimePromise = async (time: number = 10) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 10) => {
  await waitTimePromise(time);
};


const columns: ProColumns<API.UserAdminVo>[] = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 48,
    ellipsis: true,
    fixed: 'left',
  },
  {
    title: '用户头像',
    dataIndex: 'userAvatar',
    width: 100,
    search: false,
    render: (_, record) => (
        <div>
          <Image src={record.userAvatar} width={80} height={80} />
        </div>
    ),
  },
    {
        title: '用户名',
        dataIndex: 'userName',
        copyable: true,
        align: 'center',
        width: 90
    },
    {
        title: '用户账号',
        dataIndex: 'userAccount',
        copyable: true,
        align: 'center',
        width: 110
    },
    {
        title: '用户密码',
        dataIndex: 'userPassword',
        width: 80,
        ellipsis: true,
    },
  {
    title: '性别',
    filters: true,
    onFilter: true,
    dataIndex: 'gender',
    valueType: 'select',
    width: 70 ,
    valueEnum: {
      0: {
        text: '男',
        status: 'Default',
      },
      1: {
        text: '女',
        status: 'Processing',
      },
    },
  },
  {
    title: '电话',
    dataIndex: 'phone',
    align: 'center',
    copyable: true,
    ellipsis: true,
    width: 100
  },
  {
    title: '邮件',
    dataIndex: 'email',
    copyable: true,
    align: 'center',
    width: 100,
      ellipsis: true,
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    copyable: true,
    align: 'center',
    width: 120,
    ellipsis: true,
  },
  {
    dataIndex: 'userRole',
    title: '用户角色',
    align: 'center',
    width: 120,
    valueType: 'select',
    filters: true,
    onFilter: true,
    valueEnum: {
      "user": { text: <Tag color="success">普通用户</Tag>, status: 'Success' },
      "admin": { text: <Tag color="warning">管理员</Tag>, status: 'Default' },
      "super": { text: <Tag color="error">超级管理员</Tag>, status: 'Error' },
    },
  },
  {
    title: '注册时间',
    key: 'createTime',
    dataIndex: 'createTime',
    valueType: 'date',
    align: 'center',
    width: 100,
  },
  {
    title: '更新时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    valueType: 'date',
    align: 'center',
    width: 100,
  },
  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    fixed: 'right',
    width: 100,
    render: (text, record, _, action) => [
      <a
          key="editable"
          onClick={() => {
            action?.startEditable?.(record.id);
          }}
      >
        编辑
      </a>,
      <TableDropdown
          key="actionGroup"
          onSelect={async key => {
              if (key === 'copy') {
                  await navigator.clipboard.writeText(JSON.stringify(record));
                  message.success('复制成功');
              } else if (key === 'delete') {
                console.log(record)
                  const res = await deleteUserInfo(record.id);
                  if (res.code === 0) {
                      message.success('删除成功');
                      action?.reload();
                  } else {
                      message.error('删除失败');
                  }
              }
          }}
          menus={[
              { key: 'copy', name: '复制' },
              { key: 'delete', name: '删除' }
          ]}
      />,
    ],
  },
];

export default () => {
  const actionRef = useRef<ActionType>();

  const saveData = async (key: any, row: API.UserAdminVo) => {
    const res = await updateUserInfo(row);
    if (res.code === 0) {
      message.success('保存成功');
      return true;
    } else {
      message.error('保存失败');
      return false;
    }
  };


  return (
      <ProTable<API.UserAdminVo>
          columns={columns}
          actionRef={actionRef}
          cardBordered
          request={async (params = {}, sort, filter) => {
            console.log(sort, filter);
            await waitTime(1000);
            const param = {
                userAdminVO: {
                    ...params
                },
                pageVO: {
                    size: params.pageSize,
                    current: params.current
                }
            };
            const res = await searchUsers(param);
            return {
              data: res.data
            }
          }}
          editable={{
            type: 'multiple',
            onSave: saveData,
          }}
          columnsState={{
            persistenceKey: 'pro-table-singe-demos',
            persistenceType: 'localStorage',
            onChange(value) {
              console.log('value: ', value);
            },
          }}
          rowKey="id"
          search={{
            // labelWidth: 'auto',
          }}
          options={{
            setting: {
              listsHeight: 400,
            },
          }}
          form={{
            // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
            syncToUrl: (values, type) => {
              if (type === 'get') {
                return {
                  ...values,
                  created_at: [values.startTime, values.endTime],
                };
              }
              return values;
            },
          }}
          pagination={{
            pageSize: 5,
            onChange: (page) => console.log(page),
          }}
          dateFormatter="string"
          headerTitle="高级表格"
      />
  );
};
