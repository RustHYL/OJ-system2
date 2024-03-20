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
    id?: number,
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
