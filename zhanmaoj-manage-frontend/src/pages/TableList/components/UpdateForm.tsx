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
            title: props.values.title,
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
          label={'题目名称'}
          width="md"
          rules={[
            {
              required: true,
              message: '请输入题目名称！',
            },
          ]}
        />
      </StepsForm.StepForm>
      <StepsForm.StepForm
        initialValues={{
          tags: props.values.tags,
          content:props.values.content
        }}
        title={'配置题目属性'}
      >
        <ProFormTextArea
          name="tags"
          width="md"
          label={'标签'}
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
          judgeConfig: props.values.judgeConfig,
          judgeCase: props.values.judgeCase,
        }}
        title={'设定题目配置'}
      >
          <ProFormTextArea
              name="judgeCase"
              width="md"
              label={'判题示例'}
          />
          <ProFormTextArea
              name="judgeConfig"
              width="md"
              label={'判题配置'}
          />
      </StepsForm.StepForm>
    </StepsForm>
  );
};
export default UpdateForm;
