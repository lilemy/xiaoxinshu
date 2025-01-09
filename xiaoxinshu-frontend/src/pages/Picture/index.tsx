import FloatButtonList from '@/components/FloatButtonList';
import PictureList from '@/components/PictureList';
import {
  listPictureTagCategory,
  listPictureVoByPage,
} from '@/services/xiaoxinshu/pictureController';
import { Link } from '@@/exports';
import { Button, Card, Flex, message, Pagination, Tabs, Tag } from 'antd';
import Search from 'antd/es/input/Search';
import React, { useEffect, useState } from 'react';

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
        nullSpaceId: true,
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
    loadData().then();
  }, [currentPage]);
  useEffect(() => {
    if (currentPage !== 1) {
      setCurrentPage(1);
    } else {
      loadData().then(); // 如果当前页已经是 1，直接加载数据
    }
  }, [selectedCategory, selectedTags, searchValue]);
  // 加载图片分类标签信息
  const loadTagAndCategory = async () => {
    setLoading(true);
    try {
      const res = await listPictureTagCategory();
      setTagList(res.data?.tagList ?? []);
      setCategory(res.data?.categoryList ?? []);
    } catch (e: any) {
      message.error('获取图片分类标签信息失败：' + e.message);
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
    <div>
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
        <PictureList dataList={pictureList} />
        <br />
        <Pagination
          align="end"
          defaultCurrent={1}
          total={total}
          current={currentPage}
          pageSize={12}
          onChange={setCurrentPage}
          showSizeChanger={false}
        />
      </Card>
      <FloatButtonList />
    </div>
  );
};

export default PicturePage;
