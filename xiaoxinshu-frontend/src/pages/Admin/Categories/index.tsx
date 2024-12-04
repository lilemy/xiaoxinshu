import CreateForm from '@/pages/Admin/Categories/components/CreateForm';
import UpdateForm from '@/pages/Admin/Categories/components/UpdateForm';
import {deleteCategories, listCategoriesVoByPage,} from '@/services/xiaoxinshu/categoriesController';
import {PlusOutlined} from '@ant-design/icons';
import {ActionType, PageContainer, ProColumns, ProTable} from '@ant-design/pro-components';
import {Button, message, Space, Typography} from 'antd';
import React, {useRef, useState} from 'react';

/**
 * 笔记所属分类管理
 * @constructor
 */
const CategoriesTableList: React.FC = () => {
  // 是否显示新建窗口
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Note>();
  const handleDelete = async (categoriesId: API.DeleteRequest) => {
    const hide = message.loading('正在删除');
    if (!categoriesId) return true;
    try {
      await deleteCategories({
        id: categoriesId.id,
      });
      console.log(categoriesId);
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
  const columns: ProColumns<API.Note>[] = [
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
      title: '优先级',
      dataIndex: 'priority',
      valueType: 'text',
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
      title: '创建时间',
      sorter: true,
      dataIndex: 'createTime',
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
      <ProTable<API.CategoriesVO, API.PageCategoriesVO>
        actionRef={actionRef}
        headerTitle={'笔记分类信息'}
        columns={columns}
        search={{
          labelWidth: 120,
        }}
        scroll={{ x: 1200 }}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { code, data } = await listCategoriesVoByPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.CategoriesQueryRequest);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
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
      />{' '}
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

export default CategoriesTableList;
