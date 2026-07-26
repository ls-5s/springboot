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
