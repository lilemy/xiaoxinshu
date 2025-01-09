// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 创建空间 POST /space/create */
export async function createSpace(body: API.SpaceCreateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/space/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 编辑空间 POST /space/edit */
export async function editSpace(body: API.SpaceEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/space/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取空间列表（仅管理员） POST /space/list */
export async function listSpaceByPage(
  body: API.SpaceQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageSpace>('/space/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取空间等级列表 GET /space/list/level */
export async function listSpaceLevel(options?: { [key: string]: any }) {
  return request<API.BaseResponseListSpaceLevel>('/space/list/level', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 分页获取空间列表 POST /space/list/vo */
export async function listSpaceVoByPage(
  body: API.SpaceQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageSpaceVO>('/space/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新空间（仅管理员） POST /space/update */
export async function updateSpace(body: API.SpaceUpdateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/space/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
