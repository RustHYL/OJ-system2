// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';



export async function searchTrueOrFalse(
    trueOrFalseSearchVO?: API.TrueOrFalseSearchRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<API.TrueOrFalseAdminVo[]>>('/api/trueOrFalse/list', {
        method: 'POST',
        data: {
            ...trueOrFalseSearchVO,
        },
        ...(options || {})
    });
}

/** 更新用户信息  POST /api/trueOrFalse/update */
export async function updateTrueOrFalseInfo(
    trueOrFalseInfo?: API.TrueOrFalseUpdateRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/trueOrFalse/update/backend', {
        method: 'POST',
        data: {
            ...trueOrFalseInfo
        },
        ...(options || {})
    });
}

/** 删除用户信息  POST /api/trueOrFalse/delete */
export async function deleteTrueOrFalseInfo(
    id?: string,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/trueOrFalse/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}


export async function addTrueOrFalseBackend(
  trueOrFalseAdminVo?: API.TrueOrFalseAdminVo,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<number>>('/api/trueOrFalse/add/backend', {
    method: 'POST',
    data: {
      ...trueOrFalseAdminVo
    },
    ...(options || {})
  });
}




export async function searchTestTrueOrFalse(
  trueOrFalseTestSearchVO?: API.TrueOrFalseTestSearchRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<API.TrueOrFalseTestAdminVo[]>>('/api/trueOrFalse/test/list', {
    method: 'POST',
    data: {
      ...trueOrFalseTestSearchVO,
    },
    ...(options || {})
  });
}

export async function deleteTestTrueOrFalse(
  id?: string,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/trueOrFalse/test/delete', {
    method: 'POST',
    data: {
      id
    },
    ...(options || {})
  });
}

export async function updateTestTrueOrFalse(
  trueOrFalseTestUpdateRequest?: API.TrueOrFalseTestUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/trueOrFalse/test/update', {
    method: 'POST',
    data: {
      ...trueOrFalseTestUpdateRequest
    },
    ...(options || {})
  });
}


export async function addTestTrueOrFalse(
  trueOrFalseTestAddRequest?: API.TrueOrFalseTestAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/trueOrFalse/test/add', {
    method: 'POST',
    data: {
      ...trueOrFalseTestAddRequest
    },
    ...(options || {})
  });
}
