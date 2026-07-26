<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getSiteInfoApi, updateSiteConfigApi } from '@/api/sysConfig'

const config = reactive<Record<string, string>>({})
const saving = ref(false)
const msg = ref<string>('')

onMounted(async () => {
  try {
    const res = await getSiteInfoApi()
    Object.assign(config, res.data)
  } catch { /* ignore */ }
})

const handleSave = async () => {
  saving.value = true
  msg.value = ''
  try {
    await updateSiteConfigApi({ ...config })
    msg.value = '保存成功'
  } catch (e: any) {
    msg.value = e.message || '保存失败'
  } finally { saving.value = false }
}

// 预设配置项
const fields = [
  { key: 'site_title', label: '站点标题' },
  { key: 'site_subtitle', label: '站点副标题' },
  { key: 'about_me', label: '关于我' },
  { key: 'github', label: 'GitHub' },
  { key: 'email', label: '邮箱' },
  { key: 'icp', label: '备案号' },
]
</script>

<template>
  <div class="page">
    <div class="page-header"><h1>系统配置</h1></div>
    <div class="card">
      <Transition name="fade">
        <p v-if="msg" :class="msg === '保存成功' ? 'ok' : 'err'">{{ msg }}</p>
      </Transition>
      <div v-for="f in fields" :key="f.key" class="field">
        <label>{{ f.label }}</label>
        <input v-model="config[f.key]" :placeholder="`请输入${f.label}`" />
      </div>
      <div class="footer">
        <button class="save-btn" :disabled="saving" @click="handleSave">{{ saving ? '保存中...' : '保存配置' }}</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page { max-width: 640px; margin: 0 auto; }
.page-header { margin-bottom: 20px; }
.page-header h1 { font-size: 22px; font-weight: 700; color: #1a1a1a; margin: 0; }
.card { background: #fff; border-radius: 12px; padding: 28px 32px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.ok, .err { font-size: 13px; padding: 8px 12px; border-radius: 8px; margin-bottom: 16px; }
.ok { color: #27ae60; background: #f0fdf4; }
.err { color: #e74c3c; background: #fef2f2; }
.field { margin-bottom: 16px; }
.field label { display: block; font-size: 13px; font-weight: 500; color: #666; margin-bottom: 6px; }
.field input { width: 100%; padding: 10px 14px; border: 1.5px solid #e8e8e8; border-radius: 8px; font-size: 14px; outline: none; background: #fafafa; box-sizing: border-box; }
.field input:focus { border-color: #667eea; background: #fff; }
.footer { margin-top: 8px; }
.save-btn { padding: 10px 28px; border: none; border-radius: 8px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; font-size: 14px; font-weight: 500; cursor: pointer; }
.save-btn:disabled { opacity: 0.5; cursor: not-allowed; }
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
