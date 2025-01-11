import QuestionList from '@/components/QuestionList';
import { getQuestionBankVoById } from '@/services/xiaoxinshu/questionBankController';
import { listQuestionVoByPage } from '@/services/xiaoxinshu/questionController';
import { useParams } from '@@/exports';
import { Avatar, Button, Card, message, Pagination } from 'antd';
import Paragraph from 'antd/es/typography/Paragraph';
import Title from 'antd/es/typography/Title';
import React, { useEffect, useState } from 'react';

const BankDetail: React.FC = () => {
  const params = useParams();
  const { questionBankId } = params;
  const [questionBank, setQuestionBank] = useState<API.QuestionBankVO>();
  const [questionList, setQuestionList] = useState<API.QuestionVO[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [current, setCurrent] = useState<number>(1);
  const [firstQuestionId, setFirstQuestionId] = useState<number>();
  /**
   * 加载数据
   */
  const loadData = async () => {
    if (!questionBankId) {
      message.error('题库不存在');
      return;
    }
    try {
      const res = await getQuestionBankVoById({ id: questionBankId as any });
      setQuestionBank(res.data);
      const questionRes = await listQuestionVoByPage({
        current: current,
        questionBankId: questionBankId as any,
        pageSize: 10,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      if (questionRes?.data?.records && questionRes.data?.records.length > 0) {
        setFirstQuestionId(questionRes.data.records[0].id);
      }
      setTotal(questionRes?.data?.total ?? 0);
      setQuestionList(questionRes?.data?.records ?? []);
    } catch (e: any) {
      message.error('获取题库失败，' + e.message);
    }
  };

  useEffect(() => {
    loadData().then();
  }, [current]);

  return (
    <div className="max-width-content">
      <Card>
        <Card.Meta
          avatar={<Avatar src={questionBank?.picture} size={72} />}
          title={
            <Title level={3} style={{ marginBottom: 0 }}>
              {questionBank?.title}
            </Title>
          }
          description={
            <>
              <Paragraph type="secondary">{questionBank?.description}</Paragraph>
              <Button
                type="primary"
                shape="round"
                href={`/bank/${questionBankId}/question/${firstQuestionId}`}
                target="_blank"
                disabled={!firstQuestionId}
              >
                开始刷题
              </Button>
            </>
          }
        />
      </Card>
      <div style={{ marginBottom: 16 }} />
      <QuestionList
        questionBankId={questionBankId as any}
        questionList={questionList}
        cardTitle={`题目列表（${total}）`}
      />
      <Pagination
        align="end"
        defaultCurrent={1}
        total={total}
        defaultPageSize={10}
        onChange={(page) => setCurrent(page)}
        showTotal={(total) => `发现 ${total} 道题目`}
        showSizeChanger={false}
      />
    </div>
  );
};

export default BankDetail;
