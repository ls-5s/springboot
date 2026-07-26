import request from '@/utils/request'

export interface UserInfoVO {
  id: number
  username: string
  nickname: string
  email: string
  avatar: string
  status: number
}

export interface UpdateUserDTO {
  nickname?: string
  email?: string
  avatar?: string
}

// 获取当前用户信息
export const getUserInfoApi = (): Promise<{ code: number; message: string; data: UserInfoVO }> => {
  return request.get('/user/info')
}

// 修改个人信息
export const updateUserInfoApi = (data: UpdateUserDTO): Promise<{ code: number; message: string; data: null }> => {
  return request.put('/user/info', data)
}
