// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';


export async function searchTrueOrFalseSubmits(
    trueOrFalseSubmitSearchVO?: API.TrueOrFalseSubmitSearchRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<API.TrueOrFalseSubmitAdminVo[]>>('/api/trueOrFalse/trueOrFalse_submit/list', {
        method: 'POST',
        data: {
            ...trueOrFalseSubmitSearchVO
        },
        ...(options || {})
    });
}


/** 更新用户信息  POST /api/trueOrFalse/update */
export async function updateTrueOrFalseSubmitInfo(
    trueOrFalseSubmitInfo?: API.TrueOrFalseSubmitUpdateRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/trueOrFalse/trueOrFalse_submit/update/backend', {
        method: 'POST',
        data: {
            ...trueOrFalseSubmitInfo
        },
        ...(options || {})
    });
}

/** 删除用户信息  POST /api/trueOrFalse/delete */
export async function deleteTrueOrFalseSubmitInfo(
    id?: string,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/trueOrFalse/trueOrFalse_submit/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}
