<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { loginApi, registerApi } from '@/api/auth'
import type { LoginParams, RegisterParams } from '@/api/auth'

const router = useRouter()

const activeTab = ref<number>(0)
const loading = ref<boolean>(false)
const errorMsg = ref<string>('')

const loginForm = reactive<LoginParams>({ username: '', password: '' })
const registerForm = reactive<RegisterParams>({ username: '', password: '', nickname: '', email: '' })
const confirmPassword = ref<string>('')

const switchTab = (tab: number) => {
  activeTab.value = tab
  errorMsg.value = ''
}

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    errorMsg.value = '请填写用户名和密码'
    return
  }
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await loginApi(loginForm)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('user', JSON.stringify(res.data))
    router.push('/')
  } catch (e: any) {
    errorMsg.value = e.message || '登录失败'
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  if (!registerForm.username || !registerForm.password || !registerForm.nickname) {
    errorMsg.value = '请填写必填项'
    return
  }
  if (registerForm.password !== confirmPassword.value) {
    errorMsg.value = '两次密码不一致'
    return
  }
  if (registerForm.password.length < 6) {
    errorMsg.value = '密码至少 6 位'
    return
  }
  loading.value = true
  errorMsg.value = ''
  try {
    await registerApi(registerForm)
    activeTab.value = 0
    loginForm.username = registerForm.username
    loginForm.password = ''
    alert('注册成功，请登录')
  } catch (e: any) {
    errorMsg.value = e.message || '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="wrapper">
    <div class="card">
      <!-- Logo -->
      <div class="logo-wrap">
        <svg class="logo-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
          <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
          <line x1="8" y1="7" x2="16" y2="7"/>
          <line x1="8" y1="11" x2="14" y2="11"/>
        </svg>
      </div>

      <!-- 标题 -->
      <h1 class="title">个人博客</h1>
      <p class="subtitle">记录每一次思考</p>

      <!-- Tab -->
      <div class="tabs">
        <button :class="{ on: activeTab === 0 }" @click="switchTab(0)">登 录</button>
        <button :class="{ on: activeTab === 1 }" @click="switchTab(1)">注 册</button>
      </div>

      <!-- 错误 -->
      <Transition name="fade">
        <p v-if="errorMsg" class="err">{{ errorMsg }}</p>
      </Transition>

      <!-- 登录表单 -->
      <Transition name="fade" mode="out-in">
        <form v-if="activeTab === 0" key="login" @submit.prevent="handleLogin" class="form">
          <input v-model="loginForm.username" placeholder="用户名" autocomplete="username" />
          <input v-model="loginForm.password" type="password" placeholder="密码" autocomplete="current-password" />
          <button class="btn" :disabled="loading">{{ loading ? '登录中...' : '登 录' }}</button>
        </form>

        <form v-else key="register" @submit.prevent="handleRegister" class="form">
          <div class="inline">
            <input v-model="registerForm.username" placeholder="用户名" autocomplete="username" />
            <input v-model="registerForm.nickname" placeholder="昵称" />
          </div>
          <input v-model="registerForm.email" type="email" placeholder="邮箱（选填）" autocomplete="email" />
          <input v-model="registerForm.password" type="password" placeholder="密码（至少6位）" autocomplete="new-password" />
          <input v-model="confirmPassword" type="password" placeholder="确认密码" autocomplete="new-password" />
          <button class="btn" :disabled="loading">{{ loading ? '注册中...' : '注 册' }}</button>
        </form>
      </Transition>
    </div>
  </div>
</template>

<style scoped>
.wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: radial-gradient(ellipse at top, #e6e9f0 0%, #eef1f5 100%);
  padding: 20px;
}

.card {
  width: 420px;
  background: #fff;
  border-radius: 24px;
  padding: 44px 40px 36px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.08), 0 0 0 1px rgba(0, 0, 0, 0.04);
  text-align: center;
}

/* Logo */
.logo-wrap {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.logo-icon {
  width: 28px;
  height: 28px;
  color: #fff;
}

/* 标题 */
.title {
  font-size: 26px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 4px;
  letter-spacing: 2px;
}

.subtitle {
  font-size: 13px;
  color: #aaa;
  margin: 0 0 28px;
}

/* Tab */
.tabs {
  display: flex;
  background: #f3f4f6;
  border-radius: 10px;
  padding: 4px;
  margin-bottom: 24px;
}

.tabs button {
  flex: 1;
  padding: 10px;
  border: none;
  background: transparent;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #999;
  cursor: pointer;
  transition: all 0.25s;
}

.tabs button.on {
  background: #fff;
  color: #333;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* 错误 */
.err {
  font-size: 13px;
  color: #e74c3c;
  background: #fef2f2;
  padding: 8px 12px;
  border-radius: 8px;
  margin: 0 0 16px;
}

/* 表单 */
.form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.inline {
  display: flex;
  gap: 10px;
}

.inline input {
  flex: 1;
}

.form input {
  width: 100%;
  padding: 14px 16px;
  border: 1.5px solid #e8e8e8;
  border-radius: 12px;
  font-size: 15px;
  color: #333;
  outline: none;
  transition: border-color 0.25s, box-shadow 0.25s;
  background: #fafafa;
  box-sizing: border-box;
}

.form input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.12);
  background: #fff;
}

.form input::placeholder {
  color: #c0c0c0;
}

.btn {
  padding: 14px;
  border: none;
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  letter-spacing: 4px;
  transition: opacity 0.25s, transform 0.25s;
  margin-top: 4px;
}

.btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

/* 过渡 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
