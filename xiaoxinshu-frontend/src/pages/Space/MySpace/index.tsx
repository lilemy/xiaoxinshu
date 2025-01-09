import PictureList from '@/components/PictureList';
import { listPictureVoByPage } from '@/services/xiaoxinshu/pictureController';
import { listSpaceVoByPage } from '@/services/xiaoxinshu/spaceController';
import { formatSize } from '@/utils';
import { history, Link, useModel } from '@umijs/max';
import { Button, Card, message, Pagination, Progress, Space, Tooltip } from 'antd';
import React, { useEffect, useState } from 'react';

const MySpacePage: React.FC = () => {
  // 当前登录用户
  const { initialState } = useModel('@@initialState');
  const loginUser = initialState?.currentUser;
  const [loading, setLoading] = useState<boolean>(false);
  const [space, setSpace] = useState<API.SpaceVO>();
  const [pictureList, setPictureList] = useState<API.PictureVO[]>([]);
  const [total, setTotal] = useState<number>();
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [sizePercent, setSizePercent] = useState<number>(0);
  // 检查用户是否有个人空间
  const checkUserSpace = async () => {
    if (!loginUser?.id) {
      history.replace('/user/login');
    }
    try {
      const res = await listSpaceVoByPage({
        userId: loginUser?.id,
        current: 1,
        pageSize: 1,
      });
      if (res.code === 0 && res.data?.records) {
        if (res.data.records.length > 0) {
          const space = res.data.records[0];
          setSpace(space);
          history.replace(`/pictures/my/space/${space.id}`);
          const totalSize = space.totalSize ?? 0;
          const maxSize = space.maxSize ?? 0;
          const percent = Number(((totalSize * 100) / maxSize).toFixed(1));
          setSizePercent(percent);
        } else {
          message.warning('请先创建空间');
          history.push('/space/create');
        }
      }
    } catch (e: any) {
      message.error('个人空间加载失败，' + e.message);
    }
  };
  useEffect(() => {
    checkUserSpace().then();
  }, []);
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listPictureVoByPage({
        spaceId: space?.id,
        current: currentPage,
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setPictureList(res.data?.records ?? []);
      setTotal(res.data?.total ?? 0);
    } catch (e: any) {
      message.error('获取图片列表失败：' + e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    if (space?.id) {
      loadData().then();
    }
  }, [space,currentPage]);
  return (
    <div>
      <Card
        loading={loading}
        title={`${space?.spaceName}（私有空间）`}
        extra={
          <div>
            <Space>
              <Link to={`/picture/create/${space?.id}`}>
                <Button type="primary">上传图片</Button>
              </Link>
              <Tooltip
                title={`占用空间 ${formatSize(space?.totalSize)} / ${formatSize(space?.maxSize)}`}
              >
                <Progress type="circle" percent={sizePercent} size={40} />
              </Tooltip>
            </Space>
          </div>
        }
      >
        <PictureList dataList={pictureList} />
        <br />
        <Pagination
          align="end"
          defaultCurrent={1}
          total={total}
          current={currentPage}
          pageSize={12}
          onChange={setCurrentPage}
          showTotal={(total) => `图片总数 ${total} / ${space?.maxCount}`}
          showSizeChanger={false}
        />
      </Card>
    </div>
  );
};

export default MySpacePage;
