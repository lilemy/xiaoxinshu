// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 创建题库题目关联 POST /questionBankQuestion/add */
export async function addQuestionBankQuestion(
  body: API.QuestionBankQuestionAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/questionBankQuestion/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 批量添加题目到题库 POST /questionBankQuestion/add/batch */
export async function batchAddQuestionsToBank(
  body: API.QuestionBankQuestionBatchRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/questionBankQuestion/add/batch', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据 id 获取题库题目关联（封装类） GET /questionBankQuestion/get/vo */
export async function getQuestionBankQuestionVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getQuestionBankQuestionVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseQuestionBankQuestionVO>('/questionBankQuestion/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 分页获取题库题目关联列表（封装类） POST /questionBankQuestion/list/vo */
export async function listQuestionBankQuestionVoByPage(
  body: API.QuestionBankQuestionQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageQuestionBankQuestionVO>('/questionBankQuestion/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 移除题库题目关联 POST /questionBankQuestion/remove */
export async function removeQuestionBankQuestion(
  body: API.QuestionBankQuestionRemoveRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/questionBankQuestion/remove', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 批量从题库移除题目 POST /questionBankQuestion/remove/batch */
export async function batchRemoveQuestionsFromBank(
  body: API.QuestionBankQuestionBatchRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/questionBankQuestion/remove/batch', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新题库题目关联 POST /questionBankQuestion/update */
export async function updateQuestionBankQuestion(
  body: API.QuestionBankQuestionUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/questionBankQuestion/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
