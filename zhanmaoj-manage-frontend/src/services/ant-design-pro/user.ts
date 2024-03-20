// @ts-ignore
/* eslint-disable */
import request from '@/plugins/globalRequest';


export async function searchUsers(
  userSearchVO?: API.UserInfoBySearchVO,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<API.UserAdminVo[]>>('/api/user/list/admin/vo', {
    method: 'POST',
    data: {
      ...userSearchVO
    },
    ...(options || {})
  });
}


/** 更新用户信息  POST /api/user/update */
export async function updateUserInfo(
    userInfo?: API.UserAdminVo,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/user/update', {
        method: 'POST',
        data: {
            ...userInfo
        },
        ...(options || {})
    });
}

/** 更新用户信息  POST /api/user/update/my */
export async function updateMyInfo(
  userInfo?: API.UserAdminVo,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/user/update/my', {
    method: 'POST',
    data: {
      ...userInfo
    },
    ...(options || {})
  });
}

/** 删除用户信息  POST /api/user/delete */
export async function deleteUserInfo(
    id?: number,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/user/delete', {
        method: 'POST',
        data: {
            id
        },
        ...(options || {})
    });
}
