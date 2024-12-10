import { updateUserInterfaceInfoLeftNum } from '@/services/xiaoxinshu/userInterfaceInfoController';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import { message, Modal } from 'antd';
import React from 'react';

interface Props {
  oldData?: API.UserInterfaceInfo;
  modalVisible: boolean;
  columns: ProColumns<API.UserInterfaceInfo>[];
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 更新用户接口信息
 *
 * @param fields
 */
const handleUpdate = async (fields: API.UserInterfaceInfoUpdateRequest) => {
  const hide = message.loading('正在更新');
  try {
    await updateUserInterfaceInfoLeftNum({
      ...fields,
    });
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

  return (
    <Modal
      title={`更新调用次数：${oldData.id}`}
      open={modalVisible}
      destroyOnClose
      footer={null}
      onCancel={onCancel}
    >
      <ProTable<API.UserInterfaceInfo>
        columns={columns}
        form={{
          initialValues: oldData,
        }}
        type="form"
        onSubmit={async (values: API.UserInterfaceInfoUpdateRequest) => {
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
