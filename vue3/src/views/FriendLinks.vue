<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getFriendLinksApi, createFriendLinkApi, updateFriendLinkApi, deleteFriendLinkApi } from '@/api/friendLink'
import type { FriendLinkItem } from '@/api/friendLink'

const list = ref<FriendLinkItem[]>([])
const showModal = ref(false)
const editingId = ref<number | null>(null)
const saving = ref(false)
const form = reactive({ name: '', url: '', logo: '', sort: 0 })

const fetchList = async () => {
  try { const res = await getFriendLinksApi(); list.value = res.data } catch { list.value = [] }
}

const handleAdd = () => { editingId.value = null; form.name = ''; form.url = ''; form.logo = ''; form.sort = 0; showModal.value = true }
const handleEdit = (item: FriendLinkItem) => {
  editingId.value = item.id; form.name = item.name; form.url = item.url; form.logo = item.logo || ''; form.sort = item.sort; showModal.value = true
}

const handleSave = async () => {
  if (!form.name.trim() || !form.url.trim()) return
  saving.value = true
  try {
    if (editingId.value) { await updateFriendLinkApi(editingId.value, form) }
    else { await createFriendLinkApi(form) }
    showModal.value = false; fetchList()
  } catch { /* ignore */ } finally { saving.value = false }
}

const handleDelete = async (item: FriendLinkItem) => {
  if (!confirm(`确定删除「${item.name}」？`)) return
  try { await deleteFriendLinkApi(item.id); fetchList() } catch { /* ignore */ }
}

onMounted(fetchList)
</script>

<template>
  <div class="page">
    <div class="page-header"><h1>友链管理</h1><button class="btn-primary" @click="handleAdd">+ 新增友链</button></div>
    <div class="card">
      <table v-if="list.length" class="table">
        <thead><tr><th>名称</th><th>地址</th><th>排序</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td class="name">{{ item.logo ? '🔗 ' : '' }}{{ item.name }}</td>
            <td class="url"><a :href="item.url" target="_blank">{{ item.url }}</a></td>
            <td>{{ item.sort }}</td>
            <td class="actions">
              <button class="act edit" @click="handleEdit(item)">编辑</button>
              <button class="act del" @click="handleDelete(item)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty">暂无友链</div>
    </div>

    <Teleport to="body">
      <div v-if="showModal" class="overlay" @click.self="showModal = false">
        <div class="modal">
          <div class="modal-header"><h2>{{ editingId ? '编辑' : '新增' }}友链</h2><button class="close" @click="showModal = false">&times;</button></div>
          <div class="modal-body">
            <div class="field"><label>名称</label><input v-model="form.name" placeholder="网站名称" /></div>
            <div class="field"><label>地址</label><input v-model="form.url" placeholder="https://..." /></div>
            <div class="field"><label>Logo URL</label><input v-model="form.logo" placeholder="选填" /></div>
            <div class="field"><label>排序</label><input v-model.number="form.sort" type="number" /></div>
          </div>
          <div class="modal-footer">
            <button class="cancel-btn" @click="showModal = false">取消</button>
            <button class="save-btn" :disabled="saving" @click="handleSave">{{ saving ? '保存中...' : '保存' }}</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.page { max-width: 900px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h1 { font-size: 22px; font-weight: 700; color: #1a1a1a; margin: 0; }
.btn-primary { padding: 10px 22px; border: none; border-radius: 8px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; font-size: 14px; cursor: pointer; }
.card { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; }
.table { width: 100%; border-collapse: collapse; }
.table th { text-align: left; padding: 14px 20px; font-size: 13px; color: #999; background: #fafbfc; border-bottom: 1px solid #eee; }
.table td { padding: 14px 20px; font-size: 14px; color: #333; border-bottom: 1px solid #f5f5f5; }
.table tbody tr:hover { background: #fafbfc; }
.name { font-weight: 500; }
.url { max-width: 280px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.url a { color: #667eea; }
.actions { white-space: nowrap; }
.act { padding: 4px 14px; border-radius: 5px; font-size: 13px; cursor: pointer; border: 1px solid #e5e7eb; background: #fff; margin-right: 8px; }
.act.edit { color: #667eea; border-color: #667eea; } .act.edit:hover { background: #667eea; color: #fff; }
.act.del { color: #dc2626; border-color: #dc2626; } .act.del:hover { background: #dc2626; color: #fff; }
.empty { padding: 64px 24px; text-align: center; color: #ccc; font-size: 14px; }
.overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.35); display: flex; justify-content: center; align-items: flex-start; padding-top: 80px; z-index: 1000; }
.modal { background: #fff; border-radius: 16px; width: 520px; box-shadow: 0 20px 60px rgba(0,0,0,0.15); }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 22px 28px 16px; }
.modal-header h2 { font-size: 18px; margin: 0; }
.close { border: none; background: transparent; font-size: 24px; color: #999; cursor: pointer; }
.modal-body { padding: 0 28px 20px; }
.modal-footer { display: flex; justify-content: flex-end; gap: 12px; padding: 16px 28px 22px; border-top: 1px solid #f0f0f0; }
.field { margin-bottom: 16px; }
.field label { display: block; font-size: 13px; font-weight: 500; color: #666; margin-bottom: 6px; }
.field input { width: 100%; padding: 10px 14px; border: 1.5px solid #e8e8e8; border-radius: 8px; font-size: 14px; outline: none; background: #fafafa; box-sizing: border-box; }
.field input:focus { border-color: #667eea; background: #fff; }
.cancel-btn, .save-btn { padding: 10px 28px; border-radius: 8px; font-size: 14px; cursor: pointer; }
.cancel-btn { border: 1px solid #e5e7eb; background: #fff; color: #666; }
.save-btn { border: none; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; font-weight: 500; }
.save-btn:disabled { opacity: 0.5; cursor: not-allowed; }
</style>
