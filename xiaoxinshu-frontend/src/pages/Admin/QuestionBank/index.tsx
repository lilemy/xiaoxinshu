import CreateForm from '@/pages/Admin/QuestionBank/components/CreateForm';
import UpdateForm from '@/pages/Admin/QuestionBank/components/UpdateForm';
import { uploadFile } from '@/services/xiaoxinshu/fileController';
import {
  deleteQuestionBank,
  listQuestionBankByPage,
} from '@/services/xiaoxinshu/questionBankController';
import { PlusOutlined, UploadOutlined } from '@ant-design/icons';
import { type ActionType, PageContainer, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, message, Space, Typography, Upload } from 'antd';
import React, { useRef, useState } from 'react';

const QuestionBankTableList: React.FC = () => {
  // 是否显示新建窗口
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  // 是否显示更新窗口
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  // 当前题库的数据
  const [currentRow, setCurrentRow] = useState<API.QuestionBank>();
  // 上传图片 url
  const [bankUrl, setBankUrl] = useState<string>('');

  /**
   * @zh-CN 删除题库
   *
   * @param questionBank
   */
  const handleDelete = async (questionBank: API.QuestionBank) => {
    const hide = message.loading('正在删除');
    if (!questionBank) return true;
    try {
      await deleteQuestionBank({
        id: questionBank.id,
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

  // 图片上传
  const handleUpload = async (options: any) => {
    const { file } = options;
    // 构造请求参数
    const params: API.uploadFileParams = {
      uploadFileRequest: {
        biz: 'question_bank_avatar',
        bizId: currentRow?.id,
      },
    };
    // 使用 FormData 包装文件数据
    const formData = new FormData();
    formData.append('file', file);
    try {
      const res = await uploadFile(params, formData);
      setBankUrl(res.data || '');
      message.success('头像上传成功');
    } catch (e: any) {
      message.error('头像上传失败，' + e.message);
    }
  };

  const columns: ProColumns<API.QuestionBank>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInForm: true,
      width: 160,
    },
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: '描述',
      dataIndex: 'description',
      valueType: 'textarea',
    },
    {
      title: '图片',
      dataIndex: 'picture',
      valueType: 'image',
      fieldProps: {
        width: 64,
      },
      hideInSearch: true,
      renderFormItem: (_, { value }) => {
        // value 和 onchange 会通过 form 自动注入
        return (
          <Upload {...value} customRequest={handleUpload} maxCount={1}>
            <Button>
              <UploadOutlined />
              更换头像
            </Button>
          </Upload>
        );
      },
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
      <ProTable<API.QuestionBank, API.PageQuestionBank>
        headerTitle={'题库信息'}
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
          const { data, code } = await listQuestionBankByPage({
            needAnswer: true,
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.QuestionBankQueryRequest);
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
        bankUrl={bankUrl}
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

export default QuestionBankTableList;
