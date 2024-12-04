import QuestionCard from '@/components/QuestionCard';
import { getQuestionBankVoById } from '@/services/xiaoxinshu/questionBankController';
import { getQuestionVoById } from '@/services/xiaoxinshu/questionController';
import { useParams } from '@@/exports';
import { history } from '@umijs/max';
import { Card, Col, Menu, message, Row, Typography } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 题目题库详情页（题库关联）
 * @constructor
 */
const BankQuestionDetail: React.FC = () => {
  const params = useParams();
  const { questionBankId, questionId } = params;
  const [questionBank, setQuestionBank] = useState<API.QuestionBankVO>({});
  const [question, setQuestion] = useState<API.QuestionVO>({});

  // 获取题库
  const loadBank = async () => {
    if (!questionBankId) {
      message.error('题库不存在');
      return;
    }
    try {
      const res = await getQuestionBankVoById({
        id: questionBankId as any,
        isNeedQueryQuestionList: true,
      });
      setQuestionBank(res.data ?? {});
    } catch (e: any) {
      message.error('获取题库列表失败，' + e.message);
    }
  };

  // 获取题目
  const loadQuestion = async () => {
    if (!questionId) {
      message.error('题目不存在');
      return;
    }
    try {
      const res = await getQuestionVoById({
        id: questionId as any,
      });
      setQuestion(res.data ?? {});
    } catch (e: any) {
      message.error('获取题目详情失败，' + e.message);
    }
  };

  // 题目菜单列表
  const questionMenuItemList = (questionBank?.questionPage?.records || []).map((q) => {
    return {
      label: q.title ?? '',
      key: q.id ?? 0,
    };
  });

  const changeMenu = (e: { key: any }) => {
    const apiLink = `/bank/${questionBankId}/question/${e.key}`;
    history.push(apiLink);
  };

  useEffect(() => {
    loadBank().then(() => '');
  }, [questionBankId]);

  useEffect(() => {
    loadQuestion().then(() => '');
  }, [questionId]);

  return (
    <div>
      <Row gutter={24}>
        <Col lg={6} md={24}>
          <Card>
            <Typography.Title level={4} style={{ padding: '0 20px' }}>
              {questionBank?.title}
            </Typography.Title>
            <Menu
              items={questionMenuItemList}
              onClick={changeMenu}
              defaultSelectedKeys={[questionId ?? '']}
            />
          </Card>
        </Col>
        <Col lg={18} md={24}>
          <QuestionCard question={question} />
        </Col>
      </Row>
    </div>
  );
};

export default BankQuestionDetail;
