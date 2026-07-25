import axios from 'axios'

interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const data = response.data as ApiResponse
    if (data.code !== 200) {
      console.error(data.message)
      return Promise.reject(new Error(data.message))
    }
    return response.data
  },
  error => {
    console.error('网络错误:', error.message)
    return Promise.reject(error)
  }
)

export default request
