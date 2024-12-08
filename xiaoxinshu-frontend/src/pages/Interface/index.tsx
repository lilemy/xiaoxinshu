import { listInterfaceInfoVoByPage } from '@/services/xiaoxinshu/interfaceInfoController';
import { history, Link } from '@@/exports';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { Card, Space, Typography } from 'antd';
import React, { useRef } from 'react';

/**
 * 接口信息页面
 * @constructor
 */
const InterfaceInfo: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const columns: ProColumns<API.InterfaceInfoVO>[] = [
    {
      title: '接口名称',
      dataIndex: 'name',
      valueType: 'text',
      render: (_, record) => {
        return <Link to={`/interface/${record.id}`}>{record.name}</Link>;
      },
    },
    {
      title: '接口描述',
      dataIndex: 'description',
      valueType: 'text',
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: '100px',
      render: (_, record) => (
        <Space size="middle">
          <Typography.Link
            onClick={() => {
              const apiLink = `/interface/${record.id}`;
              history.push(apiLink);
            }}
          >
            接口详情
          </Typography.Link>
        </Space>
      ),
    },
  ];
  return (
    <div className="max-width-content">
      <Card title="在线接口调用">
        <ProTable<API.InterfaceInfoVO, API.PageInterfaceInfoVO>
          actionRef={actionRef}
          search={{
            labelWidth: 'auto',
            style: { margin: '0', paddingTop: '0', paddingBottom: '5px' },
          }}
          options={false}
          size={'large'}
          rowKey={(data) => {
            return data.id?.toString() || '';
          }}
          columns={columns}
          request={async (params, sort, filter) => {
            const sortField = Object.keys(sort)?.[0];
            const sortOrder = sort?.[sortField] ?? undefined;
            const { data, code } = await listInterfaceInfoVoByPage({
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
      </Card>
    </div>
  );
};

export default InterfaceInfo;
