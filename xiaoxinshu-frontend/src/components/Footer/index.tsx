import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = 'lilemy';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage} | 小新书 一个分享和探索编程知识的地方`}
      links={[
        {
          key: 'icp',
          title: '渝ICP备2024030252号-1',
          href: 'https://beian.miit.gov.cn/',
          blankTarget: true,
        },
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> xiaoxinshu
            </>
          ),
          href: 'https://github.com/lilemy/xiaoxinshu',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
