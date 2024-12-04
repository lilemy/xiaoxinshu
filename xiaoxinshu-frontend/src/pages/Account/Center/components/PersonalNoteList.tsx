import TagList from '@/components/TagList';
import { listMyNoteVoByPage } from '@/services/xiaoxinshu/noteController';
import { history } from '@@/core/history';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import { Space, Typography } from 'antd';

/**
 * 个人笔记列表
 * @constructor
 */
const PersonalNoteList = () => {
  const columns: ProColumns<API.NotePersonalVO>[] = [
    {
      title: 'id',
      dataIndex: 'id',
      valueType: 'text',
      hideInForm: true,
      hideInSearch: true,
      width: 160,
    },
    {
      title: '标题',
      dataIndex: 'title',
      valueType: 'text',
    },
    {
      title:'图片',
      dataIndex: 'picture',
      valueType: 'image',
      fieldProps: {
        width: 64,
      },
      hideInSearch: true,
      width: 80,
    },
    {
      title: '标签',
      dataIndex: 'tags',
      valueType: 'select',
      fieldProps: {
        mode: 'tags',
      },
      render: (_, record) => {
        const tagList = record.tagList;
        return <TagList tagList={tagList} />;
      },
    },
    {
      title: '审核状态',
      dataIndex: 'reviewStatus',
      valueEnum: {
        0: {
          text: '待审核',
          status: 'Default',
        },
        1: {
          text: '审核通过',
          status: 'Success',
        },
        2: {
          text: '审核不通过',
          status: 'Error',
        },
      },
      width: 100,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 80,
      render: (_, record) => (
        <Space size="middle">
          <Typography.Link
            onClick={() => {
              history.push(`/note/personal/${record.id}`);
            }}
          >
            查看详情
          </Typography.Link>
        </Space>
      ),
    },
  ];
  return (
    <div>
      <ProTable<API.NotePersonalVO, API.PageNotePersonalVO>
        rowKey="id"
        options={false}
        columns={columns}
        pagination={{
          // 默认每页显示10条
          pageSize: 10,
        }}
        request={async (params, sort, filter) => {
          const sortField = Object.keys(sort)?.[0];
          const sortOrder = sort?.[sortField] ?? undefined;
          const { data, code } = await listMyNoteVoByPage({
            needAnswer: false,
            ...params,
            sortField,
            sortOrder,
            ...filter,
          } as API.NoteQueryRequest);
          return {
            success: code === 0,
            data: data?.records || [],
            total: Number(data?.total) || 0,
          };
        }}
      />
    </div>
  );
};

export default PersonalNoteList;
