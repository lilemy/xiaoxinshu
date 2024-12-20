import ContentPage from '@/components/Content';
import NoteCard from '@/components/NoteCard';
import { getNoteVoById } from '@/services/xiaoxinshu/noteController';
import { useParams } from '@@/exports';
import { Affix, Card, Col, message, Row } from 'antd';
import React, { useEffect, useState } from 'react';

const NoteDetailPage: React.FC = () => {
  const params = useParams();
  const { noteId } = params;
  const [note, setNote] = useState<API.NoteVO>({});

  // 获取数据
  const loadData = async () => {
    if (!noteId) {
      message.error('笔记不存在');
      return;
    }
    try {
      const res = await getNoteVoById({
        id: noteId as any,
      });
      setNote(res.data ?? {});
    } catch (e: any) {
      message.error('获取笔记详情失败，' + e.message);
    }
  };

  useEffect(() => {
    loadData().then();
  }, []);

  return (
    <div className="max-width-content">
      <Row wrap={true}>
        <Col xs={24} sm={24} md={24} lg={18} xl={18}>
          <NoteCard note={note} />
        </Col>
        <Col xs={0} sm={0} md={0} lg={6} xl={6}>
          <Affix offsetTop={80}>
            <Card>
              <ContentPage markdown={note.content || ''} />
            </Card>
          </Affix>
        </Col>
      </Row>
    </div>
  );
};

export default NoteDetailPage;
