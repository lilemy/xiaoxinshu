import NoteShareModal from '@/components/NoteShareModal';
import TagList from '@/components/TagList';
import { Link } from '@@/exports';
import { ExportOutlined, FireOutlined, LikeOutlined } from '@ant-design/icons';
import { Avatar, Card, List } from 'antd';
import Meta from 'antd/es/card/Meta';
import { useState } from 'react';

interface Props {
  noteList: API.NoteVO[];
}

/**
 * 笔记组件
 * @param props
 * @constructor
 */
const NoteList = (props: Props) => {
  const { noteList = [] } = props;
  const [shareModalVisible, setShareModalVisible] = useState<boolean>(false);
  const [currentNote, setCurrentNote] = useState<API.NoteVO>({});
  const noteListView = (note: API.NoteVO) => {
    return (
      <Card
        hoverable
        cover={<img alt="" src={note.picture || '/xiaoxin.jpg'} />}
        actions={[
          <div key="fire">
            <FireOutlined />
            {note.viewNum}
          </div>,
          <div key="like">
            <LikeOutlined />
            {note.thumbNum}
          </div>,
          <div
            key="share"
            onClick={() => {
              setShareModalVisible(true);
              setCurrentNote(note);
            }}
          >
            <ExportOutlined />
          </div>,
        ]}
      >
        <Link to={`/note/${note.id}`}>
          <Meta
            avatar={<Avatar src={note.user?.userAvatar} />}
            title={note.title}
            description={<TagList tagList={note.tagList} />}
          />
        </Link>
      </Card>
    );
  };
  return (
    <div>
      <List
        grid={{
          gutter: 16,
          column: 4,
          xs: 1,
          sm: 2,
          md: 2,
          lg: 4,
        }}
        dataSource={noteList}
        renderItem={(item) => <List.Item>{noteListView(item)}</List.Item>}
      />
      <NoteShareModal
        note={currentNote}
        modalVisible={shareModalVisible}
        onCancel={() => {
          setShareModalVisible(false);
        }}
      />
    </div>
  );
};

export default NoteList;
