import { listQuestionBankVoByPage } from '@/services/xiaoxinshu/questionBankController';
import {
  addQuestionBankQuestion,
  listQuestionBankQuestionVoByPage,
  removeQuestionBankQuestion,
} from '@/services/xiaoxinshu/questionBankQuestionController';
import { Form, message, Modal, Select } from 'antd';
import React, { useEffect, useState } from 'react';

interface Props {
  questionTitle?: string;
  questionId?: number;
  visible: boolean;
  onCancel: () => void;
}

/**
 * 更新题目所属题库弹窗
 * @param props
 * @constructor
 */
const UpdateBankModal: React.FC<Props> = (props) => {
  const { questionTitle, questionId, visible, onCancel } = props;
  const [form] = Form.useForm();
  const [questionBankList, setQuestionBankList] = useState<API.QuestionBankVO[]>([]);

  // 获取所属题库列表
  const getCurrentQuestionBankIdList = async () => {
    try {
      const res = await listQuestionBankQuestionVoByPage({
        questionId,
        pageSize: 20,
      });
      const list = (res.data?.records ?? []).map((item) => item.questionBankId);
      console.log(list);
      form.setFieldValue('questionBankIdList' as any, list);
    } catch (e: any) {
      message.error('获取题目所属题库列表失败，' + e.message);
    }
  };

  useEffect(() => {
    if (questionId) {
      getCurrentQuestionBankIdList().then();
    }
  }, [questionId]);

  // 获取题库列表
  const getQuestionBankList = async () => {
    // 题库数量不多，直接全量获取
    const pageSize = 200;
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
  };

  useEffect(() => {
    getQuestionBankList().then();
  }, []);

  return (
    <Modal
      destroyOnClose
      title={`更新所属题库：${questionTitle ?? ''}`}
      open={visible}
      footer={null}
      onCancel={() => {
        onCancel?.();
      }}
    >
      <Form form={form} style={{ marginTop: 24 }}>
        <Form.Item label="所属题库" name="questionBankIdList">
          <Select
            mode="multiple"
            style={{ width: '100%' }}
            options={questionBankList.map((questionBank) => {
              return {
                label: questionBank.title,
                value: questionBank.id,
              };
            })}
            onSelect={async (value) => {
              const hide = message.loading('正在更新');
              try {
                await addQuestionBankQuestion({
                  questionId,
                  questionBankId: value,
                });
                hide();
                message.success('绑定题库成功');
              } catch (error: any) {
                hide();
                message.error('绑定题库失败，' + error.message);
              }
            }}
            onDeselect={async (value) => {
              const hide = message.loading('正在更新');
              try {
                await removeQuestionBankQuestion({
                  questionId,
                  questionBankId: value,
                });
                hide();
                message.success('取消绑定题库成功');
              } catch (error: any) {
                hide();
                message.error('取消绑定题库失败，' + error.message);
              }
            }}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default UpdateBankModal;
