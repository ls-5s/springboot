<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPendingCommentsApi, approveCommentApi, deleteCommentApi } from '@/api/comment'
import type { CommentItem } from '@/api/comment'

const list = ref<CommentItem[]>([])
const statusMap: Record<number, string> = { 0: '待审核', 1: '已通过', 2: '已拒绝' }

const fetchList = async () => {
  try { const res = await getPendingCommentsApi(); list.value = res.data } catch { list.value = [] }
}

const handleApprove = async (item: CommentItem) => {
  try { await approveCommentApi(item.id); fetchList() } catch { /* ignore */ }
}

const handleDelete = async (item: CommentItem) => {
  if (!confirm('确定删除该评论？')) return
  try { await deleteCommentApi(item.id); fetchList() } catch { /* ignore */ }
}

onMounted(fetchList)
</script>

<template>
  <div class="page">
    <div class="page-header"><h1>评论管理</h1></div>
    <div class="card">
      <table v-if="list.length" class="table">
        <thead><tr><th>评论人</th><th>内容</th><th>状态</th><th>时间</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.userName }}</td>
            <td class="content">{{ item.content }}</td>
            <td><span :class="['s-tag', { pending: item.status === 0, ok: item.status === 1 }]">{{ statusMap[item.status] || '未知' }}</span></td>
            <td class="time">{{ item.createTime?.slice(0, 10) }}</td>
            <td class="actions">
              <button v-if="item.status === 0" class="act ok" @click="handleApprove(item)">通过</button>
              <button class="act del" @click="handleDelete(item)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty">暂无待审核评论</div>
    </div>
  </div>
</template>

<style scoped>
.page { max-width: 1000px; margin: 0 auto; }
.page-header { margin-bottom: 20px; }
.page-header h1 { font-size: 22px; font-weight: 700; color: #1a1a1a; margin: 0; }
.card { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; }
.table { width: 100%; border-collapse: collapse; }
.table th { text-align: left; padding: 14px 20px; font-size: 13px; color: #999; background: #fafbfc; border-bottom: 1px solid #eee; }
.table td { padding: 14px 20px; font-size: 14px; color: #333; border-bottom: 1px solid #f5f5f5; }
.table tbody tr:hover { background: #fafbfc; }
.content { max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.time { color: #999; font-size: 13px; }
.s-tag { padding: 2px 10px; border-radius: 10px; font-size: 12px; }
.s-tag.pending { background: #fef3c7; color: #d97706; }
.s-tag.ok { background: #eef7ee; color: #27ae60; }
.actions { white-space: nowrap; }
.act { padding: 4px 14px; border-radius: 5px; font-size: 13px; cursor: pointer; border: 1px solid #e5e7eb; background: #fff; margin-right: 8px; }
.act.ok { color: #27ae60; border-color: #27ae60; } .act.ok:hover { background: #27ae60; color: #fff; }
.act.del { color: #dc2626; border-color: #dc2626; } .act.del:hover { background: #dc2626; color: #fff; }
.empty { padding: 64px 24px; text-align: center; color: #ccc; font-size: 14px; }
</style>
