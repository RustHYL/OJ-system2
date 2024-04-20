import {PlusOutlined} from '@ant-design/icons';
import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  ModalForm,
  PageContainer,
  ProDescriptions,
  ProFormText,
  ProFormTextArea,
  ProTable, TableDropdown,
} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Drawer, message} from 'antd';
import React, { useRef, useState } from 'react';
import UpdateForm from './components/UpdateForm';
import {addTest, deleteTestInfo, searchTests, updateTestInfo} from "@/services/ant-design-pro/test";
import {ProFormDigit, ProFormSelect} from "@ant-design/pro-form/lib";
import {ProFormDateTimePicker} from "@ant-design/pro-form";
import {waitTime} from "@/pages/Test/TestSubmitManage";
import {history} from "@umijs/max";



const handleAdd = async (fields: API.TestAdminVo) => {
  const hide = message.loading('正在添加');
  try {
    await addTest({
       ...fields,
    });
    hide();
    message.success('添加成功');
    return true;
  } catch (error) {
    console.log(error);
    hide();
    message.error('添加失败,请重新添加！');
    return false;
  }
};

/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
const handleUpdate = async (fields: API.TestAdminVo) => {
  const hide = message.loading('修改中');
  try {
    await updateTestInfo({
      id: fields.id,
      title: fields.title,
      status: fields.status,
      password: fields.password,
      content: fields.content,
      beginTime: fields.beginTime,
      examTime: fields.examTime,
      expiredTime: fields.expiredTime,
    });
    hide();
    message.success('修改成功');
    return true;
  } catch (error) {
    hide();
    message.error('修改失败！');
    return false;
  }
};



