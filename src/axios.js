import axios from 'axios'
import Element from 'element-ui'
import router from './router'
import store from './store'


axios.defaults.baseURL = "http://localhost:8088"

// 前置拦截
axios.interceptors.request.use(config => {
  // 自动添加 token 到请求头
  const token = localStorage.getItem("token")
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

axios.interceptors.response.use(response => {
    let res = response.data;

    console.log("=================")
    console.log(res)
    console.log("=================")

    if (res.code === 200) {
      return response
    } else {

      Element.Message.error('错了哦，这是一条错误消息', {duration: 3 * 1000})

      return Promise.reject(response.data.msg)
    }
  },
  error => {
    console.log(error)
    const is401 = error.response && error.response.status === 401
    const msg = (error.response && error.response.data && error.response.data.msg)
      ? error.response.data.msg
      : (error.message || '请求失败')

    if (is401) {
      store.commit("REMOVE_INFO")
      router.push({ path: '/login', query: { msg } })
      // 401 不弹窗，仅由登录页 alert 展示提示，避免重复
    } else {
      Element.Message.error(msg, { duration: 3 * 1000 })
    }
    return Promise.reject(error)
  }
)
