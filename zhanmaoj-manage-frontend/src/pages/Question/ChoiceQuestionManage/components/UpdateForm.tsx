import {
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React from 'react';
export type FormValueType = {
  target?: string;
  template?: string;
  type?: string;
  time?: string;
  frequency?: string;
} & Partial<API.ChoiceQuestionAdminVo>;
export type UpdateFormProps = {
  onCancel: (flag?: boolean, formVals?: FormValueType) => void;
  onSubmit: (values: FormValueType) => Promise<void>;
  updateModalOpen: boolean;
  values: Partial<API.ChoiceQuestionAdminVo>;
};
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
            content: props.values.content,
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
          name="content"
          label={'题目'}
          width="md"
          rules={[
            {
              required: true,
              message: '请输入题目！',
            },
          ]}
        />
      </StepsForm.StepForm>
      <StepsForm.StepForm
        initialValues={{
          tags: props.values.tags,
        }}
        title={'配置题目属性'}
      >
        <ProFormTextArea
          name="tags"
          width="md"
          label={'标签'}
        />
      </StepsForm.StepForm>
      <StepsForm.StepForm
        initialValues={{
          answer: props.values.answer,
          optionA: props.values.optionA,
          optionB: props.values.optionB,
          optionC: props.values.optionC,
          optionD: props.values.optionD,
        }}
        title={'设定题目配置'}
      >
          <ProFormText
              name="answer"
              width="md"
              label={'答案'}
          />
          <ProFormText
              name="optionA"
              width="md"
              label={'选项A'}
          />
          <ProFormText
              name="optionB"
              width="md"
              label={'选项B'}
          />
          <ProFormText
              name="optionC"
              width="md"
              label={'选项C'}
          />
          <ProFormText
              name="optionD"
              width="md"
              label={'选项D'}
          />
      </StepsForm.StepForm>
    </StepsForm>
  );
};
export default UpdateForm;
