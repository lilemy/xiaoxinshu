import QuestionList from '@/components/QuestionList';
import { getQuestionBankVoById } from '@/services/xiaoxinshu/questionBankController';
import { useParams } from '@@/exports';
import { Avatar, Button, Card, message } from 'antd';
import Paragraph from 'antd/es/typography/Paragraph';
import Title from 'antd/es/typography/Title';
import React, { useEffect, useState } from 'react';

const BankDetail: React.FC = () => {
  const params = useParams();
  const { questionBankId } = params;
  const [questionBank, setQuestionBank] = useState<API.QuestionBankVO>();
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
      const res = await getQuestionBankVoById({
        id: questionBankId as any,
        isNeedQueryQuestionList: true,
      });
      setQuestionBank(res.data);
      if (res.data?.questionPage?.records && res.data?.questionPage.records.length > 0) {
        setFirstQuestionId(res.data.questionPage.records[0].id);
      }
    } catch (e: any) {
      message.error('获取题库失败，' + e.message);
    }
  };

  useEffect(() => {
    loadData().then(() => '');
  }, []);

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
        questionList={questionBank?.questionPage?.records ?? []}
        cardTitle={`题目列表（${questionBank?.questionPage?.total || 0}）`}
      />
    </div>
  );
};

export default BankDetail;
