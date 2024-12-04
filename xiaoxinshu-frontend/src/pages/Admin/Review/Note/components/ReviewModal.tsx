import MdViewer from '@/components/Markdown/MdViewer';
import { ProDescriptions, ProFormSelect, ProFormTextArea } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { message, Modal } from 'antd';
import Title from 'antd/es/typography/Title';
import React, { useRef } from 'react';
import { reviewNote } from '@/services/xiaoxinshu/noteController';

interface Props {
  oldData?: API.Note;
  modalVisible: boolean;
  onSubmit: () => void;
  onCancel: () => void;
}

const ReviewModal: React.FC<Props> = (props) => {
  const { oldData, modalVisible, onSubmit, onCancel } = props;
  const formRef = useRef<ProFormInstance>();
  if (!oldData) {
    return <></>;
  }
  const onSubmitFrom = async (value: API.Note) => {
    try {
      await reviewNote({
        id: oldData.id,
        reviewStatus: value.reviewStatus,
        reviewMessage: value.reviewMessage,
      });
      message.success('审核成功');
      onSubmit?.();
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('审核失败：', e);
    }
  };
  return (
    <Modal open={modalVisible} onCancel={onCancel} width={1000} footer={null}>
      <Title level={4}>笔记审核：{oldData.title}</Title>
      <ProDescriptions column={1}>
        <ProDescriptions.Item label="id" copyable={true}>
          {oldData.id}
        </ProDescriptions.Item>
        <ProDescriptions.Item label="标题">{oldData.title}</ProDescriptions.Item>
        <ProDescriptions.Item label="标签">{oldData.tags}</ProDescriptions.Item>
        <ProDescriptions.Item label="创建用户">{oldData.userId}</ProDescriptions.Item>
      </ProDescriptions>
      <br />
      <ProDescriptions column={1} layout="vertical">
        <ProDescriptions.Item label="内容">
          <MdViewer value={oldData.content} />
        </ProDescriptions.Item>
      </ProDescriptions>
      <br />
      <ProDescriptions column={1}>
        <ProDescriptions.Item label="编辑时间" valueType="dateTime">
          {oldData.editTime}
        </ProDescriptions.Item>
        <ProDescriptions.Item label="更新时间" valueType="dateTime">
          {oldData.updateTime}
        </ProDescriptions.Item>
        <ProDescriptions.Item label="创建时间" valueType="dateTime">
          {oldData.createTime}
        </ProDescriptions.Item>
      </ProDescriptions>
      <br />
      <ProForm
        formRef={formRef}
        style={{ maxWidth: 600 }}
        onFinish={async (value) => {
          await onSubmitFrom(value);
        }}
      >
        <ProFormSelect
          style={{ maxWidth: '50%' }}
          rules={[{ required: true }]}
          label="审核状态："
          name="reviewStatus"
          options={[
            {
              label: '审核通过',
              value: '1',
            },
            {
              label: '审核不通过',
              value: '2',
            },
          ]}
        />
        <ProFormTextArea
          tooltip="审核信息为空时，填写默认信息"
          label="审核信息："
          name="reviewMessage"
        />
      </ProForm>
    </Modal>
  );
};

export default ReviewModal;
