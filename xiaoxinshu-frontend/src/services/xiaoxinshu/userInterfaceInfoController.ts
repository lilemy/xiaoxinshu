// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** 添加用户接口关系 POST /userInterfaceInfo/add */
export async function addUserInterfaceInfo(
  body: API.UserInterfaceInfoAddRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/userInterfaceInfo/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 更新用户接口调用次数 POST /userInterfaceInfo/update/leftNum */
export async function updateUserInterfaceInfoLeftNum(
  body: API.UserInterfaceInfoUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean>('/userInterfaceInfo/update/leftNum', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
