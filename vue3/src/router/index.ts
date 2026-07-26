import { createRouter, createWebHistory } from 'vue-router'
import DefaultLayout from '@/layouts/DefaultLayout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    component: DefaultLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue')
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue')
      },
      // 占位路由：后续页面开发时替换
      {
        path: 'articles',
        name: 'Articles',
        component: () => import('@/views/Placeholder.vue')
      },
      {
        path: 'categories',
        name: 'Categories',
        component: () => import('@/views/Placeholder.vue')
      },
      {
        path: 'tags',
        name: 'Tags',
        component: () => import('@/views/Placeholder.vue')
      },
      {
        path: 'comments',
        name: 'Comments',
        component: () => import('@/views/Placeholder.vue')
      },
      {
        path: 'links',
        name: 'Links',
        component: () => import('@/views/Placeholder.vue')
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/Placeholder.vue')
      }
    ]
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
    if (token) {
      next('/')
    } else {
      next()
    }
  } else {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
