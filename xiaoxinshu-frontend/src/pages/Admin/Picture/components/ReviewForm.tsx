import { doPictureReview } from '@/services/xiaoxinshu/pictureController';
import {
  ProColumns,
  ProDescriptions,
  ProFormSelect,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { message, Modal } from 'antd';
import React, { useRef } from 'react';

interface Props {
  oldData?: API.Picture;
  modalVisible: boolean;
  columns: ProColumns<API.Picture>[];
  onSubmit: () => void;
  onCancel: () => void;
}

const UpdateForm: React.FC<Props> = (props) => {
  const { oldData, modalVisible, columns, onSubmit, onCancel } = props;
  const formRef = useRef<ProFormInstance>();
  if (!oldData) {
    return <></>;
  }
  const onSubmitFrom = async (value: API.Picture) => {
    try {
      await doPictureReview({
        id: oldData.id,
        reviewStatus: value.reviewStatus,
        reviewMessage: value.reviewMessage,
      });
      message.success('审核成功');
      onSubmit?.();
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('审核失败：' + e.message);
    }
  };
  return (
    <Modal title={'审核'} open={modalVisible} destroyOnClose footer={null} onCancel={onCancel}>
      <ProDescriptions<API.Picture> column={1} dataSource={oldData} columns={columns as any} />
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

export default UpdateForm;
