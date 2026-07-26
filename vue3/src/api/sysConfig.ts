import request from '@/utils/request'

// 站点信息（公开）
export const getSiteInfoApi = (): Promise<{ code: number; message: string; data: Record<string, string> }> => {
  return request.get('/site-info')
}

// 修改站点配置（admin）
export const updateSiteConfigApi = (data: Record<string, string>): Promise<{ code: number; message: string; data: null }> => {
  return request.put('/admin/site-config', data)
}
