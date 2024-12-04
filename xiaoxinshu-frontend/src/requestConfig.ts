import { BACKEND_HOST_DEV, BACKEND_HOST_PROD } from '@/constants';
import type { RequestOptions } from '@@/plugin-request/request';
import type { RequestConfig } from '@umijs/max';

const isDev = process.env.NODE_ENV === 'development';

/**
 * 请求处理
 */
export const requestConfig: RequestConfig = {
  baseURL: isDev ? BACKEND_HOST_DEV : BACKEND_HOST_PROD,
  withCredentials: true,

  // 请求拦截器
  requestInterceptors: [
    (config: RequestOptions) => {
      // 拦截请求配置，进行个性化处理。
      return config;
    },
  ],

  // 响应拦截器
  responseInterceptors: [
    (response) => {
      // 请求地址
      const requestPath: string = response.config.url ?? '';

      // 响应
      const { data } = response as unknown as BaseResponse<any>;
      if (!data) {
        throw new Error('服务异常');
      }

      // 错误码处理
      const code: number = data.code;
      // 未登录，且不为获取用户登录信息接口
      if (
        code === 40100 &&
        !requestPath.includes('user/get/login') &&
        !requestPath.includes('user/add/sign_in') &&
        !location.pathname.includes('/user/login')
      ) {
        // 跳转至登录页
        window.location.href = `/user/login?redirect=${window.location.href}`;
        throw new Error('请先登录');
      }

      if (code !== 0) {
        throw new Error(data.message ?? '服务器错误');
      }
      return response;
    },
  ],
};
