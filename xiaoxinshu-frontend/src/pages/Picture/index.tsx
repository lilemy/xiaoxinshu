import TagList from '@/components/TagList';
import {
  listPictureTagCategory,
  listPictureVoByPage,
} from '@/services/xiaoxinshu/pictureController';
import { Link } from '@@/exports';
import { Avatar, Button, Card, Empty, Flex, Image, message, Pagination, Tabs, Tag } from 'antd';
import Meta from 'antd/es/card/Meta';
import Search from 'antd/es/input/Search';
import React, { useEffect, useState } from 'react';
import Masonry from 'react-masonry-css';
import './index.css';

/**
 * 图片大全页面
 * @constructor
 */
const PicturePage: React.FC = () => {
  // 加载
  const [loading, setLoading] = useState<boolean>(false);
  const [pictureList, setPictureList] = useState<API.PictureVO[]>([]);
  const [currentPage, setCurrentPage] = useState<number>(1);
  const [total, setTotal] = useState<number>();
  const [tagList, setTagList] = useState<string[]>([]);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [category, setCategory] = useState<string[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [searchValue, setSearchValue] = useState<string>('');
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listPictureVoByPage({
        searchText: searchValue,
        category: selectedCategory === 'all' ? '' : selectedCategory,
        tags: selectedTags,
        current: currentPage,
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setPictureList(res.data?.records ?? []);
      setTotal(res.data?.total ?? 0);
    } catch (e: any) {
      message.error('获取图片列表失败：', e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadData().then();
  }, [currentPage]);
  useEffect(() => {
    if (currentPage !== 1) {
      setCurrentPage(1);
    } else {
      loadData().then(); // 如果当前页已经是 1，直接加载数据
    }
  }, [selectedCategory, selectedTags, searchValue]);
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
  // 加载图片分类标签信息
  const loadTagAndCategory = async () => {
    setLoading(true);
    try {
      const res = await listPictureTagCategory();
      setTagList(res.data?.tagList ?? []);
      setCategory(res.data?.categoryList ?? []);
    } catch (e: any) {
      message.error('获取图片分类标签信息失败：', e.message);
    }
    setLoading(false);
  };
  useEffect(() => {
    loadTagAndCategory().then();
  }, []);
  const categoryList = [
    { key: 'all', label: '全部' },
    ...category.map((category) => ({
      key: category,
      label: category,
    })),
  ];

  function handleChange(tag: string, checked: boolean) {
    const nextSelectedTags = checked
      ? [...selectedTags, tag]
      : selectedTags.filter((t) => t !== tag);
    setSelectedTags(nextSelectedTags);
  }

  return (
    <div className="max-width-content">
      <Card
        title="图片大全"
        loading={loading}
        extra={
          <Link to="/picture/create">
            <Button key="create" type="primary">
              上传图片
            </Button>
          </Link>
        }
      >
        <div style={{ maxWidth: '480px', margin: '0 auto 16px' }}>
          <Search
            allowClear
            placeholder="搜索图片"
            size="large"
            enterButton="搜索"
            onSearch={(value) => {
              setSearchValue(value);
            }}
            defaultValue={searchValue}
          />
        </div>
        <Tabs
          type="card"
          defaultActiveKey={selectedCategory}
          items={categoryList}
          onChange={(key) => {
            setSelectedCategory(key);
          }}
        />
        <Flex gap={4} wrap align="center">
          <span>标签：</span>
          {tagList.map<React.ReactNode>((tag) => (
            <Tag.CheckableTag
              key={tag}
              checked={selectedTags.includes(tag)}
              onChange={(checked) => handleChange(tag, checked)}
            >
              {tag}
            </Tag.CheckableTag>
          ))}
        </Flex>
        <br />
        {pictureList.length===0 && <Empty />}
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
        <br />
        <Pagination
          align="end"
          defaultCurrent={1}
          total={total}
          current={currentPage}
          pageSize={12}
          onChange={setCurrentPage}
        />
      </Card>
    </div>
  );
};

export default PicturePage;
