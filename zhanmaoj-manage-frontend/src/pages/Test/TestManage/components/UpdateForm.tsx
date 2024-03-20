import {
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React from 'react';
import {ProFormDateTimePicker} from "@ant-design/pro-form";
import {ProFormSelect} from "@ant-design/pro-form/lib";
export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.QuestionAdminVo>;
export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalOpen: boolean;
  values: Partial<API.TestAdminVo>;
};

const statusType = [
  {
    value: 0,
    label: '公开',
  },
  {
    value: 1,
    label: '加密',
  },
];
const UpdateForm: React.FC<UpdateFormProps> = (props) => {
  return (
    <StepsForm
      stepsProps={{
        size: 'small',
      }}
      stepsFormRender={(dom, submitter) => {
        return (
          <Modal
            width={640}
            bodyStyle={{
              padding: '32px 40px 48px',
            }}
            destroyOnClose
            title={'详细信息'}
            open={props.updateModalOpen}
            footer={submitter}
            onCancel={() => {
              props.onCancel();
            }}
          >
            {dom}
          </Modal>
        );
      }}
      onFinish={props.onSubmit}
    >
      <StepsForm.StepForm
        initialValues={{
            id:props.values.id,
            title:props.values.title
        }}
        title={'基本信息'}
      >
          <ProFormText
              name="id"
              label={'题目ID'}
              width="md"
              rules={[
                  {
                      required: true,
                      message: '请勿修改',
                  },
              ]}
          />
          <ProFormText
              name="title"
              label={'题目标题'}
              width="md"
          />
      </StepsForm.StepForm>
      <StepsForm.StepForm
        initialValues={{
          status:props.values.status,
          tags: props.values.password,
          content:props.values.content
        }}
        title={'配置题目属性'}
      >
          <ProFormSelect
              name="status"
              width="md"
              label={'状态'}
              options={statusType}
          />
        <ProFormText.Password
          name="password"
          width="md"
          label={'密码'}
        />
          <ProFormTextArea
              name="content"
              width="md"
              label={'内容'}
              placeholder={'请输入至少五个字符'}
              rules={[
                  {
                      required: true,
                      message: '请输入至少五个字符的规则描述！',
                      min: 5,
                  },
              ]}
          />
      </StepsForm.StepForm>
      <StepsForm.StepForm
        initialValues={{
          beginTime: props.values.beginTime,
          expiredTime: props.values.expiredTime,
          examTime: props.values.examTime,
        }}
        title={'设定题目配置'}
      >
          <ProFormDateTimePicker
              name="beginTime"
              label="开始时间"
          />
          <ProFormDateTimePicker
              name="expiredTime"
              label="结束时间"
          />
          <ProFormText width="md" label={"做题时间"} name="examTime"/>
      </StepsForm.StepForm>
    </StepsForm>
  );
};
export default UpdateForm;
