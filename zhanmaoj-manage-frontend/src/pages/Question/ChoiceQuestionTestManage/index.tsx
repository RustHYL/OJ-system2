import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  ModalForm,
  PageContainer,
  ProDescriptions,
  ProFormText,
  ProTable, TableDropdown,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Button, Drawer, message } from 'antd';
import React, { useRef, useState } from 'react';
import UpdateForm from './components/UpdateForm';


import TagsComponent from "@/components/Tags";
import {
  addChoiceQuestionsBackend,
  deleteChoiceQuestionInfo,
  searchChoiceQuestions, updateChoiceQuestionInfo
} from "@/services/ant-design-pro/choiceQuestion";
import {waitTime} from "@/pages/Question/ChoiceQuestionSubmitManage";


/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: API.ChoiceQuestionAdminVo) => {
  const hide = message.loading('正在添加');
  try {
    await addChoiceQuestionsBackend({
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
const handleUpdate = async (fields: API.ChoiceQuestionAdminVo) => {
  const hide = message.loading('修改中');
  try {
    await updateChoiceQuestionInfo({
      id: fields.id,
      title: fields.title,
      tags: fields.tags,
      answer: fields.answer,
      optionA: fields.optionA,
      optionB: fields.optionB,
      optionC: fields.optionC,
      optionD: fields.optionD,
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
  const [currentRow, setCurrentRow] = useState<API.ChoiceQuestionAdminVo>();
  const tagsTooltipContent = <>{`["示例1", "示例2"]`}</>;
  const columns: ProColumns<API.ChoiceQuestionAdminVo>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
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
      title: '题目',
      dataIndex: 'title',
      copyable: true,
      align: 'center',
      width: 90,
      ellipsis: true,
    },
    {
      title: '选项A',
      dataIndex: 'optionA',
      copyable: true,
      align: 'center',
      width: 70,
      ellipsis: true,
      search:false,
    },
    {
      title: '选项B',
      dataIndex: 'optionB',
      copyable: true,
      align: 'center',
      width: 70,
      ellipsis: true,
      search:false,
    },
    {
      title: '选项C',
      dataIndex: 'optionC',
      copyable: true,
      align: 'center',
      width: 70,
      ellipsis: true,
      search:false,
    },
    {
      title: '选项D',
      dataIndex: 'optionD',
      copyable: true,
      align: 'center',
      width: 70,
      ellipsis: true,
      search:false,
    },
    {
        title: '答案',
        dataIndex: 'answer',
        copyable: true,
        align: 'center',
        width: 60,
        ellipsis: true,
        valueType: 'select',
        search:false,
        valueEnum: {
            0: { text: '未填写', status: 'Warning' },
            1: { text: 'A', status: 'Success' },
            2: { text: 'B', status: 'Success' },
            3: { text: 'C', status: 'Success' },
            4: { text: 'D', status: 'Success' },
        },
    },
    {
      title: '用户ID',
      dataIndex: 'userId',
      width: 60,
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
      width: 90,
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
                const res = await deleteChoiceQuestionInfo(record.id);
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
      <ProTable<API.ChoiceQuestionAdminVo, API.PageParams>
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
            const res = await searchChoiceQuestions(param);
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
          const success = await handleAdd(value as API.ChoiceQuestionAdminVo);
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
        <ProFormText width="md" label={"标签"} name="tags" tooltip={tagsTooltipContent}/>
        <ProFormText width="md" label={"选项A"} name="optionA"/>
        <ProFormText width="md" label={"选项B"} name="optionB"/>
        <ProFormText width="md" label={"选项C"} name="optionC"/>
        <ProFormText width="md" label={"选项D"} name="optionD"/>
        <ProFormText
            name="answer"
            width="md"
            label={'题目答案'}
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
          <ProDescriptions<API.ChoiceQuestionAdminVo>
            column={2}
            title={currentRow?.title}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.id
            }}
            columns={columns as ProDescriptionsItemProps<API.ChoiceQuestionAdminVo>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};
export default TableList;
