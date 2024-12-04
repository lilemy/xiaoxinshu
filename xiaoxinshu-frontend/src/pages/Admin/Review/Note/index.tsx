import ReviewModal from '@/pages/Admin/Review/Note/components/ReviewModal';
import { listReviewingNoteByPage } from '@/services/xiaoxinshu/noteController';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { Space, Typography } from 'antd';
import Title from 'antd/es/typography/Title';
import React, { useRef, useState } from 'react';

/**
 * 笔记审核页面
 * @constructor
 */
const ReviewNote: React.FC = () => {
  const actionRef = useRef<ActionType>();
  // 审核题目窗口
  const [reviewModalVisible, setReviewModalVisible] = useState<boolean>(false);
  // 当前题目的数据
  const [currentRow, setCurrentRow] = useState<API.Question>();
  /**
   * 表格列配置
   */
  const columns: ProColumns<API.Note>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
    },
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: '标签',
      dataIndex: 'tags',
      valueType: 'text',
      hideInSearch: true,
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
            详情
          </Typography.Link>
        </Space>
      ),
    },
  ];
  return (
    <div className="max-width-content">
      <Title level={4}>审核笔记</Title>
      <ProTable<API.Note, API.PageNote>
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 'auto',
        }}
        columns={columns}
        options={false}
        size={'large'}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listReviewingNoteByPage({
            needContent: true,
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.NoteQueryRequest);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
      />
      <ReviewModal
        modalVisible={reviewModalVisible}
        oldData={currentRow}
        onSubmit={() => {
          setReviewModalVisible(false);
          actionRef.current?.reload();
        }}
        onCancel={() => {
          setReviewModalVisible(false);
        }}
      />
    </div>
  );
};

export default ReviewNote;
