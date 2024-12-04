import MdEditor from '@/components/Markdown/MdEditor';
import { getQuestionBankList } from '@/services/xiaoxinshu/questionBankController';
import { createQuestion } from '@/services/xiaoxinshu/questionController';
import { ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { Card, Input, message, Tag } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

const QuestionCreate: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [questionBankList, setQuestionBankList] = useState<API.QuestionBankListVO[]>([]);
  const [contentMd, setContentMd] = useState<string>('');
  const [answerMd, setAnswerMd] = useState<string>('');
  const formRef = useRef<ProFormInstance>();
  const [tags, setTags] = useState<string[]>([]);
  const [inputValue, setInputValue] = useState('');
  const loadQuestionBankList = async () => {
    setLoading(true);
    try {
      const res = await getQuestionBankList();
      setQuestionBankList(res.data || []);
    } catch (e: any) {
      message.error('获取题库失败，' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadQuestionBankList().then(() => '');
  }, []);
  const questionBankListForm = questionBankList.map((questionBank) => ({
    value: questionBank.id,
    label: questionBank.title,
  }));

  // 回车时添加标签
  const handlePressEnter = () => {
    if (inputValue && !tags.includes(inputValue)) {
      setTags([...tags, inputValue]);
      setInputValue(''); // 清空输入框
    }
  };

  // 删除标签
  const handleClose = (removedTag: any) => {
    setTags(tags.filter((tag) => tag !== removedTag));
  };

  const onSubmitFrom = async (value: API.QuestionCreateRequest, tags: string[]) => {
    setLoading(true);
    try {
      await createQuestion({
        ...value,
        tags: tags,
      });
      message.success('新题目创建成功，等待管理员审核');
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('创建失败，' + e.message);
    }
    setAnswerMd('');
    setContentMd('');
    setTags([]);
    setLoading(false);
  };

  return (
    <div className="max-width-content">
      <Card loading={loading}>
        <ProForm
          size="large"
          style={{ width: '95%', margin: 'auto' }}
          onFinish={async (value) => {
            await onSubmitFrom(value, tags);
          }}
          formRef={formRef}
        >
          <ProFormText
            label="题目标题："
            name="title"
            rules={[{ required: true, message: '请输入题目标题' }]}
          />
          <ProFormSelect
            name="questionBankIds"
            label="所属题库"
            mode="multiple"
            options={questionBankListForm}
          />
          <ProFormText label="题目内容：" name="content">
            <MdEditor
              value={contentMd}
              onChange={(value) => {
                formRef.current?.setFieldsValue({ content: value });
                setContentMd(value);
              }}
            />
          </ProFormText>
          <ProFormText
            tooltip="暂不支持创建题目时添加图片，请创建题目成功后在修改题目页面添加"
            label="推荐答案："
            name="answer"
            rules={[{ required: true, message: '请输入题目推荐答案' }]}
          >
            <MdEditor
              value={answerMd}
              onChange={(value) => {
                formRef.current?.setFieldsValue({ answer: value });
                setAnswerMd(value);
              }}
            />
          </ProFormText>
          <ProForm.Item label="题目标签：" name="tags">
            {tags.map((tag) => (
              <Tag
                onChange={() => formRef.current?.setFieldsValue({ tags: tags })}
                key={tag}
                closable
                onClose={() => handleClose(tag)}
              >
                {tag}
              </Tag>
            ))}
            <Input
              value={inputValue}
              onChange={(e) => {
                setInputValue(e.target.value);
                formRef.current?.setFieldsValue({ tags: tags });
              }}
              onPressEnter={handlePressEnter}
              placeholder="输入后回车添加标签"
              style={{ width: 'auto', marginTop: 8 }}
            />
          </ProForm.Item>
        </ProForm>
      </Card>
    </div>
  );
};

export default QuestionCreate;