const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.TestAdminVo>();
  const columns: ProColumns<API.TestAdminVo>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
      ellipsis: true,
      align: 'center',
      copyable: true,
      editable: false,
      fixed: 'left',
      tip: 'The rule name is the unique key',
      render: (dom, entity) => {
        return (
          <a
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
            }}
          >
            {dom}
          </a>
        );
      },
    },
    {
      title: '标题',
      dataIndex: 'title',
      width: 80,
      align: 'center',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 60,
      align: 'center',
      valueType: 'select',
      ellipsis: true,
      filters: true,
      onFilter: true,
      valueEnum: {
        0: { text: '公开', status: 'Default' },
        1: { text: '加密', status: 'Error' },
      },
    },
    {
      title: '密码',
      dataIndex: 'password',
      width: 80,
      align: 'center',
      search:false,
      copyable: true,
      ellipsis: true,
    },
    {
      title: '内容',
      dataIndex: 'content',
      copyable: true,
      align: 'center',
      ellipsis: true,
      width: 80
    },
    {
      title: '题目数',
      dataIndex: 'questionNum',
      width: 80,
      search:false,
      align: 'center',
      editable:false,
      copyable: true,
    },
    {
      title: '开始时间',
      dataIndex: 'beginTime',
      valueType: 'dateTime',
      width: 90,
      search:false,
      align: 'center',
      copyable: true,
    },
    {
      title: '结束时间',
      dataIndex: 'expiredTime',
      valueType: 'dateTime',
      width: 90,
      search:false,
      align: 'center',
      copyable: true,
    },
    {
      title: '答题时间/分钟',
      dataIndex: 'examTime',
      width: 70,
      search:false,
      align: 'center',
      copyable: true,
    },
    {
      title: '总分',
      dataIndex: 'totalScore',
      width: 70,
      search:false,
      editable:false,
      align: 'center',
      copyable: true,
    },
    {
      title: '创建用户ID',
      dataIndex: 'userId',
      width: 80,
      align: 'center',
      editable:false,
      copyable: true,
      ellipsis: true,
    },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      valueType: 'date',
      align: 'center',
      editable:false,
      ellipsis: true,
      width: 70 ,
    },
    {
      title: '更新时间',
      key: 'updateTime',
      dataIndex: 'updateTime',
      valueType: 'date',
      align: 'center',
      editable:false,
      ellipsis: true,
      width: 70,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      fixed: 'right',
      width: 90,
      render: (text, record, _, action) => [
          <a
            key="config"
            onClick={() => {
              handleUpdateModalOpen(true);
              setCurrentRow(record);
            }}
        >
          更新
        </a>,
        // <Link
        //     key="detail"
        //     to={{
        //       pathname: '/test/detail', // 指定目标页面的路径
        //       search: `?id=${record.id}`, // 将record.id作为查询参数传递
        //     }}
        // >
        //   详情
        // </Link>,
        <a
            key="delete"
            onClick={async () => {
              const res = await deleteTestInfo(record.id);
              if (res.code === 0) {
                message.success('删除成功');
                action?.reload();
              } else {
                message.error('删除失败');
              }
            }}
        >
          删除
        </a>,
        <TableDropdown
            key="actionGroup"
            onSelect={async key => {
              if (key === 'copy') {
                await navigator.clipboard.writeText(JSON.stringify(record));
                message.success('复制成功');
              } else if (key === 'trueOrFalse') {
                history.push(`/test/detail/trueOrFalse/${record.id}`)
              } else if (key === 'choiceQuestion') {
                history.push(`/test/detail/choiceQuestion/${record.id}`)
              } else if (key === 'question') {
                history.push(`/test/detail/question/${record.id}`)
              }
            }}
            menus={[
              { key: 'copy', name: '复制' },
              { key: 'trueOrFalse', name: '判断题' },
              { key: 'choiceQuestion', name: '选择题' },
              { key: 'question', name: '编程题' },
            ]}
        />,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.TestAdminVo, API.PageParams>
        headerTitle={'题目信息'}
        actionRef={actionRef}
        rowKey="key"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        columnsState={{
          persistenceKey: 'pro-table-singe-demos',
          persistenceType: 'localStorage',
          onChange(value) {
            console.log('value: ', value);
          },
        }}
        request={
          async (params = {}, sort, filter) => {
            console.log(sort, filter);
            await waitTime(1000);
            const param = {
              ...params,
            };
            // @ts-ignore
            const res = await searchTests(param);
            return {
              data: res.data,
            }
          }
        }
        pagination={{
          pageSize: 5,
          onChange: (page) => console.log(page),
        }}
        columns={columns}
      />
      <ModalForm
        title={'新建题目'}
        width="400px"
        open={createModalOpen}
        onOpenChange={handleModalOpen}
        onFinish={async (value) => {
          const success = await handleAdd(value as API.TestAddRequest);
          if (success) {
            handleModalOpen(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText width="md" label={"标题"} name="title"/>
        <ProFormSelect
            width="md"
            label={"状态"}
            valueEnum={{
              0: '公开',
              1: '加密',
            }}
            rules={[{ required: true, message: 'Please select status!' }]}
            name="status"/>
        <ProFormText.Password width="md" label={"密码"} name="password"/>
        <ProFormTextArea width="md" label={"简介"} name="content"/>
        <ProFormDateTimePicker
            name="beginTime"
            label="开始时间"
        />
        <ProFormDateTimePicker
            name="expiredTime"
            label="结束时间"
        />
        <ProFormDigit
          label="做题时间"
          name="examTIme"
          width="sm"
          min={0}
          max={1000}
        />
        <ProFormDigit
            label="判断题数"
            name="trueOrFalseNum"
            width="sm"
            min={0}
            max={1000}
        />
        <ProFormDigit
            label="判断题单题分数"
            name="trueOrFalsePerScore"
            width="sm"
            min={0}
            max={20}
        />
        <ProFormDigit
            label="选择题数"
            name="choiceQuestionNum"
            width="sm"
            min={0}
            max={1000}
        />
        <ProFormDigit
            label="选择题单题分数"
            name="choiceQuestionPerScore"
            width="sm"
            min={0}
            max={20}
        />
        <ProFormTextArea
            name="questionList"
            width="md"
            tooltip={"[  \n" +
              "  {\"id\": 1730853831375450113, \"score\": 20},\n" +
              "  {\"id\": 1730853941824057345, \"score\": 30}\n" +
              "]"}
            label={'编程题'}
        />
      </ModalForm>
      <UpdateForm
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        updateModalOpen={updateModalOpen}
        values={currentRow || {}}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.id&& (
          <ProDescriptions<API.TrueOrFalseAdminVo>
            column={2}
            title={currentRow?.title}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.id
            }}
            columns={columns as ProDescriptionsItemProps<API.TrueOrFalseAdminVo>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default TableList;
