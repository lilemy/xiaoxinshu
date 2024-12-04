import { updateQuestionBank } from '@/services/xiaoxinshu/questionBankController';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import { message, Modal } from 'antd';
import React from 'react';

interface Props {
  oldData?: API.QuestionBank;
  modalVisible: boolean;
  columns: ProColumns<API.QuestionBank>[];
  bankUrl: string;
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 更新题库
 *
 * @param fields
 */
const handleUpdate = async (fields: API.QuestionBankUpdateRequest) => {
  const hide = message.loading('正在更新');
  try {
    await updateQuestionBank(fields);
    hide();
    message.success('更新成功');
    return true;
  } catch (error: any) {
    hide();
    message.error('更新失败' + error.message);
    return false;
  }
};

const UpdateForm: React.FC<Props> = (props) => {
  const { oldData, modalVisible, columns, bankUrl, onSubmit, onCancel } = props;

  if (!oldData) {
    return <></>;
  }

  return (
    <Modal title={'更新'} open={modalVisible} destroyOnClose footer={null} onCancel={onCancel}>
      <ProTable<API.QuestionBankUpdateRequest>
        columns={columns}
        form={{
          initialValues: oldData,
        }}
        type="form"
        onSubmit={async (values: API.QuestionBankUpdateRequest) => {
          console.log(bankUrl);
          const success = await handleUpdate({
            ...values,
            picture: bankUrl,
            id: oldData?.id,
          });
          if (success) {
            onSubmit?.();
          }
        }}
      />
    </Modal>
  );
};

export default UpdateForm;
