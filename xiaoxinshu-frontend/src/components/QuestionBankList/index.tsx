import { Link } from '@@/exports';
import { Avatar, Card, List, Typography } from 'antd';

interface Props {
  questionBankList: API.QuestionBankVO[];
}

/**
 * 题库组件
 * @param props
 * @constructor
 */
const QuestionBankList = (props: Props) => {
  const { questionBankList = [] } = props;
  const questionBankView = (questionBank: API.QuestionBankVO) => {
    return (
      <Card hoverable>
        <Link to={`/bank/${questionBank.id}`}>
          <Card.Meta
            avatar={<Avatar src={questionBank.picture} />}
            title={questionBank.title}
            description={
              <Typography.Paragraph
                type="secondary"
                ellipsis={{ rows: 1 }}
                style={{ marginBottom: 0 }}
              >
                {questionBank.description}
              </Typography.Paragraph>
            }
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
          md: 3,
          lg: 3,
        }}
        dataSource={questionBankList}
        renderItem={(item) => <List.Item>{questionBankView(item)}</List.Item>}
      />
    </div>
  );
};

export default QuestionBankList;
