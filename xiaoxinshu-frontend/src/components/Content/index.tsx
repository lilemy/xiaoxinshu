import MdUtils from '@/utils/MdUtils';
import { Tree } from 'antd';
import Title from 'antd/es/typography/Title';
import { useState } from 'react';

interface Props {
  markdown: string;
}

/**
 * 目录
 * @param props
 * @constructor
 */
const ContentPage = (props: Props) => {
  const { markdown = '' } = props;
  const treeData = MdUtils.extractHeadingsAsTreeData(markdown);
  // 当前高亮的目录项
  const [activeKey, setActiveKey] = useState('');
  // 点击节点跳转到页面指定位置
  const onSelect = (selectedKeys: any) => {
    if (selectedKeys.length > 0) {
      const id = selectedKeys[0];
      const target = document.getElementById(id);
      if (target) {
        target.scrollIntoView({ behavior: 'smooth', block: 'start' });
        setActiveKey(id); // 设置高亮项
      }
    }
  };
  return (
    <div>
      <Title level={3}>目录</Title>
      <Tree treeData={treeData} defaultExpandAll onSelect={onSelect} selectedKeys={[activeKey]} />
    </div>
  );
};

export default ContentPage;
