// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 删除图片 POST /pic/delete */
export async function deletePicture(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/pic/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 编辑图片 POST /pic/edit */
export async function editPicture(body: API.PictureEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/pic/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 根据 id 获取图片（仅管理员） GET /pic/get */
export async function getPictureById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPictureByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePicture>('/pic/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 根据 id 获取图片封装信息 GET /pic/get/vo */
export async function getPictureVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPictureVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePictureVO>('/pic/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 分页获取图片列表（仅管理员） POST /pic/list */
export async function listPictureByPage(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePagePicture>('/pic/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页获取图片封装列表 POST /pic/list/vo */
export async function listPictureVoByPage(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePagePictureVO>('/pic/list/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 审核图片 POST /pic/review */
export async function doPictureReview(
  body: API.PictureReviewRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/pic/review', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取图片分类标签列表 GET /pic/tag_category */
export async function listPictureTagCategory(options?: { [key: string]: any }) {
  return request<API.BaseResponsePictureTagCategory>('/pic/tag_category', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 更新图片（仅管理员） POST /pic/update */
export async function updatePicture(
  body: API.PictureUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/pic/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 上传图片 POST /pic/upload */
export async function uploadPicture(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.uploadPictureParams,
  body: {},
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePictureVO>('/pic/upload', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    params: {
      ...params,
      pictureUploadRequest: undefined,
      ...params['pictureUploadRequest'],
    },
    data: body,
    ...(options || {}),
  });
}

/** 批量上传图片（仅管理员） POST /pic/upload/batch */
export async function uploadPictureByBatch(
  body: API.PictureUploadByBatchRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseInteger>('/pic/upload/batch', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 上传图片根据 URL POST /pic/upload/url */
export async function uploadPictureByUrl(
  body: API.PictureUploadRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePictureVO>('/pic/upload/url', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
