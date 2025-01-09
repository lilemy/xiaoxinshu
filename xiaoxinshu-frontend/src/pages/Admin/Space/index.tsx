import UpdateForm from '@/pages/Admin/Space/components/UpdateForm';
import { listSpaceByPage } from '@/services/xiaoxinshu/spaceController';
import { formatSize } from '@/utils';
import {
  ActionType,
  PageContainer,
  ProColumns,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import { Space, Typography } from 'antd';
import React, { useRef, useState } from 'react';

const SpaceTableList: React.FC = () => {
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Note>();

  const columns: ProColumns<API.Space>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInForm: true,
    },
    {
      title: '空间名称',
      dataIndex: 'spaceName',
      valueType: 'text',
    },
    {
      title: '空间级别',
      dataIndex: 'spaceLevel',
      valueEnum: {
        0: {
          text: '普通版',
          status: 'Default',
        },
        1: {
          text: '专业版',
          status: 'Success',
        },
      },
    },
    {
      title: '使用情况',
      valueType: 'text',
      hideInSearch: true,
      hideInForm: true,
      render: (_, record) => {
        return (
          <ProDescriptions column={1} size="small">
            <ProDescriptions.Item label="大小">
              {formatSize(record.totalSize)} / {formatSize(record.maxSize)}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="数量">
              {record.totalCount} / {record.maxCount}
            </ProDescriptions.Item>
          </ProDescriptions>
        );
      },
      width: 200,
    },
    {
      title: '创建用户',
      dataIndex: 'userId',
      valueType: 'text',
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
      title: '编辑时间',
      sorter: true,
      dataIndex: 'editTime',
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
        </Space>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.Space, API.PageSpace>
        headerTitle={'空间信息'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        defaultSize="small"
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listSpaceByPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.SpaceQueryRequest);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
        pagination={{
          pageSize: 10, // 每页显示 10 条数据
        }}
        columns={columns}
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

export default SpaceTableList;
