import { FRONTEND_HOST_DEV, FRONTEND_HOST_PROD } from '@/constants';
import { copyToClipboard } from '@/utils';
import { Button, Input, Modal, QRCode, Space } from 'antd';
import Title from 'antd/es/typography/Title';
import React from 'react';

interface Props {
  picture: API.PictureVO;
  modalVisible: boolean;
  onCancel: () => void;
}

const PictureShareModal: React.FC<Props> = (props) => {
  const { picture, modalVisible, onCancel } = props;
  const isDev = process.env.NODE_ENV === 'development';
  const baseURL = isDev ? FRONTEND_HOST_DEV : FRONTEND_HOST_PROD;
  const shareURL = `${baseURL}/picture/${picture.id}`;

  return (
    <div>
      <Modal open={modalVisible} destroyOnClose footer={null} onCancel={onCancel}>
        <Title level={3} style={{ textAlign: 'center', margin: '5px 15px' }}>
          {`分享图片：${picture.name}`}
        </Title>
        <Title level={5}>链接分享</Title>
        <Space.Compact style={{ width: '100%', marginBottom: 16 }}>
          <Input defaultValue={shareURL} style={{ width: 'calc(100% - 100px)' }} disabled />
          <Button
            type="primary"
            onClick={() => {
              copyToClipboard(shareURL);
            }}
          >
            复制链接
          </Button>
        </Space.Compact>
        <Title level={5}>二维码分享</Title>
        <QRCode style={{ margin: '0 auto' }} value={shareURL} icon="/logo.svg" />
      </Modal>
    </div>
  );
};

export default PictureShareModal;
