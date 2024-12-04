import {message, Modal} from "antd";
import {ProColumns, ProTable} from "@ant-design/pro-components";
import React from "react";
import {addUser} from "@/services/xiaoxinshu/userController";

interface Props {
  modalVisible: boolean;
  columns: ProColumns<API.User>[];
  onSubmit: () => void;
  onCancel: () => void;
}


/**
 * @zh-CN 添加用户
 * @param fields
 */
const handleAdd = async (fields: API.UserAddRequest) => {
  const hide = message.loading('正在添加');
  try {
    await addUser({
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
  const {columns, modalVisible, onCancel, onSubmit} = props;

  return (
    <Modal title={'新建'} open={modalVisible} destroyOnClose footer={null} onCancel={onCancel}>
      <ProTable<API.UserAddRequest>
        // @ts-ignore
        columns={columns}
        type="form"
        onSubmit={async (value) => {
          const success = await handleAdd(value);
          if (success) {
            onSubmit?.();
          }
        }}/>
    </Modal>
  )
};

export default CreatForm;
