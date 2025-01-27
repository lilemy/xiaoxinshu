import { DefaultFooter } from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = 'lilemy';
  const currentYear = new Date().getFullYear();
  return (
    <div>
      <DefaultFooter
        // @ts-ignore
        copyright={
          <>
            {`${currentYear} ${defaultMessage} | `}
            <a href="https://github.com/lilemy/xiaoxinshu" target="_blank" rel="noreferrer">
              小新书 一个分享和探索编程知识的地方
            </a>
          </>
        }
        style={{
          background: 'none',
        }}
        links={[
          {
            key: 'xiaoxinshu-icp',
            title: '渝ICP备2024030252号-1',
            href: 'https://beian.miit.gov.cn/',
            blankTarget: true,
          },
        ]}
      />
    </div>
  );
};

export default Footer;
