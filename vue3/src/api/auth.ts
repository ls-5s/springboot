import request from '@/utils/request'

export interface LoginParams {
  username: string
  password: string
}

export interface RegisterParams {
  username: string
  password: string
  nickname: string
  email: string
}

export interface LoginResult {
  token: string
  userId: number
  username: string
  nickname: string
}

// 登录
export const loginApi = (data: LoginParams): Promise<{ code: number; message: string; data: LoginResult }> => {
  return request.post('/auth/login', data)
}

// 注册
export const registerApi = (data: RegisterParams): Promise<{ code: number; message: string; data: null }> => {
  return request.post('/auth/register', data)
}

// 退出登录
export const logoutApi = (): Promise<{ code: number; message: string; data: null }> => {
  return request.post('/auth/logout')
}
