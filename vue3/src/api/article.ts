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

// 文章管理列表（admin，含草稿）
export const getAdminArticlesApi = (params: {
  page?: number
  size?: number
  keyword?: string
  status?: number
}): Promise<{ code: number; message: string; data: ArticlePage }> => {
  return request.get('/admin/articles', { params })
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

// 文章详情（公开）
export const getArticleDetailApi = (id: number): Promise<{ code: number; message: string; data: ArticleItem }> => {
  return request.get(`/articles/${id}`)
}

// 文章归档
export interface ArchiveItem {
  yearMonth: string
  count: number
  articles: { id: number; title: string; createTime: string }[]
}
export const getArchiveApi = (): Promise<{ code: number; message: string; data: ArchiveItem[] }> => {
  return request.get('/articles/archive')
}
