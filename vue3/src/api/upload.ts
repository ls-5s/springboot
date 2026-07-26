import request from '@/utils/request'

export const uploadApi = (file: File): Promise<{ code: number; message: string; data: { url: string; name: string } }> => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
