 import QuestionBankList from '@/components/QuestionBankList';
import { listQuestionBankVoByPage } from '@/services/xiaoxinshu/questionBankController';
import { Card, message } from 'antd';
import Title from 'antd/es/typography/Title';
import React, { useEffect, useState } from 'react';

const Bank: React.FC = () => {
  // 题库列表
  const [questionBankList, setQuestionBankList] = useState<API.QuestionBankVO[]>([]);
  const [loading, setLoading] = useState(false);
  const loadData = async () => {
    setLoading(true);
    // 题库数量不多，直接全量获取
    const pageSize = 200;
    // 获取题库
    try {
      const res = await listQuestionBankVoByPage({
        pageSize,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setQuestionBankList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取题库列表失败，' + e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData().then();
  }, []);

  return (
    <Card loading={loading} id="banksPage" className="max-width-content">
      <Title level={3}>题库大全</Title>
      <QuestionBankList questionBankList={questionBankList} />
    </Card>
  );
};

export default Bank;
