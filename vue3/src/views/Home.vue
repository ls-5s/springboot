<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getAdminArticlesApi } from '@/api/article'
import { getCategoriesApi } from '@/api/category'
import { getTagsApi } from '@/api/tag'
import { getPendingCommentsApi } from '@/api/comment'
import type { ArticleItem } from '@/api/article'

const articleCount = ref(0)
const categoryCount = ref(0)
const tagCount = ref(0)
const pendingCount = ref(0)
const recentArticles = ref<ArticleItem[]>([])

onMounted(async () => {
  try {
    const [aRes, cRes, tRes, pRes] = await Promise.all([
      getAdminArticlesApi({ page: 1, size: 1 }),
      getCategoriesApi(),
      getTagsApi(),
      getPendingCommentsApi()
    ])
    articleCount.value = aRes.data.total
    categoryCount.value = cRes.data.length
    tagCount.value = tRes.data.length
    pendingCount.value = pRes.data.length
  } catch { /* ignore */ }

  // 最近5篇文章
  try {
    const res = await getAdminArticlesApi({ page: 1, size: 5 })
    recentArticles.value = res.data.records
  } catch { recentArticles.value = [] }
})
</script>

<template>
  <div class="home">
    <h1 class="welcome">欢迎回来</h1>

    <div class="stats">
      <div class="stat-card"><span class="stat-num">{{ articleCount }}</span><span class="stat-label">文章</span></div>
      <div class="stat-card"><span class="stat-num">{{ categoryCount }}</span><span class="stat-label">分类</span></div>
      <div class="stat-card"><span class="stat-num">{{ tagCount }}</span><span class="stat-label">标签</span></div>
      <div class="stat-card warn"><span class="stat-num">{{ pendingCount }}</span><span class="stat-label">待审评论</span></div>
    </div>

    <div class="card">
      <div class="card-header"><h2>最近文章</h2></div>
      <table v-if="recentArticles.length" class="table">
        <tbody>
          <tr v-for="item in recentArticles" :key="item.id">
            <td class="title">{{ item.title }}</td>
            <td><span :class="['s-tag', { d: item.status===0, p: item.status===1, r: item.status===2 }]">
              {{ {0:'草稿',1:'已发布',2:'私密'}[item.status] }}</span></td>
            <td class="time">{{ item.createTime?.slice(0, 16) }}</td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty">暂无文章</div>
    </div>
  </div>
</template>

<style scoped>
.home { max-width: 960px; margin: 0 auto; }
.welcome { font-size: 24px; font-weight: 700; color: #1a1a1a; margin: 0 0 28px; }

.stats { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 28px; }
.stat-card { background: #fff; border-radius: 12px; padding: 24px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.stat-num { display: block; font-size: 32px; font-weight: 700; color: #667eea; margin-bottom: 4px; }
.stat-card.warn .stat-num { color: #e67e22; }
.stat-label { font-size: 13px; color: #999; }

.card { background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden; }
.card-header { padding: 18px 24px 14px; border-bottom: 1px solid #f0f0f0; }
.card-header h2 { font-size: 16px; font-weight: 600; color: #1a1a1a; margin: 0; }
.table { width: 100%; border-collapse: collapse; }
.table td { padding: 14px 24px; font-size: 14px; color: #333; border-bottom: 1px solid #f5f5f5; }
.table tbody tr:hover { background: #fafbfc; }
.title { font-weight: 500; }
.time { color: #999; font-size: 13px; }
.s-tag { display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 12px; font-weight: 500; }
.s-tag.p { background: #eef7ee; color: #27ae60; }
.s-tag.d { background: #f3f4f6; color: #999; }
.s-tag.r { background: #fef2f2; color: #e74c3c; }
.empty { padding: 48px 24px; text-align: center; color: #ccc; font-size: 14px; }
</style>
