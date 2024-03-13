import { PlusOutlined } from '@ant-design/icons';
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
import { Button, Drawer, message } from 'antd';
import React, { useRef, useState } from 'react';
import UpdateForm from './components/UpdateForm';
import {
  addQuestionsBackend,
  deleteQuestionInfo,
  searchQuestions,
  updateQuestionInfo
} from "@/services/ant-design-pro/question";

import TagsComponent from "@/components/Tags";
import {waitTime} from "@/pages/Test/TestManage";


/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: API.QuestionAdminVo) => {
  const hide = message.loading('正在添加');
  try {
    await addQuestionsBackend({
       ...fields,
    });
    hide();
    message.success('Added successfully');
    return true;
  } catch (error) {
    console.log(error);
    hide();
    message.error('Adding failed, please try again!');
    return false;
  }
};

/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
const handleUpdate = async (fields: API.QuestionAdminVo) => {
  const hide = message.loading('Configuring');
  try {
    await updateQuestionInfo({
      id: fields.id,
      title: fields.title,
      content: fields.content,
      tags: fields.tags,
      answer: fields.answer,
      submitNum: fields.submitNum,
      acceptedNum: fields.acceptedNum,
      judgeCase: fields.judgeCase,
      judgeConfig:fields.judgeConfig,
      thumbNum: fields.thumbNum,
      favourNum: fields.favourNum,
      userId: fields.userId,
    });
    hide();
    message.success('Configuration is successful');
    return true;
  } catch (error) {
    hide();
    message.error('Configuration failed, please try again!');
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
  const [currentRow, setCurrentRow] = useState<API.QuestionAdminVo>();
  const tagsTooltipContent = <>{`["示例1", "示例2"]`}</>;
  const judgeCaseTooltipContent = <>{`[{"input":"1 2","output":"3"},{"input":"2 3","output":"5"}]`}</>;
  const judgeConfigTooltipContent = <>{`{"timeLimit":333,"memoryLimit":333,"stackLimit":333}`}</>;
  const columns: ProColumns<API.QuestionAdminVo>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 100,
      ellipsis: true,
      align: 'center',
      copyable: true,
      editable: false,
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
      copyable: true,
      align: 'center',
      width: 100,
      // valueType: 'textarea',
    },
    {
      title: '标签',
      dataIndex: 'tags',
      width: 120,
      align: 'center',
      search:false,
      render: (_, record) => (
          <div>
            <TagsComponent tags={JSON.parse(record.tags as string)}/>
          </div>
      ),
    },
    {
      title: '用户ID',
      dataIndex: 'userId',
      width: 120,
      align: 'center',
      editable:false,
      copyable: true,
      ellipsis: true,
    },
    {
      title: '提交数',
      dataIndex: 'submitNum',
      copyable: true,
      align: 'center',
      search:false,
      editable:false,
      width: 60
    },
    {
      title: '通过数',
      dataIndex: 'acceptedNum',
      width: 60,
      search:false,
      align: 'center',
      editable:false,
      copyable: true,
    },
    {
      title: '点赞数',
      dataIndex: 'thumbNum',
      width: 60 ,
      search:false,
      align: 'center',
      editable:false,
      copyable: true,
    },
    {
      title: '收藏数',
      dataIndex: 'favourNum',
      align: 'center',
      search:false,
      editable:false,
      copyable: true,
      width: 60
    },
    {
      title: '创建时间',
      key: 'createTime',
      dataIndex: 'createTime',
      valueType: 'date',
      align: 'center',
      editable:false,
      width: 90,
    },
    {
      title: '更新时间',
      key: 'updateTime',
      dataIndex: 'updateTime',
      valueType: 'date',
      align: 'center',
      editable:false,
      width: 90,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      fixed: 'right',
      width: 100,
      render: (text, record, _, action) => [
          <a
            key="config"
            onClick={() => {
              handleUpdateModalOpen(true);
              setCurrentRow(record);
            }}
        >
          详情
        </a>,
        <TableDropdown
            key="actionGroup"
            onSelect={async key => {
              if (key === 'copy') {
                await navigator.clipboard.writeText(JSON.stringify(record));
                message.success('复制成功');
              } else if (key === 'delete') {
                console.log(record)
                const res = await deleteQuestionInfo(record.id);
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

  return (
    <PageContainer>
      <ProTable<API.QuestionAdminVo, API.PageParams>
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
            const res = await searchQuestions(param);
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
          const success = await handleAdd(value as API.QuestionAdminVo);
          if (success) {
            handleModalOpen(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText
          label={"题目名称"}
          rules={[
            {
              required: true,
              message: '题目名称为必填项',
            },
          ]}
          width="md"
          name="title"
        />
        <ProFormTextArea width="md" label={"题目内容"} name="content"/>
        <ProFormText width="md" label={"标签"} name="tags" tooltip={tagsTooltipContent}/>
        <ProFormTextArea
            name="answer"
            width="md"
            label={'题目答案'}
        />
        <ProFormTextArea
            name="judgeCase"
            width="md"
            tooltip={judgeCaseTooltipContent}
            label={'判题示例'}
        />
        <ProFormTextArea
            name="judgeConfig"
            width="md"
            tooltip={judgeConfigTooltipContent}
            label={'题目配置'}
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
          <ProDescriptions<API.QuestionAdminVo>
            column={2}
            title={currentRow?.title}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.id
            }}
            columns={columns as ProDescriptionsItemProps<API.QuestionAdminVo>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default TableList;
