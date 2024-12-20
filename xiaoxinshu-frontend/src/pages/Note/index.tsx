import NoteList from '@/components/NoteList';
import { listCategoriesVoByPage } from '@/services/xiaoxinshu/categoriesController';
import {
  listNoteVoByCategoriesId,
  listNoteVoByPage,
} from '@/services/xiaoxinshu/noteController';
import { Link } from '@@/exports';
import { Button, Card, message, Pagination } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 笔记大全页面
 * @constructor
 */
const NotePage: React.FC = () => {
  // 笔记分类列表
  const [categoriesList, setCategoriesList] = useState<API.CategoriesVO[]>([]);
  // 笔记列表
  const [noteList, setNoteList] = useState<API.NoteVO[]>([]);
  // 加载
  const [loading, setLoading] = useState<boolean>(false);
  // 菜单栏
  const [activeTabKey, setActiveTabKey] = useState<string>('default');
  // 分页信息
  const [total, setTotal] = useState<number>(0);
  const [current, setCurrent] = useState<number>(1);
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listCategoriesVoByPage({
        sortField: 'priority',
        sortOrder: 'descend',
      });
      setCategoriesList(res.data?.records ?? []);
    } catch (e: any) {
      message.error('获取笔记分类失败，' + e.message);
    }
    setLoading(false);
  };

  const loadDefaultNote = async () => {
    try {
      const res = await listNoteVoByPage({
        current: current,
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setNoteList(res.data?.records ?? []);
      setTotal(res.data?.total ?? 0);
    } catch (e: any) {
      message.error('获取笔记失败，' + e.message);
    }
  };

  const loadNoteByCategoriesId = async (tabKey: string) => {
    if (tabKey === 'default') {
      return;
    }
    try {
      const res = await listNoteVoByCategoriesId({
        categoriesId: tabKey as any,
        pageSize: 12,
        sortField: 'createTime',
        sortOrder: 'descend',
      });
      setNoteList(res.data?.records ?? []);
      setTotal(res.data?.total ?? 0);
    } catch (e: any) {
      message.error('获取笔记失败，' + e.message);
    }
  };

  // 获取笔记分类标签列表
  const tabList = [
    { key: 'default', label: '全部' }, // 添加的第一项
    ...categoriesList.map((categories) => ({
      key: categories.id?.toString() || '',
      label: categories.name,
    })),
  ];

  useEffect(() => {
    loadData().then();
  }, []);

  useEffect(() => {
    if (activeTabKey === 'default') {
      loadDefaultNote().then();
    } else {
      loadNoteByCategoriesId(activeTabKey).then();
    }
  }, [activeTabKey, current]);

  return (
    <div className="max-width-content">
      <Card
        loading={loading}
        title="笔记大全"
        extra={
          <Link to="/note/create">
            <Button key="create" type="primary">
              新建笔记
            </Button>
          </Link>
        }
        tabList={tabList}
        activeTabKey={activeTabKey}
        onTabChange={(key: string) => {
          setActiveTabKey(key);
        }}
      >
        <NoteList noteList={noteList} />
        <Pagination
          align="end"
          defaultCurrent={1}
          total={total}
          defaultPageSize={12}
          onChange={(page) => setCurrent(page)}
          showTotal={(total) => `发现 ${total} 条笔记`}
        />
      </Card>
    </div>
  );
};

export default NotePage;
