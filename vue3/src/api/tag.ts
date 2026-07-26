import request from '@/utils/request'

export interface TagItem {
  id: number
  name: string
  createTime: string
}

// 标签列表（公开）
export const getTagsApi = (): Promise<{ code: number; message: string; data: TagItem[] }> => {
  return request.get('/tags')
}

// 新增标签（admin）
export const createTagApi = (data: { name: string }): Promise<{ code: number; message: string; data: null }> => {
  return request.post('/admin/tags', data)
}

// 修改标签（admin）
export const updateTagApi = (id: number, data: { name: string }): Promise<{ code: number; message: string; data: null }> => {
  return request.put(`/admin/tags/${id}`, data)
}

// 删除标签（admin）
export const deleteTagApi = (id: number): Promise<{ code: number; message: string; data: null }> => {
  return request.delete(`/admin/tags/${id}`)
}
