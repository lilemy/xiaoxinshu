import { getLoginUserByAccessKey } from '@/services/xiaoxinshu/userController';
import { ProDescriptions } from '@ant-design/pro-components';
import { Card, message } from 'antd';
import React, { useEffect, useState } from 'react';

const AccessKeyPage: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [userInfo, setUserInfo] = useState<API.UserByAccessKey>({});
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await getLoginUserByAccessKey();
      setUserInfo(res.data ?? {});
    } catch (e: any) {
      message.error('获取当前用户密钥失败，'+ e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadData();
  }, []);
  return (
    <Card className="max-width-content" loading={loading} title="接口密钥">
      <ProDescriptions column={1}>
        <ProDescriptions.Item label="accessKey" copyable>
          {userInfo.accessKey}
        </ProDescriptions.Item>
        <ProDescriptions.Item label="secretKey" copyable>
          {userInfo.secretKey}
        </ProDescriptions.Item>
      </ProDescriptions>
    </Card>
  );
};

export default AccessKeyPage;
