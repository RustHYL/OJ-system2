import {
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React from 'react';
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
  values: Partial<API.TrueOrFalseAdminVo>;
};

const answerType = [
  {
    value: 0,
    label: '未填写',
  },
  {
    value: 1,
    label: '正确',
  },
  {
    value: 2,
    label: '错误',
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
      </StepsForm.StepForm>
      <StepsForm.StepForm
        initialValues={{
          tags: props.values.tags,
          title:props.values.title
        }}
        title={'配置题目属性'}
      >
        <ProFormTextArea
          name="tags"
          width="md"
          label={'标签'}
        />
          <ProFormTextArea
              name="title"
              width="md"
              label={'题目'}
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
          answer: props.values.answer,
        }}
        title={'设定题目配置'}
      >
          <ProFormSelect
              name="answer"
              width="md"
              label={'答案'}
              options={answerType}
          />
      </StepsForm.StepForm>
    </StepsForm>
  );
};
export default UpdateForm;
