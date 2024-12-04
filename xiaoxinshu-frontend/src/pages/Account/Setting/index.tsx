import BaseView from '@/pages/Account/Setting/commponets/Base';
import { GridContent } from '@ant-design/pro-layout';
import { Menu } from 'antd';
import React, { useState } from 'react';
import useStyles from './style.style';

type SettingsStateKeys = 'base' | 'security' | 'binding' | 'notification';
type SettingsState = {
  mode: 'inline' | 'horizontal';
  selectKey: SettingsStateKeys;
};
const Setting: React.FC = () => {
  const { styles } = useStyles();
  const menuMap: Record<string, React.ReactNode> = {
    base: '基本设置',
    security: '安全设置',
    binding: '账号绑定',
    notification: '新消息通知',
  };
  const [initConfig, setInitConfig] = useState<SettingsState>({
    mode: 'inline',
    selectKey: 'base',
  });
  const getMenu = () => {
    return Object.keys(menuMap).map((item) => ({ key: item, label: menuMap[item] }));
  };
  const renderChildren = () => {
    const { selectKey } = initConfig;
    switch (selectKey) {
      case 'base':
        return <BaseView />;
      case 'security':
        return <div />;
      case 'binding':
        return <div />;
      case 'notification':
        return <div />;
      default:
        return null;
    }
  };
  return (
    <div className="max-width-content">
      <GridContent>
        <div className={styles.main}>
          <div className={styles.leftMenu}>
            <Menu
              mode={initConfig.mode}
              selectedKeys={[initConfig.selectKey]}
              onClick={({ key }) => {
                setInitConfig({
                  ...initConfig,
                  selectKey: key as SettingsStateKeys,
                });
              }}
              items={getMenu()}
            />
          </div>
          <div className={styles.right}>
            <div className={styles.title}>{menuMap[initConfig.selectKey]}</div>
            {renderChildren()}
          </div>
        </div>
      </GridContent>
    </div>
  );
};

export default Setting;
