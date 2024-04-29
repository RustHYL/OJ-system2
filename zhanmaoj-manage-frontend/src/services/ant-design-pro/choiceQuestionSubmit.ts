// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';


export async function searchChoiceQuestionSubmits(
    choiceQuestionSubmitSearchVO?: API.ChoiceQuestionSubmitSearchRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<API.ChoiceQuestionSubmitAdminVo[]>>('/api/choiceQuestion/choiceQuestion_submit/list', {
        method: 'POST',
        data: {
            ...choiceQuestionSubmitSearchVO
        },
        ...(options || {})
    });
}


/** 更新用户信息  POST /api/choiceQuestion/update */
export async function updateChoiceQuestionSubmitInfo(
    choiceQuestionSubmitInfo?: API.ChoiceQuestionSubmitUpdateRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/choiceQuestion/choiceQuestion_submit/update/backend', {
        method: 'POST',
        data: {
            ...choiceQuestionSubmitInfo
        },
        ...(options || {})
    });
}

/** 删除用户信息  POST /api/choiceQuestion/delete */
export async function deleteChoiceQuestionSubmitInfo(
    id?: string,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/choiceQuestion/choiceQuestion_submit/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}
