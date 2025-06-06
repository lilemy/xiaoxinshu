import React, { useRef, useState } from 'react';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { deleteUser, listUserByPage } from '@/services/xiaoxinshu-backend/userController';
import { Button, message, Space, Typography } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import CreateForm from '@/pages/Admin/User/components/CreateForm';
import UpdateForm from '@/pages/Admin/User/components/UpdateForm';

const UserTableList: React.FC = () => {
  // 新建窗口的弹窗
  const [createModalOpen, handleCreateModalOpen] = useState<boolean>(false);
  // 更新窗口的弹窗
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  // 当前选中用户
  const [currentRow, setCurrentRow] = useState<API.User>();

  /**Add commentMore actions
   * @zh-CN 删除用户
   *
   * @param user
   */
  const handleDelete = async (user: API.User) => {
    const hide = message.loading('正在删除');
    if (!user.id) return true;
    try {
      await deleteUser({
        id: user.id,
      });
      hide();
      message.success('删除成功');
      actionRef?.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('删除失败' + error.message);
      return false;
    }
  };

  const columns: ProColumns<API.User>[] = [
    {
      title: '账号',
      dataIndex: 'userAccount',
      valueType: 'text',
    },
    {
      title: '用户名',
      dataIndex: 'userName',
      valueType: 'text',
    },
    {
      title: '头像',
      dataIndex: 'userAvatar',
      valueType: 'image',
      fieldProps: {
        width: 64,
      },
      hideInSearch: true,
      width: 80,
    },
    {
      title: '简介',
      dataIndex: 'userProfile',
      valueType: 'textarea',
    },
    {
      title: '手机号码',
      dataIndex: 'userPhone',
      valueType: 'text',
    },
    {
      title: '邮箱',
      dataIndex: 'userEmail',
      valueType: 'text',
    },
    {
      title: '性别',
      dataIndex: 'userGender',
      valueEnum: {
        0: {
          text: '男',
          status: 'Success',
        },
        1: {
          text: '女',
          status: 'Warning',
        },
      },
      width: 100,
    },
    {
      title: '用户生日',
      sorter: true,
      dataIndex: 'userBirthday',
      valueType: 'date',
      hideInSearch: true,
    },
    {
      title: '权限',
      dataIndex: 'userRole',
      valueEnum: {
        user: {
          text: '用户',
          status: 'Default',
        },
        admin: {
          text: '管理员',
          status: 'Success',
        },
      },
      width: 100,
    },
    {
      title: '创建时间',
      sorter: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
      width: 150,
    },
    {
      title: '更新时间',
      sorter: true,
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
      width: 150,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 100,
      fixed: 'right',
      render: (_, record) => (
        <Space size={'middle'}>
          <Typography.Link
            key="config"
            onClick={() => {
              handleUpdateModalOpen(true);
              setCurrentRow(record);
            }}
          >
            修改
          </Typography.Link>
          <Typography.Link
            type="danger"
            onClick={() => {
              handleDelete(record).then();
            }}
          >
            删除
          </Typography.Link>
        </Space>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.User, API.PageUser>
        headerTitle={'用户信息'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        scroll={{ x: 1200 }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleCreateModalOpen(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listUserByPage({
            ...params,
            userQueryRequest: {
              ...filter,
              sortField,
              sortOrder,
            },
          } as API.listUserByPageParams);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
        columns={columns}
      />
      <CreateForm
        modalVisible={createModalOpen}
        columns={columns}
        onSubmit={() => {
          handleCreateModalOpen(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          handleCreateModalOpen(false);
        }}
      />
      <UpdateForm
        modalVisible={updateModalOpen}
        columns={columns}
        oldData={currentRow}
        onSubmit={() => {
          handleUpdateModalOpen(false);
          setCurrentRow(undefined);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
        }}
      />
    </PageContainer>
  );
};
export default UserTableList;
