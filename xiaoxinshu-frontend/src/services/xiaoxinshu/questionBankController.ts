// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 添加题库 POST /questionBank/add */
export async function addQuestionBank(
  body: API.QuestionBankAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/questionBank/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除题库 POST /questionBank/delete */
export async function deleteQuestionBank(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/questionBank/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取题库列表信息 GET /questionBank/get/list */
export async function getQuestionBankList(options?: { [key: string]: any }) {
  return request<API.BaseResponseListQuestionBankListVO>('/questionBank/get/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 根据id获取题库脱敏信息 GET /questionBank/get/vo */
export async function getQuestionBankVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getQuestionBankVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseQuestionBankVO>('/questionBank/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 分页获取题库列表（仅管理员可用） POST /questionBank/list */
export async function listQuestionBankByPage(
  body: API.QuestionBankQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionBank>('/questionBank/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取题库列表（封装类） POST /questionBank/list/vo */
export async function listQuestionBankVoByPage(
  body: API.QuestionBankQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionBankVO>('/questionBank/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新题库 POST /questionBank/update */
export async function updateQuestionBank(
  body: API.QuestionBankUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/questionBank/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
