import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  ModalForm,
  PageContainer,
  ProDescriptions, ProFormSelect,
  ProTable, TableDropdown,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Button, Drawer, message } from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import UpdateForm from './components/UpdateForm';

import TagsComponent from "@/components/Tags";
import {waitTime} from "@/pages/Question/QuestionSubmitManage";
import {useParams} from "react-router";
import {ProFormDigit} from "@ant-design/pro-form/lib";
import {
  addTestTrueOrFalse,
  deleteTestTrueOrFalse,
  searchTestTrueOrFalse,
  updateTestTrueOrFalse
} from "@/services/ant-design-pro/trueOrFalse";
import {getQuestionTestIdTitleVO, getTrueOrFalseTestIdTitleVO} from "@/services/ant-design-pro/test";




/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
const handleUpdate = async (fields: API.TrueOrFalseTestUpdateRequest) => {
  const hide = message.loading('修改中');
  try {
    await updateTestTrueOrFalse({
      id: fields.id,
      score: fields.score,
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
  const [currentRow, setCurrentRow] = useState<API.TrueOrFalseTestAdminVo>();
  const { id } = useParams();
  const [questionName, setQuestionName] = useState([]);

  useEffect(()=>{

  },[questionName])

    /**
     * @en-US Add node
     * @zh-CN 添加节点
     * @param fields
     */
    const handleAdd = async (fields: API.ChoiceQuestionTestAddRequest) => {
        const hide = message.loading('正在添加');
        try {
            console.log(fields)
            await addTestTrueOrFalse({
                ...fields,

            });
            console.log("fields:", fields)
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

  const columns: ProColumns<API.TrueOrFalseAdminVo>[] = [
    {
      title: 'ID',
      dataIndex: 'questionId',
      width: 100,
      ellipsis: true,
      align: 'center',
      search: false,
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
      search:false,
      width: 180,
    },
    {
      title: '标签',
      dataIndex: 'tags',
      width: 120,
      search:false,
      align: 'center',
      render: (_, record) => (
          <div>
            <TagsComponent tags={JSON.parse(record.tags as string)}/>
          </div>
      ),
    },
    {
      title: '答案',
      dataIndex: 'answer',
      copyable: true,
      align: 'center',
      search:false,
      editable:false,
      ellipsis: true,
      width: 60,
      valueType: 'select',
      valueEnum: {
        0: { text: '未填写', status: 'Warning' },
        1: { text: '正确', status: 'Success' },
        2: { text: '错误', status: 'Success' },
      },
    },
    {
      title: '分值',
      dataIndex: 'score',
      width: 80,
      search:false,
      align: 'center',
      editable:false,
      copyable: true,
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
          更新
        </a>,
        <a
          key="delete"
          onClick={async () => {
            console.log(record.id)
            const res = await deleteTestTrueOrFalse(record.id);
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
              }
            }}
            menus={[
              { key: 'copy', name: '复制' },
            ]}
        />,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.TrueOrFalseAdminVo, API.PageParams>
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
              getTrueOrFalseTestIdTitleVO().then(res => {
                  const data = []
                  // @ts-ignore
                  for (let i = 0; i < res.data.length; i++) {
                      // @ts-ignore
                      data.push({value: res.data[i].id, label: res.data[i].title})
                      // @ts-ignore

                  }
                  console.log(data,'data')

                  // @ts-ignore
                  setQuestionName([...data])

              })
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
              testId: id
            };
            // @ts-ignore
            const res = await searchTestTrueOrFalse(param);
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
        title={'添加题目'}
        width="400px"
        open={createModalOpen}
        onOpenChange={handleModalOpen}
        onFinish={async (value) => {
          const success = await handleAdd(value as API.TrueOrFalseTestAddRequest);
          if (success) {
            handleModalOpen(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormSelect
          label={"题目ID"}
          rules={[
            {
              required: true,
              message: '题目名称为必填项',
            },
          ]}
          width="md"
          name="id"
          options={questionName}
        />
        <ProFormDigit width="md" label={"分数"} name="score"/>
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
