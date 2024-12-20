import { CLIENT_SDK_URL } from '@/constants';
import {
  getInterfaceInfoVo,
  invokeInterfaceInfo,
} from '@/services/xiaoxinshu/interfaceInfoController';
import { useParams } from '@@/exports';
import { ProDescriptions } from '@ant-design/pro-components';
import { Button, Card, Divider, Form, Image, Input, message } from 'antd';
import React, { useEffect, useState } from 'react';

const InterfaceDetail: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const params = useParams();
  const { interfaceInfoId } = params;
  const [interfaceInfo, setInterfaceInfo] = useState<API.InterfaceInfoVO>();
  const [invokeLoading, setInvokeLoading] = useState<boolean>(false);
  const [invokeRes, setInvokeRes] = useState();
  const [invokeResData, setInvokeResData] = useState('');
  const [form] = Form.useForm();
  const loadData = async () => {
    if (!interfaceInfoId) {
      message.error('接口不存在');
      return;
    }
    setLoading(true);
    try {
      const res = await getInterfaceInfoVo({
        id: interfaceInfoId as any,
      });
      setInterfaceInfo(res.data);
    } catch (error: any) {
      message.error('请求失败，' + error.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    loadData().then();
  }, []);

  const invokeInterface = async () => {
    if (!interfaceInfoId) {
      message.error('接口不存在');
      return;
    }
    setInvokeLoading(true);
    const values = await form.validateFields();
    try {
      const res = await invokeInterfaceInfo({
        id: interfaceInfoId as any,
        ...values,
      });
      setInvokeRes(res.data as any);
      if (res.data) {
        setInvokeResData(JSON.parse(res.data.toString()).data);
      }
      message.success('请求成功');
    } catch (error: any) {
      message.error('调用失败，' + error.message);
    }
    setInvokeLoading(false);
  };

  return (
    <div className="max-width-content">
      <Card loading={loading}>
        {interfaceInfo ? (
          <ProDescriptions
            title={interfaceInfo.name}
            extra={
              <a target="_blank" href={CLIENT_SDK_URL} rel="noreferrer">
                下载 SDK
              </a>
            }
            column={1}
          >
            <ProDescriptions.Item
              label="接口状态"
              valueEnum={{
                0: {
                  text: '关闭',
                  status: 'Default',
                },
                1: {
                  text: '开放',
                  status: 'Success',
                },
              }}
            >
              {interfaceInfo.status}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="描述">{interfaceInfo.description}</ProDescriptions.Item>
            <ProDescriptions.Item label="请求地址">
              {interfaceInfo.url}
              {interfaceInfo.path}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="请求方法">{interfaceInfo.method}</ProDescriptions.Item>
            <ProDescriptions.Item label="请求参数" valueType="jsonCode">
              {interfaceInfo.requestParams}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="请求头" valueType="jsonCode">
              {interfaceInfo.requestHeader}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="响应头" valueType="jsonCode">
              {interfaceInfo.responseHeader}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="创建时间" valueType="dateTime">
              {interfaceInfo.createTime}
            </ProDescriptions.Item>
            <ProDescriptions.Item label="更新时间" valueType="dateTime">
              {interfaceInfo.updateTime}
            </ProDescriptions.Item>
          </ProDescriptions>
        ) : (
          <>接口不存在</>
        )}
        <Divider />
        {interfaceInfo?.status === 1 ? (
          <div>
            <Card
              title="在线测试"
              loading={invokeLoading}
              extra={
                <Button type="primary" onClick={invokeInterface} loading={invokeLoading}>
                  调用
                </Button>
              }
            >
              <Form form={form}>
                <Form.Item name="userRequestParams" label="请求参数">
                  <Input addonBefore={interfaceInfo.requestParams} placeholder="请输入请求参数" />
                </Form.Item>
              </Form>
            </Card>
            <Divider />
            <Card title="调用结果" loading={invokeLoading}>
              <ProDescriptions>
                <ProDescriptions.Item valueType="jsonCode">
                  {invokeRes ? invokeRes : '暂无数据'}
                </ProDescriptions.Item>
              </ProDescriptions>
            </Card>
          </div>
        ) : (
          <></>
        )}
        {invokeResData && interfaceInfo?.responseHeader?.includes('image') ? (
          <Card title="图片预览">
            <Image src={invokeResData} />
          </Card>
        ) : (
          <></>
        )}
      </Card>
    </div>
  );
};

export default InterfaceDetail;
