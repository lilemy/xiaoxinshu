import MdEditor from '@/components/Markdown/MdEditor';
import { listCategoriesVoByPage } from '@/services/xiaoxinshu/categoriesController';
import { createNote } from '@/services/xiaoxinshu/noteController';
import {
  ProFormRadio,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { Card, Input, message, Tag } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

const NoteCreate: React.FC = () => {
  // 笔记分类列表
  const [categoriesList, setCategoriesList] = useState<API.CategoriesVO[]>([]);
  // 加载
  const [loading, setLoading] = useState<boolean>(false);
  const [contentMd, setContentMd] = useState<string>('');
  const formRef = useRef<ProFormInstance>();
  const [tags, setTags] = useState<string[]>([]);
  const [inputValue, setInputValue] = useState('');
  const loadCategoriesList = async () => {
    setLoading(true);
    try {
      const res = await listCategoriesVoByPage({
        sortField: 'priority',
        sortOrder: 'descend',
      });
      setCategoriesList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取笔记分类失败，' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadCategoriesList().then(() => '');
  }, []);
  const categoriesListForm = categoriesList.map((categoriesVO) => ({
    value: categoriesVO.id,
    label: categoriesVO.name,
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
  const onSubmitFrom = async (value: API.NoteCreateRequest, tags: string[]) => {
    setLoading(true);
    try {
      await createNote({
        ...value,
        tags: tags,
        isTop: value.isTop ? 1 : 0,
      });
      message.success('新笔记创建成功，等待管理员审核');
      formRef.current?.resetFields();
    } catch (e: any) {
      message.error('创建失败，' + e.message);
    }
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
            label="笔记标题："
            name="title"
            rules={[{ required: true, message: '请输入笔记标题' }]}
          />
          <ProFormSelect
            name="categoriesIds"
            label="所属版块"
            mode="multiple"
            options={categoriesListForm}
          />
          <ProFormText label="笔记内容：" name="content">
            <MdEditor
              value={contentMd}
              onChange={(value) => {
                formRef.current?.setFieldsValue({ content: value });
                setContentMd(value);
              }}
            />
          </ProFormText>
          <ProFormRadio.Group
            name="visible"
            label="可见范围："
            radioType="button"
            initialValue={0}
            options={[
              {
                label: '公开',
                value: 0,
              },
              {
                label: '仅自己可见',
                value: 1,
              },
            ]}
          />
          <ProFormSwitch
            label="是否置顶"
            name="isTop"
            checkedChildren="置顶"
            unCheckedChildren="不置顶"
          />
          <ProForm.Item label="笔记标签：" name="tags">
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
              size="small"
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

export default NoteCreate;
