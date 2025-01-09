import TagList from '@/components/TagList';
import ReviewForm from '@/pages/Admin/Picture/components/ReviewForm';
import { deletePicture, listPictureByPage } from '@/services/xiaoxinshu/pictureController';
import { formatSize } from '@/utils';
import { PlusOutlined } from '@ant-design/icons';
import {
  ActionType,
  PageContainer,
  ProColumns,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import { history } from '@umijs/max';
import { Button, message, Popconfirm, Space, Typography } from 'antd';
import React, { useRef, useState } from 'react';

const PictureTableList: React.FC = () => {
  // 是否显示更新窗口
  const [reviewModalVisible, setReviewModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Picture>();

  /**
   * @zh-CN 删除图片
   *
   * @param picture
   */
  const handleDelete = async (picture: API.Picture) => {
    const hide = message.loading('正在删除');
    if (!picture) return true;
    try {
      await deletePicture({
        id: picture.id,
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

  const columns: ProColumns<API.Picture>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInForm: true,
    },
    {
      title: '图片',
      dataIndex: 'url',
      valueType: 'image',
    },
    {
      title: '图片名称',
      dataIndex: 'name',
      valueType: 'text',
    },
    {
      title: '内容',
      dataIndex: 'introduction',
      valueType: 'text',
    },
    {
      title: '分类',
      dataIndex: 'category',
      valueType: 'text',
    },
    {
      title: '标签',
      dataIndex: 'tags',
      valueType: 'select',
      fieldProps: {
        mode: 'tags',
      },
      render: (_, record) => {
        const tagList = JSON.parse(record.tags || '[]');
        return <TagList tagList={tagList} />;
      },
    },
    {
      title: '图片信息',
      valueType: 'text',
      dataIndex: 'picSize',
      hideInForm: true,
      hideInSearch: true,
      sorter: true,
      render: (_, record) => {
        return (
          <ProDescriptions size="small" column={1}>
            <ProDescriptions.Item label="大小">{formatSize(record.picSize)}</ProDescriptions.Item>
            <ProDescriptions.Item label="宽度">{record.picWidth}</ProDescriptions.Item>
            <ProDescriptions.Item label="高度">{record.picHeight}</ProDescriptions.Item>
            <ProDescriptions.Item label="宽高比">{record.picScale}</ProDescriptions.Item>
            <ProDescriptions.Item label="格式">{record.picFormat}</ProDescriptions.Item>
          </ProDescriptions>
        );
      },
      width: 150,
    },
    {
      title: '审核信息',
      dataIndex: 'reviewStatus',
      valueEnum: {
        0: {
          text: '未审核',
          status: 'Default',
        },
        1: {
          text: '审核通过',
          status: 'Success',
        },
        2: {
          text: '审核未通过',
          status: 'Error',
        },
      },
      render: (_, record) => {
        return (
          <ProDescriptions size="small" column={1}>
            <ProDescriptions.Item
              label="状态"
              valueEnum={{
                0: {
                  text: '未审核',
                  status: 'Default',
                },
                1: {
                  text: '审核通过',
                  status: 'Success',
                },
                2: {
                  text: '审核未通过',
                  status: 'Error',
                },
              }}
            >
              {record.reviewStatus}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="信息">{record.reviewMessage}</ProDescriptions.Item>
            <ProDescriptions.Item label="用户">{record.reviewerId}</ProDescriptions.Item>
          </ProDescriptions>
        );
      },
    },
    {
      title: '创建用户',
      dataIndex: 'userId',
      valueType: 'text',
      hideInForm: true,
    },
    {
      title: '审核时间',
      sorter: true,
      dataIndex: 'reviewTime',
      valueType: 'dateTime',
      hideInSearch: true,
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
              setReviewModalVisible(true);
            }}
          >
            审核
          </Typography.Link>
          <Typography.Link
            onClick={() => {
              history.push(`/picture/edit/${record.id}`);
            }}
          >
            修改
          </Typography.Link>
          <Popconfirm title="是否删除这张图片" onConfirm={() => handleDelete(record)}>
            <Typography.Link type="danger">删除</Typography.Link>
          </Popconfirm>
        </Space>
      ),
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.Picture, API.PagePicture>
        headerTitle={'图片信息'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        defaultSize="small"
        toolBarRender={() => [
          <Button
            type="primary"
            key="add"
            onClick={() => {
              history.push('/picture/create');
            }}
          >
            <PlusOutlined /> 创建图片
          </Button>,
          <Button
            key="batchAdd"
            type="primary"
            ghost
            onClick={() => {
              history.push('/admin/picture/batchAdd');
            }}
          >
            <PlusOutlined /> 批量创建图片
          </Button>,
        ]}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listPictureByPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.PictureQueryRequest);
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
      <ReviewForm
        modalVisible={reviewModalVisible}
        oldData={currentRow}
        columns={columns}
        onSubmit={() => {
          setReviewModalVisible(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setReviewModalVisible(false);
        }}
      />
    </PageContainer>
  );
};

export default PictureTableList;
