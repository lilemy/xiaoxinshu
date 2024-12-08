import CreateForm from '@/pages/Admin/InterfaceInfo/components/CreateForm';
import UpdateForm from '@/pages/Admin/InterfaceInfo/components/UpdateForm';
import {
  deleteInterfaceInfo,
  listInterfaceInfoByPage,
  offlineInterface,
  onlineInterface,
} from '@/services/xiaoxinshu/interfaceInfoController';
import { PlusOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Space, Typography } from 'antd';
import React, { useRef, useState } from 'react';

/**
 * 接口信息管理
 * @constructor
 */
const InterfaceInfoTableList: React.FC = () => {
  const actionRef = useRef<ActionType>();
  // 是否显示新建窗口
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  // 当前接口信息的数据
  const [currentRow, setCurrentRow] = useState<API.InterfaceInfo>();
  /**
   * @zh-CN 删除接口信息
   *
   * @param interfaceInfo
   */
  const handleDelete = async (interfaceInfo: API.InterfaceInfo) => {
    const hide = message.loading('正在删除');
    if (!interfaceInfo) return true;
    try {
      await deleteInterfaceInfo({
        id: interfaceInfo.id,
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

  /**
   * 发布接口
   *
   * @param record
   */
  const handleOnline = async (record: API.InterfaceOnlineRequest) => {
    // 显示正在发布的加载提示
    const hide = message.loading('发布中');
    // 如果接口数据为空，直接返回true
    if (!record) return true;
    try {
      // 调用发布接口的POST请求方法
      await onlineInterface({
        // 传递接口的id参数
        id: record.id,
      });
      hide();
      // 显示操作成功的提示信息
      message.success('操作成功');
      // 重新加载数据
      actionRef.current?.reload();
      // 返回true表示发布成功
      return true;
    } catch (error: any) {
      hide();
      // 显示操作失败的错误提示信息
      message.error('操作失败，' + error.message);
      // 返回false表示发布失败
      return false;
    }
  };

  /**
   * 下线接口
   *
   * @param record
   */
  const handleOffline = async (record: API.InterfaceOfflineRequest) => {
    const hide = message.loading('下线中');
    if (!record) return true;
    try {
      await offlineInterface({
        id: record.id,
      });
      hide();
      message.success('操作成功');
      actionRef.current?.reload();
      return true;
    } catch (error: any) {
      hide();
      message.error('操作失败，' + error.message);
      return false;
    }
  };

  const columns: ProColumns<API.InterfaceInfo>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInForm: true,
      width: 160,
    },
    {
      title: '名称',
      dataIndex: 'name',
      valueType: 'text',
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
    },
    {
      title: '接口地址',
      dataIndex: 'url',
      valueType: 'text',
    },
    {
      title: '实际接口地址',
      dataIndex: 'actualUrl',
      valueType: 'text',
    },
    {
      title: '接口路径',
      dataIndex: 'path',
      valueType: 'text',
    },
    {
      title: '请求参数',
      dataIndex: 'requestParams',
      valueType: 'jsonCode',
    },
    {
      title: '请求头',
      dataIndex: 'requestHeader',
      valueType: 'jsonCode',
    },
    {
      title: '响应头',
      dataIndex: 'responseHeader',
      valueType: 'jsonCode',
    },
    {
      title: '接口状态',
      dataIndex: 'status',
      valueEnum: {
        0: {
          text: '关闭',
          status: 'Default',
        },
        1: {
          text: '开放',
          status: 'Success',
        },
      },
      width: 100,
      hideInForm: true,
    },
    {
      title: '请求类型',
      dataIndex: 'method',
      valueType: 'text',
    },
    {
      title: '创建人',
      dataIndex: 'userId',
      valueType: 'text',
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
            修改
          </Typography.Link>
          {record.status === 0 ? (
            <Typography.Link onClick={() => handleOnline(record)}>发布</Typography.Link>
          ) : null}
          {record.status === 1 ? (
            <Typography.Link type="danger" onClick={() => handleOffline(record)}>
              下线
            </Typography.Link>
          ) : null}
          <Typography.Link type="danger" onClick={() => handleDelete(record)}>
            删除
          </Typography.Link>
        </Space>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.InterfaceInfo, API.PageInterfaceInfo>
        headerTitle="接口信息"
        actionRef={actionRef}
        columns={columns}
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
          const { data, code } = await listInterfaceInfoByPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.InterfaceInfoQueryRequest);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
      />
      <CreateForm
        modalVisible={createModalVisible}
        columns={columns}
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

export default InterfaceInfoTableList;
