import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例，配置基础 URL（指向后端 8080 端口，带 /api 前缀）
const request = axios.create({
  baseURL: 'http://localhost:8080/api',   // 后端接口统一前缀
  timeout: 10000,                         // 请求超时时间 10 秒
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * 请求拦截器
 * 在发送请求之前，自动从 localStorage 中取出 Token，添加到请求头 Authorization 中
 */
request.interceptors.request.use(
  config => {
    // 从本地存储获取 Token（登录成功后由前端存入 localStorage）
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 * 统一处理后端返回的数据格式（Result 结构）
 * - 如果 code === 200，直接返回 data 字段（简化组件中的取值）
 * - 如果 code === 401，清空本地 Token 并跳转到登录页
 * - 其他错误码，弹出 ElMessage 提示，并 reject
 */
request.interceptors.response.use(
  response => {
    const res = response.data

    // 后端统一返回格式：{ code, message, data }
    if (res.code === 200) {
      // 成功，返回 data 字段（组件中使用时无需再 .data）
      return res.data
    } else {
      // 业务错误：显示错误信息
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    // 网络错误或 HTTP 状态码非 2xx
    console.error('响应拦截器错误:', error)

    if (error.response) {
      const { status, data } = error.response

      // 401 未登录或 Token 过期
      if (status === 401) {
        ElMessage.error('登录已过期，请重新登录')
        // 清除本地存储的 Token 和用户信息
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        // 跳转到登录页（如果当前不在登录页）
        if (window.location.pathname !== '/login') {
          window.location.href = '/login'
        }
      } else if (status === 403) {
        ElMessage.error('没有权限访问')
      } else if (status === 500) {
        ElMessage.error('服务器内部错误')
      } else {
        ElMessage.error(data?.message || `请求失败 (${status})`)
      }
    } else if (error.request) {
      // 请求已发出，但未收到响应（网络问题或后端未启动）
      ElMessage.error('网络错误，请检查后端服务是否已启动')
    } else {
      ElMessage.error('请求配置错误')
    }

    return Promise.reject(error)
  }
)

export default request