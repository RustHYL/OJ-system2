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


export async function searchTestChoiceQuestion(
  choiceQuestionTestSearchVO?: API.ChoiceQuestionTestSearchRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<API.ChoiceQuestionTestAdminVo[]>>('/api/choiceQuestion/test/list', {
    method: 'POST',
    data: {
      ...choiceQuestionTestSearchVO,
    },
    ...(options || {})
  });
}

export async function deleteTestChoiceQuestion(
  id?: number,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/choiceQuestion/test/delete', {
    method: 'POST',
    data: {
      id
    },
    ...(options || {})
  });
}

export async function updateTestChoiceQuestion(
  choiceQuestionTestUpdateRequest?: API.ChoiceQuestionTestUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/choiceQuestion/test/update', {
    method: 'POST',
    data: {
      choiceQuestionTestUpdateRequest
    },
    ...(options || {})
  });
}


export async function addTestChoiceQuestion(
  choiceQuestionTestAddRequest?: API.ChoiceQuestionTestAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/choiceQuestion/test/add', {
    method: 'POST',
    data: {
      choiceQuestionTestAddRequest
    },
    ...(options || {})
  });
}
