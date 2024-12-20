import TagList from '@/components/TagList';
import { deletePicture, listPictureByPage } from '@/services/xiaoxinshu/pictureController';
import { PlusOutlined } from '@ant-design/icons';
import { ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Space, Typography } from 'antd';
import React, { useRef, useState } from 'react';

const PictureTableList: React.FC = () => {
  // 是否显示新建窗口
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Picture>();

  /**
   * @zh-CN 删除图片
   *
   * @param note
   */
  const handleDelete = async (note: API.Note) => {
    const hide = message.loading('正在删除');
    if (!note) return true;
    try {
      await deletePicture({
        id: note.id,
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
      width: 160,
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
      title: '图片大小',
      sorter: true,
      dataIndex: 'picSize',
      valueType: 'text',
    },
    {
      title: '图片宽度',
      sorter: true,
      dataIndex: 'picWidth',
      valueType: 'text',
    },
    {
      title: '图片高度',
      sorter: true,
      dataIndex: 'picHeight',
      valueType: 'text',
    },
    {
      title: '图片宽高比例',
      sorter: true,
      dataIndex: 'picScale',
      valueType: 'text',
    },
    {
      title: '图片格式',
      dataIndex: 'picFormat',
      valueType: 'text',
    },
    {
      title: '创建用户',
      dataIndex: 'userId',
      valueType: 'text',
      hideInForm: true,
    },
    {
      title: '审核状态',
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
    },
    {
      title: '审核信息',
      dataIndex: 'reviewMessage',
      valueType: 'text',
      hideInForm: true,
    },
    {
      title: '审核用户',
      dataIndex: 'reviewerId',
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
              setUpdateModalVisible(true);
            }}
          >
            修改
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
      <ProTable<API.Picture, API.PagePicture>
        headerTitle={'图片信息'}
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="add"
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
        columns={columns}
      />
    </PageContainer>
  );
};

export default PictureTableList;
