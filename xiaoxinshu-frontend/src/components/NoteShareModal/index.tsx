import { FRONTEND_HOST_DEV, FRONTEND_HOST_PROD } from '@/constants';
import { Modal, QRCode, Typography } from 'antd';
import React from 'react';

interface Props {
  note: API.NoteVO;
  modalVisible: boolean;
  onCancel: () => void;
}

const NoteShareModal: React.FC<Props> = (props) => {
  const { note, modalVisible, onCancel } = props;
  const isDev = process.env.NODE_ENV === 'development';
  const baseURL = isDev ? FRONTEND_HOST_DEV : FRONTEND_HOST_PROD;
  const shareURL = `${baseURL}/note/${note.id}`;
  return (
    <div>
      <Modal
        title={`分享笔记 - ${note.title}`}
        open={modalVisible}
        destroyOnClose
        footer={null}
        onCancel={onCancel}
      >
        <Typography.Paragraph copyable>{shareURL}</Typography.Paragraph>
        <QRCode value={shareURL} icon="/logo.svg" />
      </Modal>
    </div>
  );
};

export default NoteShareModal;
