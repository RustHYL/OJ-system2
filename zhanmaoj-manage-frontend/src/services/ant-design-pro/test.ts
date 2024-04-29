// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';



export async function searchTests(
    testSearchVO?: API.TestSearchRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<API.TestAdminVo[]>>('/api/test/list', {
        method: 'POST',
        data: {
            ...testSearchVO,
        },
        ...(options || {})
    });
}

/** 更新用户信息  POST /api/test/update */
export async function updateTestInfo(
    testInfo?: API.TestUpdateRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/test/update', {
        method: 'POST',
        data: {
            ...testInfo
        },
        ...(options || {})
    });
}

/** 删除用户信息  POST /api/test/delete */
export async function deleteTestInfo(
    id?: string,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/test/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}


export async function addTest(
    testAddRequest?: API.TestAddRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<number>>('/api/test/add', {
        method: 'POST',
        data: {
            ...testAddRequest
        },
        ...(options || {})
    });
}

export async function getTest(
  id?: string,
) {
  return request<API.BaseResponse<API.TestAdminVo>>('/api/test/get', {
    method: 'GET',
    data: {
      id,
    },
  });
}

export async function getQuestionTestIdTitleVO(
) {
  return request<API.BaseResponse<API.IdTitleVO>>('/api/question/idList', {
    method: 'GET',
  });
}


export async function getTrueOrFalseTestIdTitleVO(
) {
    return request<API.BaseResponse<API.IdTitleVO>>('/api/trueOrFalse/idList', {
        method: 'GET',
    });
}

export async function getChoiceQuestionTestIdTitleVO(
) {
    return request<API.BaseResponse<API.IdTitleVO>>('/api/choiceQuestion/idList', {
        method: 'GET',
    });
}
