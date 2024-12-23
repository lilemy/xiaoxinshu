import { uploadPictureByBatch } from '@/services/xiaoxinshu/pictureController';
import { ProFormDigit, ProFormText } from '@ant-design/pro-components';
import { ProForm, ProFormInstance } from '@ant-design/pro-form/lib';
import { history } from '@umijs/max';
import { Card, message } from 'antd';
import { useRef, useState } from 'react';

const PictureBatchAddPage = () => {
  const formRef = useRef<ProFormInstance>();
  const [loading, setLoading] = useState<boolean>(false);

  const onSubmitFrom = async (value: Record<string, any>) => {
    setLoading(true);
    try {
      const res = await uploadPictureByBatch({ ...value });
      message.success('抓取图片成功，共获取：' + res.data + '张图片');
      history.push('/pictures');
    } catch (e: any) {
      message.error('批量抓取图片失败：' + e.message);
    }
    setLoading(false);
  };

  return (
    <div className="max-width-content">
      <Card title="批量获取图片">
        <ProForm
          formRef={formRef}
          style={{ maxWidth: 900, margin: '0 auto' }}
          loading={loading}
          onFinish={async (value) => {
            await onSubmitFrom(value);
          }}
          size="large"
          submitter={{
            searchConfig: {
              submitText: '执行操作',
            },
          }}
        >
          <ProFormText
            label="搜索词："
            name="searchText"
            rules={[{ required: true, message: '请输入搜索词' }]}
          />
          <ProFormDigit
            tooltip="最多一次抓取 30 条"
            label="抓取数量："
            name="count"
            initialValue="10"
            width="xs"
            min="1"
            max="30"
          />
          <ProFormText label="名称前缀：" name="namePrefix" />
        </ProForm>
      </Card>
    </div>
  );
};

export default PictureBatchAddPage;
