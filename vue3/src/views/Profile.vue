<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getUserInfoApi, updateUserInfoApi } from '@/api/user'
import type { UpdateUserDTO } from '@/api/user'

const editing = ref<boolean>(false)
const saving = ref<boolean>(false)
const errorMsg = ref<string>('')
const successMsg = ref<string>('')

const form = reactive<UpdateUserDTO>({
  nickname: '',
  email: '',
  avatar: ''
})

onMounted(async () => {
  try {
    const res = await getUserInfoApi()
    form.nickname = res.data.nickname || ''
    form.email = res.data.email || ''
    form.avatar = res.data.avatar || ''
  } catch { /* ignore */ }
})

const handleSave = async () => {
  saving.value = true
  errorMsg.value = ''
  successMsg.value = ''
  try {
    await updateUserInfoApi(form)
    successMsg.value = '保存成功'
    const stored = localStorage.getItem('user')
    if (stored) {
      try {
        const user = JSON.parse(stored)
        user.nickname = form.nickname
        localStorage.setItem('user', JSON.stringify(user))
      } catch { /* ignore */ }
    }
    editing.value = false
  } catch (e: any) {
    errorMsg.value = e.message || '保存失败'
  } finally {
    saving.value = false
  }
}

const handleCancel = async () => {
  try {
    const res = await getUserInfoApi()
    form.nickname = res.data.nickname || ''
    form.email = res.data.email || ''
    form.avatar = res.data.avatar || ''
  } catch { /* ignore */ }
  editing.value = false
  errorMsg.value = ''
}
</script>

<template>
  <div class="profile">
    <div class="page-header">
      <h1>个人信息</h1>
    </div>

    <div class="card">
      <Transition name="fade">
        <div v-if="errorMsg || successMsg" key="msg">
          <p v-if="errorMsg" class="err">{{ errorMsg }}</p>
          <p v-if="successMsg" class="ok">{{ successMsg }}</p>
        </div>
      </Transition>

      <!-- 查看模式 -->
      <div v-if="!editing" class="info-list">
        <div class="row"><span class="label">昵称</span><span>{{ form.nickname || '-' }}</span></div>
        <div class="row"><span class="label">邮箱</span><span>{{ form.email || '-' }}</span></div>
        <div class="row"><span class="label">头像</span><span>{{ form.avatar || '-' }}</span></div>
        <button class="edit-btn" @click="editing = true; errorMsg = ''; successMsg = ''">编辑</button>
      </div>

      <!-- 编辑模式 -->
      <div v-else class="info-list">
        <div class="row">
          <span class="label">昵称</span>
          <input v-model="form.nickname" placeholder="请输入昵称" />
        </div>
        <div class="row">
          <span class="label">邮箱</span>
          <input v-model="form.email" type="email" placeholder="请输入邮箱" />
        </div>
        <div class="row">
          <span class="label">头像</span>
          <input v-model="form.avatar" placeholder="请输入头像 URL" />
        </div>
        <div class="btn-row">
          <button class="save-btn" :disabled="saving" @click="handleSave">{{ saving ? '保存中...' : '保存' }}</button>
          <button class="cancel-btn" @click="handleCancel">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile {
  max-width: 640px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}
.page-header h1 {
  font-size: 22px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 28px 32px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}

/* 消息 */
.err, .ok {
  font-size: 13px;
  padding: 8px 12px;
  border-radius: 8px;
  margin: 0 0 16px;
}
.err { color: #e74c3c; background: #fef2f2; }
.ok  { color: #27ae60; background: #f0fdf4; }

/* 信息行 */
.info-list { display: flex; flex-direction: column; gap: 14px; }
.row { display: flex; align-items: center; gap: 12px; font-size: 14px; color: #333; }
.label { width: 50px; color: #999; flex-shrink: 0; }
.row input {
  flex: 1;
  padding: 8px 12px;
  border: 1.5px solid #e8e8e8;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  background: #fafafa;
  transition: border-color 0.2s;
}
.row input:focus { border-color: #667eea; background: #fff; }

.edit-btn {
  margin-top: 8px;
  padding: 8px 20px;
  border: 1px solid #667eea;
  border-radius: 8px;
  background: #fff;
  color: #667eea;
  font-size: 14px;
  cursor: pointer;
  align-self: flex-start;
  transition: all 0.2s;
}
.edit-btn:hover { background: #667eea; color: #fff; }

.btn-row { display: flex; gap: 12px; margin-top: 8px; }
.save-btn, .cancel-btn {
  padding: 8px 20px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}
.save-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; }
.save-btn:hover { opacity: 0.9; }
.save-btn:disabled { opacity: 0.5; cursor: not-allowed; }
.cancel-btn { background: #f3f4f6; color: #666; }
.cancel-btn:hover { background: #e5e7eb; }

.fade-enter-active, .fade-leave-active { transition: opacity 0.2s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
