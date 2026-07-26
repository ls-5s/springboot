<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { helloApi } from '@/api/test'
import { logoutApi } from '@/api/auth'

const router = useRouter()
const message = ref<string>('')
const username = ref<string>('')

const fetchHello = async () => {
  try {
    const res = await helloApi()
    message.value = res.data
  } catch (e) {
    message.value = '请求失败，请检查后端是否启动'
  }
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

onMounted(() => {
  const stored = localStorage.getItem('user')
  if (stored) {
    try {
      const user = JSON.parse(stored)
      username.value = user.username || ''
    } catch { /* ignore */ }
  }
  fetchHello()
})
</script>

<template>
  <div class="home">
    <div class="header">
      <span class="user">{{ username ? `你好，${username}` : '' }}</span>
      <button class="logout-btn" @click="handleLogout">退出登录</button>
    </div>
    <h1>Spring Boot + Vue 3</h1>
    <p class="msg">{{ message }}</p>
  </div>
</template>

<style scoped>
.home {
  text-align: center;
  margin-top: 40px;
}
.header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  padding: 12px 24px;
  border-bottom: 1px solid #eee;
  margin-bottom: 60px;
}
.user {
  color: #666;
  font-size: 14px;
}
.logout-btn {
  padding: 6px 16px;
  border: 1px solid #dc2626;
  border-radius: 4px;
  background: #fff;
  color: #dc2626;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
}
.logout-btn:hover {
  background: #dc2626;
  color: #fff;
}
.msg {
  font-size: 24px;
  color: #42b883;
}
</style>
