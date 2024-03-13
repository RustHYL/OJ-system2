// import type { ActionType, ProColumns } from '@ant-design/pro-components';
// import { ProTable, TableDropdown } from '@ant-design/pro-components';
// import { message } from 'antd';
// import {useRef, useState} from 'react';
// import {deleteQuestionInfo, searchQuestions, updateQuestionInfo} from "@/services/ant-design-pro/question";
// import TagsComponent from "@/components/Tags";
//
// export const waitTimePromise = async (time: number = 10) => {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       resolve(true);
//     }, time);
//   });
// };
//
// export const waitTime = async (time: number = 10) => {
//   await waitTimePromise(time);
// };
//
//
// const columns: ProColumns<API.QuestionAdminVo>[] = [
//     {
//       title: 'ID',
//       dataIndex: 'id',
//       width: 100,
//       ellipsis: true,
//       align: 'center',
//       fixed: 'left',
//       copyable: true,
//       editable:false,
//     },
//     {
//         title: '标题',
//         dataIndex: 'title',
//         copyable: true,
//         align: 'center',
//         width: 100
//     },
//     {
//       title: '标签',
//       dataIndex: 'tags',
//       width: 120,
//       align: 'center',
//       search:false,
//       render: (_, record) => (
//           <div>
//               <TagsComponent tags={JSON.parse(record.tags as string)}/>
//           </div>
//       ),
//     },
//     {
//         title: '用户ID',
//         dataIndex: 'userId',
//         width: 120,
//         align: 'center',
//         editable:false,
//         copyable: true,
//         ellipsis: true,
//     },
//     {
//         title: '提交数',
//         dataIndex: 'submitNum',
//         copyable: true,
//         align: 'center',
//         search:false,
//         editable:false,
//         width: 60
//     },
//     {
//         title: '通过数',
//         dataIndex: 'acceptedNum',
//         width: 60,
//         search:false,
//         align: 'center',
//         editable:false,
//         copyable: true,
//     },
//     {
//         title: '点赞数',
//         dataIndex: 'thumbNum',
//         width: 60 ,
//         search:false,
//         align: 'center',
//         editable:false,
//         copyable: true,
//     },
//     {
//         title: '收藏数',
//         dataIndex: 'favourNum',
//         align: 'center',
//         search:false,
//         editable:false,
//         copyable: true,
//         width: 60
//     },
//     {
//         title: '创建时间',
//         key: 'createTime',
//         dataIndex: 'createTime',
//         valueType: 'date',
//         align: 'center',
//         editable:false,
//         width: 90,
//     },
//     {
//         title: '更新时间',
//         key: 'updateTime',
//         dataIndex: 'updateTime',
//         valueType: 'date',
//         align: 'center',
//         editable:false,
//         width: 90,
//     },
//   {
//     title: '操作',
//     valueType: 'option',
//     key: 'option',
//     fixed: 'right',
//     width: 100,
//     render: (text, record, _, action) => [
//       <a
//           key="editable"
//           onClick={() => {
//             // @ts-ignore
//             action?.startEditable?.(record.id);
//           }}
//       >
//         编辑
//       </a>,
//       <a
//         key="details"
//         onClick={() => {
//
//         }}
//       >
//         详情
//       </a>,
//       <TableDropdown
//           key="actionGroup"
//           onSelect={async key => {
//               if (key === 'copy') {
//                   await navigator.clipboard.writeText(JSON.stringify(record));
//                   message.success('复制成功');
//               } else if (key === 'delete') {
//                 console.log(record)
//                   const res = await deleteQuestionInfo(record.id);
//                   if (res.code === 0) {
//                       message.success('删除成功');
//                       action?.reload();
//                   } else {
//                       message.error('删除失败');
//                   }
//               }
//           }}
//           menus={[
//               { key: 'copy', name: '复制' },
//               { key: 'delete', name: '删除' }
//           ]}
//       />,
//     ],
//   },
// ];
//
// export default () => {
//   const actionRef = useRef<ActionType>();
//
//   const saveData = async (key: any, row: API.QuestionAdminVo) => {
//       const res = await updateQuestionInfo(row);
//     if (res.code === 0) {
//       message.success('保存成功');
//       return true;
//     } else {
//       message.error('保存失败');
//       return false;
//     }
//   };
//
//
//   return (
//       <ProTable<API.QuestionAdminVo>
//           columns={columns}
//           actionRef={actionRef}
//           cardBordered
//           request={async (params = {}, sort, filter) => {
//             console.log(sort, filter);
//             await waitTime(1000);
//             const param = {
//                 questionAdminVO: {
//                     ...params
//                 },
//                 pageVO: {
//                     size: params.pageSize,
//                     current: params.current
//                 }
//             };
//             // @ts-ignore
//               const res = await searchQuestions(param);
//             return {
//               data: res.data
//             }
//           }}
//           editable={{
//             type: 'multiple',
//             onSave: saveData,
//           }}
//           columnsState={{
//             persistenceKey: 'pro-table-singe-demos',
//             persistenceType: 'localStorage',
//             onChange(value) {
//               console.log('value: ', value);
//             },
//           }}
//           rowKey="id"
//           search={{
//             // labelWidth: 'auto',
//           }}
//           options={{
//             setting: {
//               listsHeight: 400,
//             },
//           }}
//           form={{
//             // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
//             syncToUrl: (values, type) => {
//               if (type === 'get') {
//                 return {
//                   ...values,
//                   created_at: [values.startTime, values.endTime],
//                 };
//               }
//               return values;
//             },
//           }}
//           pagination={{
//             pageSize: 5,
//             onChange: (page) => console.log(page),
//           }}
//           dateFormatter="string"
//           headerTitle="题目信息"
//       />
//   );
// };
