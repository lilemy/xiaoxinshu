import {ProColumns, ProTable} from "@ant-design/pro-components";
import {message, Modal} from "antd";
import React from "react";
import {updateUser} from "@/services/xiaoxinshu/userController";


interface Props {
  oldData?: API.User;
  modalVisible: boolean;
  columns: ProColumns<API.User>[];
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 更新用户
 *
 * @param fields
 */
const handleUpdate = async (fields: API.UserUpdateRequest) => {
  const hide = message.loading('正在更新');
  try {
    await updateUser(fields);
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
  const {oldData, modalVisible, columns, onSubmit, onCancel,} = props;

  if (!oldData) {
    return <></>;
  }

  return (
    <Modal
      title={'更新'}
      open={modalVisible}
      destroyOnClose
      footer={null}
      onCancel={onCancel}>
      <ProTable<API.UserUpdateRequest>
        columns={columns}
        form={{
          initialValues: oldData,
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
        }}/>
    </Modal>
  );
}

export default UpdateForm;
