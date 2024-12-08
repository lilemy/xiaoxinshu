import { Link } from '@@/exports';
import { ProList } from '@ant-design/pro-components';
import { Card } from 'antd';
import { ReactNode } from 'react';

interface Props {
  interfaceInfoList: API.InterfaceInfoVO[];
}

/**
 * 接口列表组件
 * @param props
 * @constructor
 */
const InterfaceInfoList = (props: Props) => {
  const { interfaceInfoList } = props;
  return (
    <Card size="small">
      <ProList<API.InterfaceInfoVO, API.PageInterfaceInfoVO>
        dataSource={interfaceInfoList}
        metas={{
          title: {
            render: (_, data) => {
              return <Link to={`/interface/${data.id}`}>{data.name}</Link>;
            },
          },
          content: {
            dataIndex: 'description',
          },
          extra: {
            render: (_: ReactNode, data: API.InterfaceInfoVO) => {
              return <Link to={`/interface/${data.id}`}>详情</Link>;
            },
          },
        }}
      />
    </Card>
  );
};

export default InterfaceInfoList;
