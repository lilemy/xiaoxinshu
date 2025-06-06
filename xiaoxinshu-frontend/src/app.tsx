import { AvatarDropdown, Footer } from '@/components';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history } from '@umijs/max';
import { requestConfig } from './requestConfig';
import React from 'react';
import { getLoginUser } from '@/services/xiaoxinshu-backend/userController';
import defaultSettings from '../config/defaultSettings';
import { GithubFilled } from '@ant-design/icons';

const loginPath = '/user/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<InitialState> {
  const initialState: InitialState = {
    currentUser: undefined,
  };

  // 如果不是登录界面，执行
  const { location } = history;
  if (!location.pathname.startsWith(loginPath)) {
    // 获取当前登录用户
    try {
      const res = await getLoginUser();
      initialState.currentUser = res.data;
    } catch (error: any) {}
  }
  return initialState;
}

// @ts-ignore
// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState }) => {
  return {
    avatarProps: {
      render: () => {
        return <AvatarDropdown />;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.userName,
    },
    actionsRender: (props) => {
      if (props.isMobile) return [];
      return [
        <a key="github" href="https://github.com/lilemy" target="_blank" rel="noreferrer">
          <GithubFilled key="GithubFilled" />
        </a>,
      ];
    },
    footerRender: () => <Footer />,
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    logo: () => <img src="/logo.svg" alt="" />,
    ...defaultSettings,
  };
};

/**
 * @name requestConfig 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = requestConfig;
