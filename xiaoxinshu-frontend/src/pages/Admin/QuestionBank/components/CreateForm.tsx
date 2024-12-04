import { addQuestionBank } from '@/services/xiaoxinshu/questionBankController';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import { message, Modal } from 'antd';
import React from 'react';

interface Props {
  modalVisible: boolean;
  columns: ProColumns<API.QuestionBank>[];
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 添加题库
 * @param fields
 */
const handleAdd = async (fields: API.QuestionBankAddRequest) => {
  const hide = message.loading('正在添加');
  try {
    await addQuestionBank({
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
      <ProTable<API.QuestionBankAddRequest>
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
