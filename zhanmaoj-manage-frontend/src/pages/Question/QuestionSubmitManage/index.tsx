import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import {message} from 'antd';
import { useRef } from 'react';
import JudgeInfoComponent from "@/components/DateProcessing/JudgeInfo";
import {
    deleteQuestionSubmitInfo,
    searchQuestionSubmits,
    updateQuestionSubmitInfo
} from "@/services/ant-design-pro/questionSubmit";
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


const columns: ProColumns<API.QuestionSubmitAdminVo>[] = [
    {
        title: 'ID',
        dataIndex: 'id',
        width: 100,
        ellipsis: true,
        align: 'center',
        fixed: 'left',
        copyable: true,
        editable:false,
    },
    {
        title: '编程语言',
        dataIndex: 'language',
        copyable: true,
        align: 'center',
        editable:false,
        width: 70
    },
    {
        title: '用户代码',
        key: 'code',
        dataIndex: 'code',
        width: 80,
        align: 'center',
        search: false,
        editable:false,
        copyable: true,
        ellipsis: true,
    },
    {
        title: '判题结果信息',
        dataIndex: 'judgeInfo',
        copyable: true,
        align: 'center',
        search:false,
        width: 140,
        render: (_, record) => (
            <div>
                <JudgeInfoComponent judgeInfo={JSON.parse(record.judgeInfo as string)}/>
            </div>
        ),
    },
    {
        title: '判题状态',
        dataIndex: 'status',
        width: 100,
        align: 'center',
        valueType: 'select',
        filters: true,
        onFilter: true,
        valueEnum: {
            0: { text: '待判题', status: 'Default' },
            1: { text: '判题中', status: 'Processing' },
            2: { text: '判题成功', status: 'Success' },
            3: { text: '判题失败', status: 'Error' },
        },
    },
    {
        title: '题目ID',
        dataIndex: 'questionId',
        width: 90 ,
        align: 'center',
        editable:false,
        ellipsis: true,
        copyable: true,
    },
    {
        title: '用户ID',
        dataIndex: 'userId',
        align: 'center',
        editable:false,
        ellipsis: true,
        copyable: true,
        width: 90
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
        width: 80,
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
                        const res = await deleteQuestionSubmitInfo(record.id);
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

    const saveData = async (key: any, row: API.QuestionAdminVo) => {
        const res = await updateQuestionSubmitInfo(row);
        if (res.code === 0) {
            message.success('保存成功');
            return true;
        } else {
            message.error('保存失败');
            return false;
        }
    };


    return (
        <ProTable<API.QuestionAdminVo>
            columns={columns}
            actionRef={actionRef}
            cardBordered
            request={async (params = {}, sort, filter) => {
                console.log(sort, filter);
                await waitTime(1000);
                const param = {
                    ...params
                };
                const res = await searchQuestionSubmits(param);
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
            headerTitle="题目信息"
        />
    );
};
