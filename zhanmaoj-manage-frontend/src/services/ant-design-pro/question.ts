// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';



export async function searchQuestions(
    questionSearchVO?: API.QuestionSearchRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<API.QuestionAdminVo[]>>('/api/question/list', {
        method: 'POST',
        data: {
            ...questionSearchVO,
        },
        ...(options || {})
    });
}

/** 更新用户信息  POST /api/question/update */
export async function updateQuestionInfo(
    questionInfo?: API.QuestionUpdateRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/question/update/backend', {
        method: 'POST',
        data: {
            ...questionInfo
        },
        ...(options || {})
    });
}

/** 删除用户信息  POST /api/question/delete */
export async function deleteQuestionInfo(
    id?: number,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/question/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}


export async function addQuestionsBackend(
  questionAdminVo?: API.QuestionAdminVo,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<number>>('/api/question/add/backend', {
    method: 'POST',
    data: {
      ...questionAdminVo
    },
    ...(options || {})
  });
}


export async function searchTestQuestions(
  questionTestSearchVO?: API.QuestionTestSearchRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<API.QuestionTestAdminVo[]>>('/api/question/test/list', {
    method: 'POST',
    data: {
      ...questionTestSearchVO,
    },
    ...(options || {})
  });
}

export async function deleteTestQuestion(
  id?: number,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/question/test/delete', {
    method: 'POST',
    data: {
      id
    },
    ...(options || {})
  });
}

export async function updateTestQuestion(
  questionTestUpdateRequest?: API.QuestionTestUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/question/test/update', {
    method: 'POST',
    data: {
      questionTestUpdateRequest
    },
    ...(options || {})
  });
}

export async function addTestQuestion(
  questionTestAddRequest?: API.QuestionTestAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/question/test/add', {
    method: 'POST',
    data: {
      questionTestAddRequest
    },
    ...(options || {})
  });
}

