import MdEditor from '@/components/Markdown/MdEditor';
import MdViewer from '@/components/Markdown/MdViewer';
import TagList from '@/components/TagList';
import { uploadFile } from '@/services/xiaoxinshu/fileController';
import { getQuestionBankList } from '@/services/xiaoxinshu/questionBankController';
import {
  editQuestion,
  getQuestionPersonalById,
} from '@/services/xiaoxinshu/questionController';
import { useParams } from '@@/exports';
import { UploadOutlined } from '@ant-design/icons';
import { ProDescriptions, ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { Button, Card, Input, message, Space, Tag, Upload } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

/**
 * 个人题目详情页
 */
const QuestionPersonalPage: React.FC = () => {
  const params = useParams();
  const { questionId } = params;
  const [question, setQuestion] = useState<API.QuestionPersonalVO>({});
  const [bankList, setBankList] = useState<API.QuestionBankListVO[]>([]);
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [contentMd, setContentMd] = useState<string>('');
  const [answerMd, setAnswerMd] = useState<string>('');
  const [tags, setTags] = useState<string[]>([]);
  const [showPic, setShowPic] = useState<boolean>(false);
  const [picList, setPicList] = useState<string[]>([]);
  const [inputValue, setInputValue] = useState('');
  const formRef = useRef<ProFormInstance>();
  const [loading, setLoading] = useState<boolean>(false);
  const loadData = async () => {
    setLoading(true);
    if (!questionId) {
      message.error('题目不存在');
      return;
    }
    try {
      const res = await getQuestionPersonalById({
        id: questionId as any,
      });
      setQuestion(res.data || {});
      setContentMd(res.data?.content || '');
      setAnswerMd(res.data?.answer || '');
      setTags(res.data?.tagList || []);
    } catch (e: any) {
      message.error('获取题目失败，' + e.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData().then(() => '');
  }, [isEdit]);

  const questionBankList = question.questionBankList?.map((value) => value.id);

  const getBankList = async () => {
    setLoading(true);
    try {
      const res = await getQuestionBankList();
      setBankList(res.data || []);
    } catch (e: any) {
      message.error('获取题库失败，' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    getBankList().then(() => '');
  }, []);
  const bankListForm = bankList.map((bankList) => ({
    value: bankList.id,
    label: bankList.title,
  }));

  const columns = [
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
    },
    {
      title: '标签',
      dataIndex: 'tags',
      valueType: 'select',
      render: () => {
        return <TagList tagList={question.tagList} />;
      },
    },
    {
      title: '所属题库',
      dataIndex: 'questionBankList',
      valueType: 'text',
      render: () => {
        return question.questionBankList?.map((bank) => {
          return <Tag key={bank.id}>{bank.title}</Tag>;
        });
      },
    },
    {
      title: '内容',
      dataIndex: 'content',
      valueType: 'text',
      render: () => {
        return <MdViewer value={question.content} />;
      },
    },
    {
      title: '答案',
      dataIndex: 'answer',
      valueType: 'text',
      render: () => {
        return <MdViewer value={question.answer} />;
      },
    },
    {
      title: '审核状态',
      dataIndex: 'reviewStatus',
      valueEnum: {
        0: {
          text: '待审核',
          status: 'Default',
        },
        1: {
          text: '审核通过',
          status: 'Success',
        },
        2: {
          text: '审核不通过',
          status: 'error',
        },
      },
    },
    {
      title: '审核信息',
      dataIndex: 'reviewMessage',
      valueType: 'text',
    },
    {
      title: '审核人 id',
      dataIndex: 'reviewerId',
      valueType: 'text',
    },
    {
      title: '审核时间',
      dataIndex: 'reviewTime',
      valueType: 'dateTime',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
    },
    {
      title: '编辑时间',
      dataIndex: 'editTime',
      valueType: 'dateTime',
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
    },
  ];

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

  /**
   * 表单提交
   * @param value
   * @param tags
   */
  const onSubmitFrom = async (value: API.QuestionEditRequest, tags: string[]) => {
    setLoading(true);
    try {
      await editQuestion({
        id: question.id,
        ...value,
        tags: tags,
      });
      message.success('题目修改成功，等待管理员审核');
      formRef.current?.resetFields();
      setIsEdit(false);
    } catch (e: any) {
      message.error('保存失败：', e.message);
    }
    setLoading(false);
  };

  // 图片上传
  const handleUpload = async (options: any) => {
    const { file } = options;
    // 构造请求参数
    const params: API.uploadFileParams = {
      uploadFileRequest: {
        biz: 'question_md',
        bizId: questionId as any,
      },
    };
    // 使用 FormData 包装文件数据
    const formData = new FormData();
    formData.append('file', file);
    try {
      const res = await uploadFile(params, formData);
      if (res.data) {
        setPicList((prevList) => [res.data || '', ...prevList]);
      }
      setShowPic(true);
      message.success('图片上传成功');
    } catch (e: any) {
      message.error('图片上传失败，' + e.message);
    }
  };

  return (
    <div className="max-width-content">
      <Card
        title="个人题目详情"
        extra={
          <div>
            {!isEdit ? (
              <Button onClick={() => setIsEdit(true)}>修改题目</Button>
            ) : (
              <Button onClick={() => setIsEdit(false)}>查看详情</Button>
            )}
          </div>
        }
      >
        {!isEdit ? (
          <ProDescriptions loading={loading} columns={columns} dataSource={question} column={1} />
        ) : (
          <ProForm
            size="large"
            style={{ width: '95%', margin: 'auto' }}
            formRef={formRef}
            onFinish={async (value) => {
              await onSubmitFrom(value, tags);
            }}
          >
            <ProFormText
              label="题目标题："
              name="title"
              rules={[{ required: true, message: '请输入题目标题' }]}
              initialValue={question.title}
            />
            <ProFormSelect
              name="questionBankList"
              label="所属题库"
              mode="multiple"
              options={bankListForm}
              initialValue={questionBankList}
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
              label="推荐答案："
              name="answer"
              extra={
                <div>
                  <Space>
                    {showPic ? (
                      <Button size="middle" type="primary" onClick={() => setShowPic(false)}>
                        隐藏图片
                      </Button>
                    ) : (
                      <Button size="middle" type="primary" onClick={() => setShowPic(true)}>
                        查看图片
                      </Button>
                    )}
                    <Upload showUploadList={false} customRequest={handleUpload}>
                      <div>
                        <Button size="small">
                          <UploadOutlined />
                          添加图片
                        </Button>
                      </div>
                    </Upload>
                  </Space>
                  {showPic ? (
                    <div>
                      <ProDescriptions column={1}>
                        {picList.map((pic, key) => {
                          return (
                            <ProDescriptions.Item key={key} copyable={true} valueType="text">
                              {`<img src="${pic}" alt="">`}
                            </ProDescriptions.Item>
                          );
                        })}
                      </ProDescriptions>
                    </div>
                  ) : (
                    <></>
                  )}
                </div>
              }
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
        )}
      </Card>
    </div>
  );
};

export default QuestionPersonalPage;
