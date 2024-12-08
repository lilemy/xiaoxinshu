// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 添加接口信息 POST /interfaceInfo/add */
export async function addInterfaceInfo(
  body: API.InterfaceInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/interfaceInfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除接口信息 POST /interfaceInfo/delete */
export async function deleteInterfaceInfo(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/interfaceInfo/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据 id 查询接口信息 GET /interfaceInfo/get */
export async function getInterfaceInfo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfaceInfoParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceInfo>('/interfaceInfo/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 根据 id 查询接口信息封装 GET /interfaceInfo/get/vo */
export async function getInterfaceInfoVo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getInterfaceInfoVOParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInterfaceInfoVO>('/interfaceInfo/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 在线调用接口 POST /interfaceInfo/invoke */
export async function invokeInterfaceInfo(
  body: API.InterfaceInfoInvokeRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseObject>('/interfaceInfo/invoke', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取接口信息 POST /interfaceInfo/list */
export async function listInterfaceInfoByPage(
  body: API.InterfaceInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfaceInfo>('/interfaceInfo/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取接口信息封装 POST /interfaceInfo/list/vo */
export async function listInterfaceInfoVoByPage(
  body: API.InterfaceInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageInterfaceInfoVO>('/interfaceInfo/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 下线接口 POST /interfaceInfo/offline */
export async function offlineInterface(
  body: API.InterfaceOfflineRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/interfaceInfo/offline', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 上线接口 POST /interfaceInfo/online */
export async function onlineInterface(
  body: API.InterfaceOnlineRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/interfaceInfo/online', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改接口信息 POST /interfaceInfo/update */
export async function updateInterfaceInfo(
  body: API.InterfaceInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/interfaceInfo/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
