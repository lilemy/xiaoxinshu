// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 创建笔记分类 POST /categories/create */
export async function createCategories(
  body: API.CategoriesCreateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/categories/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除笔记分类 POST /categories/delete */
export async function deleteCategories(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/categories/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取笔记分类列表（封装类） POST /categories/list/vo */
export async function listCategoriesVoByPage(
  body: API.CategoriesQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageCategoriesVO>('/categories/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新笔记分类 POST /categories/update */
export async function updateCategories(
  body: API.CategoriesUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/categories/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
