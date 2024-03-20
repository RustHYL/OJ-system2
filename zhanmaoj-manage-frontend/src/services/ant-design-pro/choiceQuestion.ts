// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';



export async function searchChoiceQuestions(
    choiceQuestionSearchVO?: API.ChoiceQuestionSearchRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<API.ChoiceQuestionAdminVo[]>>('/api/choiceQuestion/list', {
        method: 'POST',
        data: {
            ...choiceQuestionSearchVO,
        },
        ...(options || {})
    });
}

/** 更新用户信息  POST /api/choiceQuestion/update */
export async function updateChoiceQuestionInfo(
    choiceQuestionInfo?: API.ChoiceQuestionUpdateRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/choiceQuestion/update/backend', {
        method: 'POST',
        data: {
            ...choiceQuestionInfo
        },
        ...(options || {})
    });
}

/** 删除用户信息  POST /api/choiceQuestion/delete */
export async function deleteChoiceQuestionInfo(
    id?: number,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/choiceQuestion/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}


export async function addChoiceQuestionsBackend(
  choiceQuestionAdminVo?: API.ChoiceQuestionAdminVo,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<number>>('/api/choiceQuestion/add/backend', {
    method: 'POST',
    data: {
      ...choiceQuestionAdminVo
    },
    ...(options || {})
  });
}
