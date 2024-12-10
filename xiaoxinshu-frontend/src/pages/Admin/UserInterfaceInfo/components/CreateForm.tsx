import { listInterfaceInfoByName } from '@/services/xiaoxinshu/interfaceInfoController';
import { listUserByUserAccount } from '@/services/xiaoxinshu/userController';
import { addUserInterfaceInfo } from '@/services/xiaoxinshu/userInterfaceInfoController';
import { ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { InputNumber, message, Modal } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

interface Props {
  modalVisible: boolean;
  onSubmit: () => void;
  onCancel: () => void;
}

/**
 * @zh-CN 添加用户接口信息
 * @param fields
 */
const handleAdd = async (fields: API.UserInterfaceInfoAddRequest) => {
  const hide = message.loading('正在添加');
  try {
    await addUserInterfaceInfo({
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
  const { modalVisible, onCancel, onSubmit } = props;
  const formRef = useRef<ProFormInstance>();
  const [loading, setLoading] = useState<boolean>(false);
  const [userList, setUserList] = useState<API.UserByUserAccount[]>([]);
  const [interfaceInfoList, setInterfaceInfoList] = useState<API.InterfaceInfoByName[]>([]);
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listUserByUserAccount();
      setUserList(res.data ?? []);
    } catch (e: any) {
      message.error('获取用户失败，' + e.message);
    }
    try {
      const res = await listInterfaceInfoByName();
      setInterfaceInfoList(res.data ?? []);
    } catch (e: any) {
      message.error('获取接口信息失败，' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadData();
  }, []);
  const userListForm = userList.map((user) => ({
    value: user.id,
    label: user.userAccount,
  }));
  const interfaceInfoListForm = interfaceInfoList.map((interfaceInfo) => ({
    value: interfaceInfo.id,
    label: interfaceInfo.name,
  }));
  return (
    <Modal
      title={'新建'}
      open={modalVisible}
      destroyOnClose
      footer={null}
      onCancel={onCancel}
      loading={loading}
    >
      <ProForm
        formRef={formRef}
        size="large"
        style={{ width: '95%', margin: 'auto' }}
        onFinish={async (value: API.UserInterfaceInfoAddRequest) => {
          const success = await handleAdd(value);
          if (success) {
            onSubmit?.();
          }
        }}
      >
        <ProFormSelect
          showSearch
          name="userId"
          label="用户 id："
          options={userListForm}
          rules={[{ required: true, message: '请输入用户 id' }]}
        />
        <ProFormSelect
          showSearch
          name="interfaceInfoId"
          label="接口 id："
          options={interfaceInfoListForm}
          rules={[{ required: true, message: '请输入接口 id' }]}
        />
        <ProFormText name="leftNum" label="调用次数：" initialValue={0}>
          <InputNumber />
        </ProFormText>
      </ProForm>
    </Modal>
  );
};

export default CreatForm;
