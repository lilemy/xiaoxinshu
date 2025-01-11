import TagList from '@/components/TagList';
import { deletePicture, getPictureVoById } from '@/services/xiaoxinshu/pictureController';
import { downloadImage, formatSize } from '@/utils';
import { useParams } from '@@/exports';
import { DeleteOutlined, DownloadOutlined, EditOutlined, ExportOutlined } from '@ant-design/icons';
import { ProDescriptions } from '@ant-design/pro-components';
import { history, useModel } from '@umijs/max';
import { Avatar, Button, Card, Col, Image, message, Popconfirm, Row, Space } from 'antd';
import React, { useEffect, useState } from 'react';
import PictureShareModal from '@/components/PictureShareModal';

const PictureDetailPage: React.FC = () => {
  const params = useParams();
  const { pictureId } = params;
  const [loading, setLoading] = useState<boolean>(false);
  const [image, setImage] = useState<API.PictureVO>({});
  const [shareModalVisible, setShareModalVisible] = useState<boolean>(false);
  // 当前登录用户
  const { initialState } = useModel('@@initialState');
  const canEdit =
    initialState?.currentUser?.id === image?.userId ||
    initialState?.currentUser?.userRole === 'admin';
  const loadData = async () => {
    setLoading(true);
    if (!pictureId) {
      message.error('图片不存在');
      return;
    }
    try {
      const res = await getPictureVoById({ id: pictureId as any });
      setImage(res.data ?? {});
    } catch (e: any) {
      message.error('图片获取失败：' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadData().then();
  }, []);
  const removePicture = async () => {
    setLoading(true);
    if (!pictureId) {
      message.error('图片不存在');
      return;
    }
    try {
      await deletePicture({ id: pictureId as any });
      message.success('图片删除成功');
      history.push('/pictures');
    } catch (e: any) {
      message.error('图片删除失败：' + e.message);
    }
    setLoading(false);
  };
  // 处理下载
  const doDownload = () => {
    downloadImage(image?.originalUrl);
  };
  return (
    <div className="max-width-content">
      <Row gutter={[16, 16]}>
        <Col sm={24} md={16} xl={18}>
          <Card loading={loading} title="图片预览">
            <Image src={image?.url} style={{ maxHeight: '600px', objectFit: 'contain' }} />
          </Card>
        </Col>
        <Col sm={24} md={8} xl={6}>
          <Card loading={loading} title="图片信息">
            <ProDescriptions column={1}>
              <ProDescriptions.Item label="作者">
                <Space>
                  <Avatar size={24} src={image?.user?.userAvatar} />
                  <div>{image?.user?.username}</div>
                </Space>
              </ProDescriptions.Item>
              <ProDescriptions.Item label="名称">{image?.name ?? '未命名'}</ProDescriptions.Item>
              <ProDescriptions.Item label="简介">{image?.introduction}</ProDescriptions.Item>
              <ProDescriptions.Item label="分类">{image?.category}</ProDescriptions.Item>
              <ProDescriptions.Item label="标签">
                {<TagList tagList={image?.tags} />}
              </ProDescriptions.Item>
              <ProDescriptions.Item label="格式">{image?.picFormat}</ProDescriptions.Item>
              <ProDescriptions.Item label="宽度">{image?.picWidth}</ProDescriptions.Item>
              <ProDescriptions.Item label="高度">{image?.picHeight}</ProDescriptions.Item>
              <ProDescriptions.Item label="宽高比">{image?.picScale}</ProDescriptions.Item>
              <ProDescriptions.Item label="大小">{formatSize(image?.picSize)}</ProDescriptions.Item>
            </ProDescriptions>
            <br />
            <Space wrap>
              {canEdit && (
                <Button onClick={() => history.push(`/picture/edit/${pictureId}`)}>
                  <EditOutlined />
                  图片编辑
                </Button>
              )}
              {canEdit && (
                <Popconfirm title="是否删除这张图片" onConfirm={removePicture}>
                  <Button danger>
                    <DeleteOutlined />
                    图片删除
                  </Button>
                </Popconfirm>
              )}
              <Button
                type="primary"
                onClick={() => {
                  setShareModalVisible(true);
                }}
              >
                <ExportOutlined />
                图片分享
              </Button>
              <Button type="primary" onClick={doDownload} ghost>
                <DownloadOutlined />
                图片下载
              </Button>
            </Space>
          </Card>
        </Col>
      </Row>
      <PictureShareModal
        picture={image}
        modalVisible={shareModalVisible}
        onCancel={() => {
          setShareModalVisible(false);
        }}
      />
    </div>
  );
};

export default PictureDetailPage;
