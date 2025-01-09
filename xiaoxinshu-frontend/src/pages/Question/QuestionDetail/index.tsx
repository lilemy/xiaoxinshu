import QuestionCard from '@/components/QuestionCard';
import { getQuestionVoById } from '@/services/xiaoxinshu/questionController';
import { useParams } from '@@/exports';
import { message } from 'antd';
import React, { useEffect, useState } from 'react';
import FloatButtonList from '@/components/FloatButtonList';

/**
 * 题目题库详情页
 * @constructor
 */
const QuestionDetail: React.FC = () => {
  const params = useParams();
  const { questionId } = params;
  const [question, setQuestion] = useState<API.QuestionVO>({});

  // 获取数据
  const loadData = async () => {
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

  useEffect(() => {
    loadData().then();
  }, []);

  return (
    <div className="max-width-content">
      <QuestionCard question={question} />
      <FloatButtonList />
    </div>
  );
};

export default QuestionDetail;
