import {
  ProFormText,
  ProFormTextArea,
  StepsForm,
} from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React from 'react';
import {ProFormDigit} from "@ant-design/pro-form/lib";
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
  values: Partial<API.QuestionAdminVo>;
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
            title={'更新信息'}
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
            title: props.values.title,
        }}
        title={'基本信息'}
      >
          <ProFormText
            readonly={true}
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
        <ProFormDigit
          name="score"
          label={'题目分值'}
          width="md"
          rules={[
            {
              required: true,
              message: '请输入分值！',
            },
          ]}
        />
      </StepsForm.StepForm>
    </StepsForm>
  );
};
export default UpdateForm;
