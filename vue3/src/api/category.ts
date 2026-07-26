import request from '@/utils/request'

export interface CategoryItem {
  id: number
  name: string
  sort: number
  createTime: string
}

// 分类列表（公开）
export const getCategoriesApi = (): Promise<{ code: number; message: string; data: CategoryItem[] }> => {
  return request.get('/categories')
}

// 新增分类（admin）
export const createCategoryApi = (data: { name: string; sort: number }): Promise<{ code: number; message: string; data: null }> => {
  return request.post('/admin/categories', data)
}

// 修改分类（admin）
export const updateCategoryApi = (id: number, data: { name: string; sort: number }): Promise<{ code: number; message: string; data: null }> => {
  return request.put(`/admin/categories/${id}`, data)
}

// 删除分类（admin）
export const deleteCategoryApi = (id: number): Promise<{ code: number; message: string; data: null }> => {
  return request.delete(`/admin/categories/${id}`)
}
