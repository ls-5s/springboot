import request from '@/utils/request'

export interface FriendLinkItem {
  id: number
  name: string
  url: string
  logo: string
  sort: number
}

// 友链列表（公开）
export const getFriendLinksApi = (): Promise<{ code: number; message: string; data: FriendLinkItem[] }> => {
  return request.get('/friend-links')
}

// 新增友链（admin）
export const createFriendLinkApi = (data: Omit<FriendLinkItem, 'id'>): Promise<{ code: number; message: string; data: null }> => {
  return request.post('/admin/friend-links', data)
}

// 修改友链（admin）
export const updateFriendLinkApi = (id: number, data: Omit<FriendLinkItem, 'id'>): Promise<{ code: number; message: string; data: null }> => {
  return request.put(`/admin/friend-links/${id}`, data)
}

// 删除友链（admin）
export const deleteFriendLinkApi = (id: number): Promise<{ code: number; message: string; data: null }> => {
  return request.delete(`/admin/friend-links/${id}`)
}
