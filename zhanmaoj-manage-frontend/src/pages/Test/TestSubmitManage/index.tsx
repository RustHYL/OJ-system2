import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import {message} from 'antd';
import { useRef } from 'react';
import {deleteTestSubmitInfo, searchTestSubmits, updateTestSubmitInfo} from "@/services/ant-design-pro/testSubmit";
import ignore from "ignore";
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


const columns: ProColumns<API.TestSubmitAdminVo>[] = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 48,
    ellipsis: true,
    fixed: 'left',
    editable: false,
  },
  {
      title: '状态',
      dataIndex: 'status',
      copyable: true,
      align: 'center',
      valueType: 'select',
      width: 90,
      valueEnum: {
          0: {
              text: '待判题',
              status: 'Loading',
          },
          1: {
              text: '判题中',
              status: 'Normal',
          },
          2: {
            text: '成功',
            status: 'Success',
          },
          3: {
            text: '失败',
            status: 'Error',
          },
      },
  },
  {
      title: '分数',
      dataIndex: 'score',
      copyable: true,
      align: 'center',
      width: 110,
      search:false,
  },
  {
      title: '试卷ID',
      dataIndex: 'testId',
      width: 80,
      ellipsis: true,
      copyable: true,
      editable: false,
  },
  {
    title: '用户ID',
    dataIndex: 'userId',
    width: 80,
    ellipsis: true,
    copyable: true,
    editable: false,
  },
  {
    title: '开始做卷时间',
    key: 'beginTime',
    dataIndex: 'beginTime',
    valueType: 'date',
    align: 'center',
    width: 100,
    editable: false,
    search: false,
  },
  {
    title: '结束做卷时间',
    key: 'endTime',
    dataIndex: 'endTime',
    valueType: 'date',
    align: 'center',
    width: 100,
    editable: false,
    search: false,
  },
  {
    title: '注册时间',
    key: 'createTime',
    dataIndex: 'createTime',
    valueType: 'date',
    align: 'center',
    width: 100,
    editable: false,
  },
  {
    title: '更新时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
    valueType: 'date',
    align: 'center',
    width: 100,
    editable: false,
    search: false,
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
            // @ts-ignore
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
                  const res = await deleteTestSubmitInfo(record.id);
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

  const saveData = async (key: any, row: API.TestSubmitAdminVo) => {
    const res = await updateTestSubmitInfo(row);
    if (res.code === 0) {
      message.success('保存成功');
      return true;
    } else {
      message.error('保存失败');
      return false;
    }
  };


  return (
      <ProTable<API.TestSubmitAdminVo>
          columns={columns}
          actionRef={actionRef}
          cardBordered
          request={async (params = {}, sort, filter) => {
            console.log(sort, filter);
            await waitTime(1000);

            const param = {
                testSubmitAdminVo: {
                    ...params
                },
                pageVO: {
                    size: params.pageSize,
                    current: params.current
                }
            };
            const res = await searchTestSubmits(param?.testSubmitAdminVo);
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
