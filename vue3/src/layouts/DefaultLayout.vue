<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { logoutApi } from '@/api/auth'
import { getUserInfoApi } from '@/api/user'

const router = useRouter()
const route = useRoute()
const collapsed = ref<boolean>(false)
const username = ref<string>('')

// 侧边菜单
const menus = [
  { path: '/', name: '首页', icon: 'home' },
  { path: '/articles', name: '文章管理', icon: 'article' },
  { path: '/categories', name: '分类管理', icon: 'category' },
  { path: '/tags', name: '标签管理', icon: 'tag' },
  { path: '/comments', name: '评论管理', icon: 'comment' },
  { path: '/links', name: '友链管理', icon: 'link' },
  { path: '/settings', name: '系统配置', icon: 'settings' },
]

const isActive = (path: string) => {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}

const navigate = (path: string) => {
  router.push(path)
}

const handleLogout = async () => {
  try {
    await logoutApi()
  } finally {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    router.push('/login')
  }
}

onMounted(async () => {
  try {
    const res = await getUserInfoApi()
    username.value = res.data.nickname || res.data.username
  } catch {
    const stored = localStorage.getItem('user')
    if (stored) {
      try {
        const user = JSON.parse(stored)
        username.value = user.nickname || user.username || ''
      } catch { /* ignore */ }
    }
  }
})
</script>

<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <aside :class="['sidebar', { collapsed }]">
      <div class="logo" @click="navigate('/')">
        <svg class="logo-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
          <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
          <line x1="8" y1="7" x2="16" y2="7"/>
          <line x1="8" y1="11" x2="14" y2="11"/>
        </svg>
        <span v-show="!collapsed" class="logo-text">个人博客</span>
      </div>

      <nav class="nav">
        <div
          v-for="m in menus"
          :key="m.path"
          :class="['nav-item', { active: isActive(m.path) }]"
          @click="navigate(m.path)"
        >
          <span class="nav-icon">
            <svg v-if="m.icon === 'home'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/></svg>
            <svg v-else-if="m.icon === 'article'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14,2 14,8 20,8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>
            <svg v-else-if="m.icon === 'category'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
            <svg v-else-if="m.icon === 'tag'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"/><line x1="7" y1="7" x2="7.01" y2="7"/></svg>
            <svg v-else-if="m.icon === 'comment'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
            <svg v-else-if="m.icon === 'link'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"/><path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"/></svg>
            <svg v-else-if="m.icon === 'settings'" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83-2.83l.06-.06A1.65 1.65 0 0 0 4.68 15a1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 2.83-2.83l.06.06A1.65 1.65 0 0 0 9 4.68a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 2.83l-.06.06A1.65 1.65 0 0 0 19.4 9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/></svg>
          </span>
          <span v-show="!collapsed" class="nav-text">{{ m.name }}</span>
        </div>
      </nav>

      <div class="sidebar-footer">
        <button class="collapse-btn" @click="collapsed = !collapsed">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polyline v-if="collapsed" points="9,18 15,12 9,6"/>
            <polyline v-else points="15,18 9,12 15,6"/>
          </svg>
        </button>
      </div>
    </aside>

    <!-- 主区域 -->
    <div class="main">
      <header class="topbar">
        <span class="greet" @click="router.push('/profile')">你好，{{ username || '用户' }}</span>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </header>
      <main class="body">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout {
  display: flex;
  min-height: 100vh;
  background: #f5f6fa;
}

/* 侧边栏 */
.sidebar {
  width: 220px;
  background: linear-gradient(180deg, #1e1e2f 0%, #2a2a3e 100%);
  color: #ccc;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  transition: width 0.25s;
}
.sidebar.collapsed {
  width: 64px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px 18px;
  cursor: pointer;
  border-bottom: 1px solid rgba(255,255,255,0.06);
}
.logo-icon {
  width: 26px;
  height: 26px;
  color: #667eea;
  flex-shrink: 0;
}
.logo-text {
  font-size: 16px;
  font-weight: 700;
  color: #fff;
  white-space: nowrap;
}

/* 导航 */
.nav {
  flex: 1;
  padding: 12px 10px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}
.nav-item:hover {
  background: rgba(255,255,255,0.08);
  color: #fff;
}
.nav-item.active {
  background: rgba(102,126,234,0.2);
  color: #667eea;
}
.nav-icon {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
}
.nav-icon svg {
  width: 20px;
  height: 20px;
}
.nav-text {
  font-size: 14px;
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 12px 10px;
  border-top: 1px solid rgba(255,255,255,0.06);
}
.collapse-btn {
  width: 100%;
  display: flex;
  justify-content: center;
  padding: 8px;
  border: none;
  background: transparent;
  color: #888;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.2s;
}
.collapse-btn svg {
  width: 18px;
  height: 18px;
}
.collapse-btn:hover {
  background: rgba(255,255,255,0.08);
  color: #fff;
}

/* 主区域 */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.topbar {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  padding: 12px 28px;
  background: #fff;
  border-bottom: 1px solid #eee;
  flex-shrink: 0;
}
.greet {
  color: #888;
  font-size: 14px;
  cursor: pointer;
  transition: color 0.2s;
}
.greet:hover {
  color: #667eea;
}
.logout-btn {
  padding: 6px 16px;
  border: 1px solid #dc2626;
  border-radius: 6px;
  background: #fff;
  color: #dc2626;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}
.logout-btn:hover {
  background: #dc2626;
  color: #fff;
}

.body {
  flex: 1;
  padding: 24px 28px;
  overflow-y: auto;
}
</style>
