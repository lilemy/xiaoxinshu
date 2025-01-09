import TagList from '@/components/TagList';
import { Link } from '@@/exports';
import { Avatar, Card, Empty, Image } from 'antd';
import Meta from 'antd/es/card/Meta';
import React from 'react';
import Masonry from 'react-masonry-css';
import './index.css';

interface Props {
  dataList?: API.PictureVO[];
}

const PictureList: React.FC<Props> = (props) => {
  const { dataList } = props;
  const pictureList: API.PictureVO[] = dataList ?? [];
  const pictureListView = (picture: API.PictureVO) => {
    return (
      <Card hoverable cover={<Image src={picture.url} />}>
        <Link to={`/picture/${picture.id}`}>
          <Meta
            avatar={<Avatar src={picture.user?.userAvatar} />}
            title={picture.name}
            description={<TagList tagList={picture.tags} />}
          />
        </Link>
      </Card>
    );
  };
  return (
    <div>
      {pictureList.length === 0 && <Empty />}
      <Masonry
        breakpointCols={{
          default: 3, // 默认3列
          1100: 2, // 屏幕宽度 ≤ 1100px 时显示 2 列
          700: 1, // 屏幕宽度 ≤ 700px 时显示 1 列
        }}
        className="my-masonry-grid"
        columnClassName="my-masonry-grid_column"
      >
        {pictureList.map((picture) => (
          <div key={picture.id} style={{ marginBottom: '16px' }}>
            {pictureListView(picture)}
          </div>
        ))}
      </Masonry>
    </div>
  );
};

export default PictureList;
