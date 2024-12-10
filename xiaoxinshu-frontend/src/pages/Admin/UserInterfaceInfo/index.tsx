import CreateForm from '@/pages/Admin/UserInterfaceInfo/components/CreateForm';
import UpdateForm from '@/pages/Admin/UserInterfaceInfo/components/UpdateForm';
import {
  deleteUserInterfaceInfo,
  listUserInterfaceInfoByPage,
} from '@/services/xiaoxinshu/userInterfaceInfoController';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Space, Typography } from 'antd';
import React, { useRef, useState } from 'react';
import { PlusOutlined } from '@ant-design/icons';

const UserInterfaceInfoTableList: React.FC = () => {
  const actionRef = useRef<ActionType>();
  // 是否显示新建窗口
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  // 当前接口信息的数据
  const [currentRow, setCurrentRow] = useState<API.UserInterfaceInfo>();
  /**
   * @zh-CN 删除用户接口信息
   *
   * @param userInterfaceInfo
   */
  const handleDelete = async (userInterfaceInfo: API.UserInterfaceInfo) => {
    const hide = message.loading('正在删除');
    if (!userInterfaceInfo) return true;
    try {
      await deleteUserInterfaceInfo({
        id: userInterfaceInfo.id,
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
  const columns: ProColumns<API.UserInterfaceInfo>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInForm: true,
      width: 160,
    },
    {
      title: '用户 id',
      dataIndex: 'userId',
      valueType: 'text',
      width: 160,
      hideInForm: true,
    },
    {
      title: '接口 id',
      dataIndex: 'interfaceInfoId',
      valueType: 'text',
      width: 160,
      hideInForm: true,
    },
    {
      title: '剩余调用次数',
      sorter: true,
      dataIndex: 'leftNum',
      valueType: 'text',
      hideInSearch: true,
    },
    {
      title: '调用次数',
      sorter: true,
      dataIndex: 'totalNum',
      valueType: 'text',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        0: {
          text: '正常',
          status: 'Success',
        },
        1: {
          text: '禁用',
          status: 'Error',
        },
      },
      width: 100,
      hideInForm: true,
    },
    {
      title: '创建时间',
      sorter: true,
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '更新时间',
      sorter: true,
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => (
        <Space size="middle">
          <Typography.Link
            onClick={() => {
              setCurrentRow(record);
              setUpdateModalVisible(true);
            }}
          >
            修改调用次数
          </Typography.Link>
          <Typography.Link type="danger" onClick={() => handleDelete(record)}>
            删除
          </Typography.Link>
        </Space>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.UserInterfaceInfo, API.PageUserInterfaceInfo>
        headerTitle="用户接口关系"
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
              setCreateModalVisible(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listUserInterfaceInfoByPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.UserInterfaceInfoQueryRequest);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
        columns={columns}
      />
      <CreateForm
        modalVisible={createModalVisible}
        onSubmit={() => {
          setCreateModalVisible(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setCreateModalVisible(false);
        }}
      />
      <UpdateForm
        modalVisible={updateModalVisible}
        columns={columns}
        oldData={currentRow}
        onSubmit={() => {
          setUpdateModalVisible(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setUpdateModalVisible(false);
        }}
      />
    </PageContainer>
  );
};

export default UserInterfaceInfoTableList;
