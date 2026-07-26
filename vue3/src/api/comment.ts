import request from '@/utils/request'

export interface CommentItem {
  id: number
  articleId: number
  userId: number
  userName: string
  content: string
  status: number
  createTime: string
}

export interface CommentVO {
  id: number
  userId: number
  userName: string
  avatar: string
  content: string
  createTime: string
  children: CommentVO[]
}

export interface CommentDTO {
  articleId: number
  parentId?: number
  content: string
}

// 文章评论列表（公开）
export const getCommentsByArticleApi = (articleId: number): Promise<{ code: number; message: string; data: CommentVO[] }> => {
  return request.get(`/comments/article/${articleId}`)
}

// 发表评论（auth）
export const createCommentApi = (data: CommentDTO): Promise<{ code: number; message: string; data: null }> => {
  return request.post('/comments', data)
}

// 待审核评论列表（admin）
export const getPendingCommentsApi = (): Promise<{ code: number; message: string; data: CommentItem[] }> => {
  return request.get('/admin/comments/pending')
}

// 审核通过（admin）
export const approveCommentApi = (id: number): Promise<{ code: number; message: string; data: null }> => {
  return request.put(`/admin/comments/${id}/approve`)
}

// 删除评论（admin）
export const deleteCommentApi = (id: number): Promise<{ code: number; message: string; data: null }> => {
  return request.delete(`/admin/comments/${id}`)
}
