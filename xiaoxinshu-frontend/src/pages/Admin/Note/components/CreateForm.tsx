import { ProColumns, ProTable } from '@ant-design/pro-components';
import { message, Modal } from 'antd';
import React from 'react';
import { addNote } from '@/services/xiaoxinshu/noteController';

interface Props {
  modalVisible: boolean;
  columns: ProColumns<API.Note>[];
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 添加笔记
 * @param fields
 */
const handleAdd = async (fields: API.NoteAddRequest) => {
  const hide = message.loading('正在添加');
  try {
    await addNote({
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
      <ProTable<API.NoteAddRequest>
        // @ts-ignore
        columns={columns}
        type="form"
        onSubmit={async (value) => {
          const success = await handleAdd(value);
          if (success) {
            onSubmit?.();
          }
        }}
      />
    </Modal>
  );
};

export default CreatForm;
