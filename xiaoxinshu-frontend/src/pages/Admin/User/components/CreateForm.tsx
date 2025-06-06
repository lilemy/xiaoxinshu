import { ProColumns, ProTable } from '@ant-design/pro-components';
import { message, Modal } from 'antd';
import { createUser } from '@/services/xiaoxinshu-backend/userController';
import React from 'react';

interface Props {
  modalVisible: boolean;
  columns: ProColumns<API.User>[];
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 创建用户
 * @param fields
 */
const handleCreate = async (fields: API.UserCreateRequest) => {
  const hide = message.loading('正在创建');
  try {
    await createUser({
      ...fields,
    });
    hide();
    message.success('创建成功');
    return true;
  } catch (error: any) {
    hide();
    message.error('创建失败，' + error.message);
    return false;
  }
};

const CreatForm: React.FC<Props> = (props) => {
  const { columns, modalVisible, onCancel, onSubmit } = props;

  return (
    <Modal title={'新建'} open={modalVisible} destroyOnClose footer={null} onCancel={onCancel}>
      <ProTable<API.User, API.PageUser>
        columns={columns}
        type="form"
        onSubmit={async (value) => {
          const success = await handleCreate(value as API.UserCreateRequest);
          if (success) {
            onSubmit?.();
          }
        }}
      />
    </Modal>
  );
};

export default CreatForm;
