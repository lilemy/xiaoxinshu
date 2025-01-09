import InterfaceInfoList from '@/components/InterfaceInfoList';
import NoteList from '@/components/NoteList';
import QuestionBankList from '@/components/QuestionBankList';
import QuestionList from '@/components/QuestionList';
import { listInterfaceInfoVoByPage } from '@/services/xiaoxinshu/interfaceInfoController';
import { listNoteVoByPage } from '@/services/xiaoxinshu/noteController';
import { listQuestionBankVoByPage } from '@/services/xiaoxinshu/questionBankController';
import { listQuestionVoByPage } from '@/services/xiaoxinshu/questionController';
import { Link } from '@@/exports';
import { Card, Divider, Flex, message } from 'antd';
import Title from 'antd/es/typography/Title';
import React, { useEffect, useState } from 'react';
import FloatButtonList from '@/components/FloatButtonList';

/**
 * 主页
 * @constructor
 */
const Welcome: React.FC = () => {
  // 题库列表
  const [questionBankList, setQuestionBankList] = useState<API.QuestionBankVO[]>([]);
  // 题目列表
  const [questionList, setQuestionList] = useState<API.QuestionVO[]>([]);
  // 笔记列表
  const [noteList, setNoteList] = useState<API.NoteVO[]>([]);
  // 接口信息列表
  const [interfaceInfoList, setInterfaceInfoList] = useState<API.InterfaceInfoVO[]>([]);
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    setLoading(true);
    // 获取接口
    try {
      const res = await listInterfaceInfoVoByPage({
        pageSize: 5,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setInterfaceInfoList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取接口信息列表失败，' + e.message);
    }
    // 获取笔记
    try {
      const res = await listNoteVoByPage({
        pageSize: 8,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setNoteList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取笔记列表失败，' + e.message);
    }
    // 获取题库
    try {
      const res = await listQuestionBankVoByPage({
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setQuestionBankList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取题库列表失败，' + e.message);
    }
    // 获取题目
    try {
      const res = await listQuestionVoByPage({
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setQuestionList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取题目列表失败，' + e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData().then();
  }, []);
  return (
    <Card loading={loading} className="max-width-content">
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题库</Title>
        <Link to={'/banks'}>查看更多</Link>
      </Flex>
      <QuestionBankList questionBankList={questionBankList} />
      <Divider />
      <Flex justify="space-between" align="center">
        <Title level={3}>最新题目</Title>
        <Link to={'/questions'}>查看更多</Link>
      </Flex>
      <QuestionList questionList={questionList} />
      <Divider />
      <Flex justify="space-between" align="center">
        <Title level={3}>最新笔记</Title>
        <Link to={'/notes'}>查看更多</Link>
      </Flex>
      <NoteList noteList={noteList} />
      <Divider />
      <Flex justify="space-between" align="center">
        <Title level={3}>开放接口</Title>
        <Link to={'/interfaces'}>查看更多</Link>
      </Flex>
      <InterfaceInfoList interfaceInfoList={interfaceInfoList} />
      <FloatButtonList />
    </Card>
  );
};

export default Welcome;
