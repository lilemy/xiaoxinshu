import { updateQuestion } from '@/services/xiaoxinshu/questionController';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import { message, Modal } from 'antd';
import React from 'react';

interface Props {
  oldData?: API.Question;
  modalVisible: boolean;
  columns: ProColumns<API.Question>[];
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 更新题库
 *
 * @param fields
 */
const handleUpdate = async (fields: API.QuestionUpdateRequest) => {
  const hide = message.loading('正在更新');
  try {
    await updateQuestion(fields);
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
  const { oldData, modalVisible, columns, onSubmit, onCancel } = props;

  if (!oldData) {
    return <></>;
  }

  // 表单初始化值格式转换
  const initValues = { ...oldData };
  if (oldData.tags) {
    initValues.tags = JSON.parse(oldData.tags) || [];
  }

  return (
    <Modal title={'更新'} open={modalVisible} destroyOnClose footer={null} onCancel={onCancel}>
      <ProTable<API.QuestionUpdateRequest>
        // @ts-ignore
        columns={columns}
        form={{
          initialValues: initValues,
        }}
        type="form"
        onSubmit={async (values) => {
          const success = await handleUpdate({
            ...values,
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
