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
    id?: string,
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


export async function resetUserPassword(
  id?: string,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponse<boolean>>('/api/user/reset/password', {
    method: 'POST',
    data: {
      id
    },
    ...(options || {})
  });
}


export async function updateFile(
  multipartFile?: File,
  biz?: string,
  options?: { [key: string]: any }
) {
  const formData = new FormData();
  // @ts-ignore
    formData.append('file', multipartFile); // 'file' 是后端接口预期的字段
  if (biz) {
      formData.append('biz', biz); // 添加业务类型，如果有的话
  }
  return request<API.BaseResponse<String>>('/api/file/upload', {
    method: 'POST',
    data: formData,
    ...(options || {})
  });
}

export async function updateAvatarUrl(
    avatarUrl: string,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/user/update/my/avatarUrl', {
        method: 'POST',
        data: avatarUrl,
        ...(options || {})
    });
}

export async function updateMyPhone(
    sms?: API.SMS,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/user/update/my/phone', {
        method: 'POST',
        data: {
            ...sms
        },
        ...(options || {})
    });
}


export async function verifyUserPassword(
    password: string,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/user/verify/password', {
        method: 'POST',
        data: password,
        ...(options || {})
    });
}

export async function updateMyPassword(
    userUpdateMyPasswordRequest?: API.UserUpdateMyPasswordRequest,
    options?: { [key: string]: any }
) {
    return request<API.BaseResponse<boolean>>('/api/user/update/my/password', {
        method: 'POST',
        data: userUpdateMyPasswordRequest,
        ...(options || {})
    });
}
