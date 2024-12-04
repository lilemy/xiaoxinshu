import ContentPage from '@/components/Content';
import MdViewer from '@/components/Markdown/MdViewer';
import TagList from '@/components/TagList';
import useAddUserSignInRecord from '@/hooks/useAddUserSignInRecord';
import { Affix, Card, Col, Row } from 'antd';
import Title from 'antd/es/typography/Title';

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

  // 签到
  useAddUserSignInRecord();

  return (
    <div className="question-card">
      <Card>
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
    </div>
  );
};

export default QuestionCard;
