// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 创建题目（管理员） POST /question/add */
export async function addQuestion(body: API.QuestionAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/question/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 创建题目 POST /question/create */
export async function createQuestion(
  body: API.QuestionCreateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/question/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除题目 POST /question/delete */
export async function deleteQuestion(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/question/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 批量删除题目 POST /question/delete/batch */
export async function deleteQuestionBatch(
  body: API.QuestionBatchDeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/question/delete/batch', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 编辑题目 POST /question/edit */
export async function editQuestion(
  body: API.QuestionEditRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/question/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据 id 获取个人题目（封装类） GET /question/get/my/vo */
export async function getQuestionPersonalById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getQuestionPersonalByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseQuestionPersonalVO>('/question/get/my/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 根据 id 获取题目（封装类） GET /question/get/vo */
export async function getQuestionVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getQuestionVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseQuestionVO>('/question/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 分页获取题目列表（仅管理员可用） POST /question/list */
export async function listQuestionByPage(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestion>('/question/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取当前登录用户创建的题目列表 POST /question/list/my/vo */
export async function listMyQuestionVoByPage(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionPersonalVO>('/question/list/my/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取未审核题目列表 POST /question/list/reviewing */
export async function listReviewingQuestionByPage(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestion>('/question/list/reviewing', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取题目列表（封装类） POST /question/list/vo */
export async function listQuestionVoByPage(
  body: API.QuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionVO>('/question/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 审核题目 POST /question/review */
export async function reviewQuestion(
  body: API.QuestionReviewRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/question/review', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新题目 POST /question/update */
export async function updateQuestion(
  body: API.QuestionUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/question/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
