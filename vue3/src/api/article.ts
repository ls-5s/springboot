import request from '@/utils/request'

export interface ArticleDTO {
  title: string
  summary?: string
  content?: string
  cover?: string
  categoryId?: number
  tagIds?: number[]
  status?: number   // 0草稿 1发布 2私密
  isTop?: number    // 0否 1是
}

export interface ArticleItem {
  id: number
  title: string
  summary: string
  content: string
  cover: string
  categoryId: number
  userId: number
  viewCount: number
  likeCount: number
  commentCount: number
  status: number
  isTop: number
  createTime: string
  updateTime: string
}

export interface ArticlePage {
  records: ArticleItem[]
  total: number
  size: number
  current: number
  pages: number
}

// 文章列表（公开接口）
export const getArticlesApi = (params: {
  page?: number
  size?: number
  categoryId?: number
  tagId?: number
  keyword?: string
}): Promise<{ code: number; message: string; data: ArticlePage }> => {
  return request.get('/articles', { params })
}

// 发布文章
export const createArticleApi = (data: ArticleDTO): Promise<{ code: number; message: string; data: number }> => {
  return request.post('/articles', data)
}

// 修改文章
export const updateArticleApi = (id: number, data: ArticleDTO): Promise<{ code: number; message: string; data: null }> => {
  return request.put(`/articles/${id}`, data)
}

// 删除文章
export const deleteArticleApi = (id: number): Promise<{ code: number; message: string; data: null }> => {
  return request.delete(`/articles/${id}`)
}
