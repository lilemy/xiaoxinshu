import { AvatarDropdown, Footer, SearchInput } from '@/components';
import { getLoginUser } from '@/services/xiaoxinshu/userController';
import { GithubFilled } from '@ant-design/icons';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history } from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import { requestConfig } from './requestConfig';

const loginPath = '/user/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<InitialState> {
  const initialState: InitialState = {
    currentUser: undefined,
  };
  // 如果不是登录页面，执行
  const { location } = history;
  if (!location.pathname.startsWith(loginPath)) {
    // 获取当前用户
    const res = await getLoginUser();
    initialState.currentUser = res.data;
  }
  return initialState;
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
// @ts-ignore
export const layout: RunTimeLayoutConfig = ({ initialState }) => {
  return {
    avatarProps: {
      render: () => {
        return <AvatarDropdown />;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.username,
    },
    footerRender: () => <Footer />,
    actionsRender: (props) => {
      if (props.isMobile) return [];
      return [
        <SearchInput key="search" />,
        <a key="github" href="https://github.com/lilemy" target="_blank" rel="noreferrer">
          <GithubFilled key="GithubFilled" />
        </a>,
      ];
    },
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    logo: () => <img src="/favicon.svg" alt="" />,
    ...defaultSettings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = requestConfig;
