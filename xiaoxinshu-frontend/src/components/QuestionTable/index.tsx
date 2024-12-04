import TagList from '@/components/TagList';
import { listQuestionVoByPage } from '@/services/xiaoxinshu/questionController';
import { Link } from '@@/exports';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import { TablePaginationConfig } from 'antd';
import React, { useRef, useState } from 'react';

interface Props {
  // 默认值（用于展示服务端渲染的数据）
  defaultQuestionList?: API.QuestionVO[];
  defaultTotal?: number;
}

/**
 * 题目表格组件
 *
 * @constructor
 */
const QuestionTable: React.FC = (props: Props) => {
  const { defaultQuestionList, defaultTotal } = props;
  const actionRef = useRef<ActionType>();
  // 题目列表
  const [questionList, setQuestionList] = useState<API.QuestionVO[]>(defaultQuestionList || []);
  // 题目总数
  const [total, setTotal] = useState<number>(defaultTotal || 0);
  // 用于判断是否首次加载
  const [init, setInit] = useState<boolean>(true);
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
      dataIndex: 'tagList',
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
    <div className="question-table">
      <ProTable<API.QuestionVO, API.PageQuestionVO>
        actionRef={actionRef}
        size="large"
        search={{
          labelWidth: 'auto',
        }}
        dataSource={questionList}
        pagination={
          {
            pageSize: 12,
            showTotal: (total) => `总共 ${total} 条`,
            showSizeChanger: false,
            total,
          } as TablePaginationConfig
        }
        // @ts-ignore
        request={async (params, sort, filter) => {
          // 首次请求
          if (init) {
            setInit(false);
            // 如果已有外层传来的默认数据，无需再次查询
            if (defaultQuestionList && defaultTotal) {
              return;
            }
          }
          const sortField = Object.keys(sort)?.[0] || 'createTime';
          const sortOrder = sort?.[sortField] || 'descend';
          const { data, code } = await listQuestionVoByPage({
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.QuestionQueryRequest);
          // 更新结果
          const newData = data?.records || [];
          const newTotal = Number(data?.total) || 0;
          // 更新状态
          setQuestionList(newData);
          setTotal(newTotal);
          return {
            success: code === 0,
            data: newData,
            total: newTotal,
          };
        }}
        columns={columns}
      />
    </div>
  );
};
export default QuestionTable;
