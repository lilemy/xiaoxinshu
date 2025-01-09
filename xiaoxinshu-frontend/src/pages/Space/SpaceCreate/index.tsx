import { createSpace, listSpaceLevel } from '@/services/xiaoxinshu/spaceController';
import { formatSize } from '@/utils';
import { ProDescriptions, ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { history } from '@umijs/max';
import { Card, message } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

const SpaceCreatePage: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const formRef = useRef<ProFormInstance>();
  const [spaceLevelList, setSpaceLevelList] = useState<API.SpaceLevel[]>([]);
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listSpaceLevel();
      setSpaceLevelList(res.data ?? []);
    } catch (e: any) {
      message.error('空间级别列表获取失败，' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadData().then();
  }, []);
  const spaceLevelListForm = spaceLevelList.map((spaceLevel) => ({
    value: spaceLevel.value,
    label: spaceLevel.text,
  }));
  const onSubmitFrom = async (value: API.SpaceCreateRequest) => {
    setLoading(true);
    try {
      const res = await createSpace({
        ...value,
      });
      message.success('空间创建成功');
      formRef.current?.resetFields();
      history.push(`/pictures/my/space/${res.data}`);
    } catch (e: any) {
      message.error('空间创建失败，' + e.message);
    }
    setLoading(false);
  };
  return (
    <div className="max-width-content">
      <Card title="创建空间">
        <ProForm
          size="large"
          style={{ width: '95%', margin: '20px auto 5px' }}
          formRef={formRef}
          onFinish={async (value) => {
            await onSubmitFrom(value);
          }}
          loading={loading}
        >
          <ProFormText
            label="空间名称："
            name="spaceName"
            fieldProps={{
              placeholder: '请输入空间名称',
            }}
          />
          <ProFormSelect
            label="空间级别："
            name="spaceLevel"
            options={spaceLevelListForm}
            initialValue={0}
            fieldProps={{
              placeholder: '请选择空间级别',
            }}
          />
        </ProForm>
        <br />
        <Card title="空间级别介绍">
          <ProDescriptions column={1}>
            {spaceLevelList.map((spaceLevel, index) => (
              <ProDescriptions.Item key={index} label={spaceLevel.text}>
                图片总大小 {formatSize(spaceLevel.maxSize)}，图片数量 {spaceLevel.maxCount}
              </ProDescriptions.Item>
            ))}
          </ProDescriptions>
        </Card>
      </Card>
    </div>
  );
};

export default SpaceCreatePage;
