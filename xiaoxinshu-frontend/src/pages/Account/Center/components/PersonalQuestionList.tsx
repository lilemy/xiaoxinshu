import TagList from '@/components/TagList';
import { ProDescriptions, ProList } from '@ant-design/pro-components';
import { Link } from '@umijs/max';

interface Props {
  questionList: API.QuestionPersonalVO[];
}

/**
 * 个人题目列表
 * @param props
 * @constructor
 */
const PersonalQuestionList = (props: Props) => {
  const { questionList } = props;
  return (
    <div>
      <ProList
        rowKey="id"
        dataSource={questionList}
        showActions="hover"
        metas={{
          title: {
            render: (_, data) => {
              return <Link to={`/question/personal/${data.id}`}>{data.title}</Link>;
            },
          },
          subTitle: {
            render: (_, data) => {
              return <TagList tagList={data.tagList} />;
            },
          },
          content: {
            render: (_, data) => {
              return (
                <ProDescriptions>
                  <ProDescriptions.Item
                    label="审核状态"
                    valueEnum={{
                      0: {
                        text: '审核未通过',
                        status: 'Default',
                      },
                      1: {
                        text: '审核通过',
                        status: 'Success',
                      },
                    }}
                  >
                    {data.reviewStatus}
                  </ProDescriptions.Item>
                </ProDescriptions>
              );
            },
          },
          actions: {
            render: (_, data) => {
              return <Link to={`/question/personal/${data.id}`}>详情</Link>;
            },
          },
        }}
      />
    </div>
  );
};

export default PersonalQuestionList;
