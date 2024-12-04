import MdViewer from '@/components/Markdown/MdViewer';
import NoteShareModal from '@/components/NoteShareModal';
import TagList from '@/components/TagList';
import { ExportOutlined, FireOutlined, HeartOutlined, LikeOutlined } from '@ant-design/icons';
import { Avatar, Card, Flex } from 'antd';
import Title from 'antd/es/typography/Title';
import { useState } from 'react';

interface Props {
  note: API.NoteVO;
}

/**
 * 笔记卡片
 * @param props
 * @constructor
 */
const NoteCard = (props: Props) => {
  const { note } = props;
  const [shareModalVisible, setShareModalVisible] = useState<boolean>(false);
  return (
    <div>
      <Card
        actions={[
          <div key="fire">
            <FireOutlined />
            {note.viewNum || '浏览'}
          </div>,
          <div key="like">
            <LikeOutlined />
            {note.thumbNum || '点赞'}
          </div>,
          <div key="heart">
            <HeartOutlined />
            {note.favourNum || '收藏'}
          </div>,
          <div
            key="share"
            onClick={() => {
              setShareModalVisible(true);
            }}
          >
            <ExportOutlined />
          </div>,
        ]}
      >
        <Flex justify="space-between" align="center">
          <Title level={1} style={{ fontSize: 24 }}>
            {note.title}
          </Title>
          <div>
            <Avatar style={{ marginRight: 5 }} src={note.user?.userAvatar} />
            {note.user?.username}
          </div>
        </Flex>
        <TagList tagList={note.tagList} />
      </Card>
      <div style={{ marginBottom: 16 }} />
      <Card title="笔记内容">
        <MdViewer value={note.content} />
      </Card>
      <NoteShareModal
        note={note}
        modalVisible={shareModalVisible}
        onCancel={() => {
          setShareModalVisible(false);
        }}
      />
    </div>
  );
};

export default NoteCard;
