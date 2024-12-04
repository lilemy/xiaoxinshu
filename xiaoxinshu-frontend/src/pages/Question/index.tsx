import TagList from '@/components/TagList';
import { listQuestionVoByPage } from '@/services/xiaoxinshu/questionController';
import { Link } from '@@/exports';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { Button, Card, Col, Row } from 'antd';
import Title from 'antd/es/typography/Title';
import React, { useRef } from 'react';

const Question: React.FC = () => {
  const actionRef = useRef<ActionType>();
  /**
   * 表格列配置
   */
  const columns: ProColumns<API.QuestionVO>[] = [
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
      render: (_, record) => {
        return <Link to={`/question/${record.id}`}>{record.title}</Link>;
      },
    },
    {
      title: '标签',
      dataIndex: 'tags',
      valueType: 'select',
      fieldProps: {
        mode: 'tags',
      },
      render: (_, record) => {
        return <TagList tagList={record.tagList} />;
      },
    },
  ];
  return (
    <div className="max-width-content" style={{ width: '100%' }}>
      <Card style={{ marginBottom: 15 }}>
        <Row>
          <Col flex="auto">
            <Title level={3}>题目大全</Title>
          </Col>
          <Col>
            <Link to="/question/create">
              <Button type="primary" size={'large'}>
                新建题目
              </Button>
            </Link>
          </Col>
        </Row>
      </Card>
      <ProTable<API.QuestionVO, API.PageQuestionVO>
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 'auto',
        }}
        options={false}
        size={'large'}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listQuestionVoByPage({
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
        columns={columns}
      />
    </div>
  );
};

export default Question;
