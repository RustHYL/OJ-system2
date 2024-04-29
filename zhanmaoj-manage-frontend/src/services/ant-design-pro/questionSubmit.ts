// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';


export async function searchQuestionSubmits(
    questionSubmitSearchVO?: API.QuestionSubmitSearchRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<API.QuestionAdminVo[]>>('/api/question/question_submit/list', {
        method: 'POST',
        data: {
            ...questionSubmitSearchVO
        },
        ...(options || {})
    });
}


/** 更新用户信息  POST /api/question/update */
export async function updateQuestionSubmitInfo(
    questionSubmitInfo?: API.QuestionSubmitUpdateRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/question/question_submit/update/backend', {
        method: 'POST',
        data: {
            ...questionSubmitInfo
        },
        ...(options || {})
    });
}

/** 删除用户信息  POST /api/question/delete */
export async function deleteQuestionSubmitInfo(
    id?: string,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/question/question_submit/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}
