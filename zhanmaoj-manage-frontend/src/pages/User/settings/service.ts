import { request } from '@umijs/max';


export async function queryCurrent(): Promise<{ data: API.BaseResponse<API.CurrentUser> }> {
  return request('/api/user/get/login');
}

export async function query() {
  return request('/api/users');
}
