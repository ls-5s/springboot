import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：未登录跳转登录页
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    // 已登录则跳过登录页
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    // 未登录则跳转登录页
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
