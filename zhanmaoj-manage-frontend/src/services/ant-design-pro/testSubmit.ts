// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';


export async function searchTestSubmits(
  testSubmitSearchVO?: API.TestSubmitSearchRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<API.TestSubmitAdminVo[]>>('/api/test/test_submit/list', {
    method: 'POST',
    data: {
      ...testSubmitSearchVO
    },
    ...(options || {})
  });
}


/** 更新用户信息  POST /api/test/update */
export async function updateTestSubmitInfo(
  testSubmitInfo?: API.TestSubmitUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/test/test_submit/update/backend', {
    method: 'POST',
    data: {
      ...testSubmitInfo
    },
    ...(options || {})
  });
}

/** 删除用户信息  POST /api/test/delete */
export async function deleteTestSubmitInfo(
  id?: string,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/test/test_submit/delete', {
    method: 'POST',
    data: {
      id
    },
    ...(options || {})
  });
}
