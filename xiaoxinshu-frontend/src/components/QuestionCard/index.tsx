import ContentPage from '@/components/Content';
import MdViewer from '@/components/Markdown/MdViewer';
import QuestionShareModal from '@/components/QuestionShareModal';
import TagList from '@/components/TagList';
import useAddUserSignInRecord from '@/hooks/useAddUserSignInRecord';
import { ExportOutlined, FireOutlined, HeartOutlined, LikeOutlined } from '@ant-design/icons';
import { Affix, Card, Col, Row } from 'antd';
import Title from 'antd/es/typography/Title';
import { useState } from 'react';

interface Props {
  question: API.QuestionVO;
}

/**
 * 题目卡片
 * @param props
 * @constructor
 */
const QuestionCard = (props: Props) => {
  const { question } = props;
  const [shareModalVisible, setShareModalVisible] = useState<boolean>(false);
  // 签到
  useAddUserSignInRecord();

  return (
    <div className="question-card">
      <Card
        actions={[
          <div key="fire">
            <FireOutlined />
            {question.viewNum || '浏览'}
          </div>,
          <div key="like">
            <LikeOutlined />
            {question.thumbNum || '点赞'}
          </div>,
          <div key="heart">
            <HeartOutlined />
            {question.favourNum || '收藏'}
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
        <Title level={1} style={{ fontSize: 24 }}>
          {question.title}
        </Title>
        <TagList tagList={question.tagList} />
        <div style={{ marginBottom: 16 }} />
        <MdViewer value={question.content} />
      </Card>
      <div style={{ marginBottom: 16 }} />
      <Row wrap={true}>
        <Col
          xs={{ span: '24' }}
          sm={{ span: '24' }}
          md={{ span: '24' }}
          lg={{ span: '18' }}
          xl={{ span: '18' }}
        >
          <Card title="推荐答案">
            <MdViewer value={question.answer} />
          </Card>
        </Col>
        <Col
          xs={{ span: '0' }}
          sm={{ span: '0' }}
          md={{ span: '0' }}
          lg={{ span: '6' }}
          xl={{ span: '6' }}
        >
          <Affix offsetTop={100}>
            <Card>
              <ContentPage markdown={question.answer || ''} />
            </Card>
          </Affix>
        </Col>
      </Row>
      <QuestionShareModal
        question={question}
        modalVisible={shareModalVisible}
        onCancel={() => {
          setShareModalVisible(false);
        }}
      />
    </div>
  );
};

export default QuestionCard;
