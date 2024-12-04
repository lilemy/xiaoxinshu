import ReviewModal from '@/pages/Admin/Review/Question/components/ReviewModal';
import { listReviewingQuestionByPage } from '@/services/xiaoxinshu/questionController';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { Space, Typography } from 'antd';
import Title from 'antd/es/typography/Title';
import React, { useRef, useState } from 'react';

/**
 * 题目审核页面
 * @constructor
 */
const ReviewQuestion: React.FC = () => {
  const actionRef = useRef<ActionType>();
  // 审核题目窗口
  const [reviewModalVisible, setReviewModalVisible] = useState<boolean>(false);
  // 当前题目的数据
  const [currentRow, setCurrentRow] = useState<API.Question>();
  /**
   * 表格列配置
   */
  const columns: ProColumns<API.Question>[] = [
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
      <Title level={4}>审核题目</Title>
      <ProTable<API.Question, API.PageQuestion>
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
          const { data, code } = await listReviewingQuestionByPage({
            needAnswer: true,
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.QuestionQueryRequest);
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

export default ReviewQuestion;
