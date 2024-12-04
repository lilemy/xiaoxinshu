// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 添加笔记（管理员） POST /note/add */
export async function addNote(body: API.NoteAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/note/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 创建笔记 POST /note/create */
export async function createNote(body: API.NoteCreateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/note/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除笔记 POST /note/delete */
export async function deleteNote(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/note/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 编辑笔记 POST /note/edit */
export async function editNote(body: API.NoteEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/note/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据 id 获取个人笔记 GET /note/get/my/vo */
export async function getNotePersonalVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getNotePersonalVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseNotePersonalVO>('/note/get/my/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 根据id获取笔记封装 GET /note/get/vo */
export async function getNoteVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getNoteVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseNoteVO>('/note/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 分页获取笔记列表（仅管理员可用） POST /note/list */
export async function listNoteByPage(body: API.NoteQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponsePageNote>('/note/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据分类 id 分页获取笔记列表（封装类） POST /note/list/id/vo */
export async function listNoteVoByCategoriesId(
  body: API.NoteQueryByCategoriesRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageNoteVO>('/note/list/id/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取个人笔记列表 POST /note/list/my/vo */
export async function listMyNoteVoByPage(
  body: API.NoteQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageNotePersonalVO>('/note/list/my/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取未审核笔记列表 POST /note/list/reviewing */
export async function listReviewingNoteByPage(
  body: API.NoteQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageNote>('/note/list/reviewing', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取笔记列表（封装类） POST /note/list/vo */
export async function listNoteVoByPage(
  body: API.NoteQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageNoteVO>('/note/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 审核笔记 POST /note/review */
export async function reviewNote(body: API.NoteReviewRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/note/review', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新笔记 POST /note/update */
export async function updateNote(body: API.NoteUpdateRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/note/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
