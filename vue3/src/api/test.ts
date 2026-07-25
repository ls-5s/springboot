import request from '@/utils/request'

export const helloApi = (): Promise<{ code: number; message: string; data: string }> => {
  return request.get('/test/hello')
}
