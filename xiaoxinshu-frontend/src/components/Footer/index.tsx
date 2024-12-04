import { DefaultFooter } from '@ant-design/pro-components';
import { Image } from 'antd';
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
            key: 'xiaoxinshu-gongan',
            title: (
              <>
                <Image src="/beian.png" alt="备案" width={17} height={17} />
                渝公网安备50010602504893
              </>
            ),
            href: 'https://beian.mps.gov.cn/#/query/webSearch?code=50010602504893',
            blankTarget: true,
          },
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
