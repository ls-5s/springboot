<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getAdminArticlesApi, createArticleApi, updateArticleApi, deleteArticleApi } from '@/api/article'
import type { ArticleItem, ArticleDTO } from '@/api/article'
import { getCategoriesApi } from '@/api/category'
import type { CategoryItem } from '@/api/category'
import { getTagsApi } from '@/api/tag'
import type { TagItem } from '@/api/tag'
import { uploadApi } from '@/api/upload'

// 分类和标签数据
const categories = ref<CategoryItem[]>([])
const tags = ref<TagItem[]>([])

// 搜索 + 分页 + 状态筛选
const keyword = ref<string>('')
const filterStatus = ref<number | undefined>(undefined)
const articles = ref<ArticleItem[]>([])
const total = ref<number>(0)
const page = ref<number>(1)
const pageSize = ref<number>(10)
const totalPages = ref<number>(0)

// 弹窗
const showModal = ref<boolean>(false)
const editingId = ref<number | null>(null)
const saving = ref<boolean>(false)
const errorMsg = ref<string>('')

const form = reactive<ArticleDTO>({
  title: '',
  summary: '',
  content: '',
  cover: '',
  categoryId: undefined,
  tagIds: [],
  status: 1,
  isTop: 0
})

const selectedTagIds = ref<number[]>([])

const toggleTag = (id: number) => {
  const idx = selectedTagIds.value.indexOf(id)
  if (idx > -1) selectedTagIds.value.splice(idx, 1)
  else selectedTagIds.value.push(id)
}

const handleUpload = async (e: Event) => {
  const input = e.target as HTMLInputElement
  if (!input.files?.length) return
  try {
    const res = await uploadApi(input.files[0])
    form.cover = res.data.url
  } catch (e: any) {
    errorMsg.value = e.message || '上传失败'
  }
}

const statusMap: Record<number, string> = { 0: '草稿', 1: '已发布', 2: '私密' }

const fetchList = async () => {
  try {
    const res = await getAdminArticlesApi({
      page: page.value,
      size: pageSize.value,
      keyword: keyword.value || undefined,
      status: filterStatus.value
    })
    articles.value = res.data.records
    total.value = res.data.total
    totalPages.value = res.data.pages
  } catch {
    articles.value = []
    total.value = 0
    totalPages.value = 0
  }
}

// 搜索：重置到第一页
const handleSearch = () => {
  page.value = 1
  fetchList()
}

// 翻页
const goPage = (p: number) => {
  if (p < 1 || p > totalPages.value) return
  page.value = p
  fetchList()
}

const handleCreate = () => {
  editingId.value = null
  form.title = ''
  form.summary = ''
  form.content = ''
  form.cover = ''
  form.categoryId = undefined
  form.status = 1
  form.isTop = 0
  selectedTagIds.value = []
  errorMsg.value = ''
  showModal.value = true
}

const handleEdit = (item: ArticleItem) => {
  editingId.value = item.id
  form.title = item.title
  form.summary = item.summary || ''
  form.content = item.content || ''
  form.cover = item.cover || ''
  form.categoryId = item.categoryId
  form.status = item.status
  form.isTop = item.isTop
  selectedTagIds.value = []
  errorMsg.value = ''
  showModal.value = true
}

const handleSave = async () => {
  if (!form.title.trim()) {
    errorMsg.value = '标题不能为空'
    return
  }
  saving.value = true
  errorMsg.value = ''
  try {
    const payload = { ...form, tagIds: selectedTagIds.value.length > 0 ? selectedTagIds.value : undefined }
    if (editingId.value) {
      await updateArticleApi(editingId.value, payload)
    } else {
      await createArticleApi(payload)
    }
    showModal.value = false
    fetchList()
  } catch (e: any) {
    errorMsg.value = e.message || '保存失败'
  } finally {
    saving.value = false
  }
}

const handleDelete = async (item: ArticleItem) => {
  if (!confirm(`确定删除「${item.title}」？`)) return
  try {
    await deleteArticleApi(item.id)
    fetchList()
  } catch (e: any) {
    alert(e.message || '删除失败')
  }
}

onMounted(async () => {
  fetchList()
  try { const c = await getCategoriesApi(); categories.value = c.data } catch { /* ignore */ }
  try { const t = await getTagsApi(); tags.value = t.data } catch { /* ignore */ }
})
</script>

<template>
  <div class="articles">
    <div class="page-header">
      <h1>文章管理</h1>
      <button class="create-btn" @click="handleCreate">+ 新建文章</button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <input
        v-model="keyword"
        placeholder="搜索标题..."
        @keyup.enter="handleSearch"
      />
      <select v-model="filterStatus" @change="handleSearch" class="status-select">
        <option :value="undefined">全部状态</option>
        <option :value="1">已发布</option>
        <option :value="0">草稿</option>
        <option :value="2">私密</option>
      </select>
      <button class="search-btn" @click="handleSearch">搜索</button>
    </div>

    <!-- 列表 -->
    <div class="card">
      <table v-if="articles.length > 0" class="table">
        <thead>
          <tr>
            <th style="width:40%">标题</th>
            <th>状态</th>
            <th>浏览量</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in articles" :key="item.id">
            <td class="title-cell">
              <span class="top-tag" v-if="item.isTop">置顶</span>
              {{ item.title }}
            </td>
            <td>
              <span :class="['status-tag', { draft: item.status === 0, pub: item.status === 1, priv: item.status === 2 }]">
                {{ statusMap[item.status] || '未知' }}
              </span>
            </td>
            <td class="num">{{ item.viewCount }}</td>
            <td class="time">{{ item.createTime?.slice(0, 10) }}</td>
            <td class="actions">
              <button class="act edit" @click="handleEdit(item)">编辑</button>
              <button class="act del" @click="handleDelete(item)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty">暂无文章</div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="pager">
        <button :disabled="page <= 1" @click="goPage(page - 1)">上一页</button>
        <span v-for="p in totalPages" :key="p"
              :class="['page-num', { cur: p === page }]"
              @click="goPage(p)">{{ p }}</span>
        <button :disabled="page >= totalPages" @click="goPage(page + 1)">下一页</button>
        <span class="total-info">共 {{ total }} 篇</span>
      </div>
    </div>

    <!-- 新建/编辑弹窗 -->
    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal">
          <div class="modal-header">
            <h2>{{ editingId ? '编辑文章' : '新建文章' }}</h2>
            <button class="close-btn" @click="showModal = false">&times;</button>
          </div>

          <div class="modal-body">
            <Transition name="fade">
              <p v-if="errorMsg" class="err">{{ errorMsg }}</p>
            </Transition>

            <div class="field">
              <label>标题 <span class="req">*</span></label>
              <input v-model="form.title" placeholder="请输入标题" maxlength="100" />
            </div>
            <div class="field">
              <label>摘要</label>
              <textarea v-model="form.summary" placeholder="请输入摘要" rows="3" maxlength="500" />
            </div>
            <div class="field">
              <label>正文 (Markdown)</label>
              <textarea v-model="form.content" placeholder="请输入正文" rows="8" />
            </div>
            <div class="field">
              <label>封面图</label>
              <div class="cover-row">
                <input v-model="form.cover" placeholder="输入 URL 或上传" />
                <label class="upload-btn">
                  上传
                  <input type="file" accept="image/*" hidden @change="handleUpload" />
                </label>
              </div>
            </div>
            <div class="field">
              <label>分类</label>
              <select v-model="form.categoryId">
                <option :value="undefined">请选择分类</option>
                <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
              </select>
            </div>
            <div class="field" v-if="tags.length">
              <label>标签</label>
              <div class="tag-list">
                <span v-for="t in tags" :key="t.id"
                      :class="['tag-chip', { on: selectedTagIds.includes(t.id) }]"
                      @click="toggleTag(t.id)">{{ t.name }}</span>
              </div>
            </div>
            <div class="inline">
              <div class="field flex-1">
                <label>状态</label>
                <select v-model="form.status">
                  <option :value="0">草稿</option>
                  <option :value="1">已发布</option>
                  <option :value="2">私密</option>
                </select>
              </div>
              <div class="field flex-1">
                <label>置顶</label>
                <select v-model="form.isTop">
                  <option :value="0">否</option>
                  <option :value="1">是</option>
                </select>
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button class="cancel-btn" @click="showModal = false">取消</button>
            <button class="save-btn" :disabled="saving" @click="handleSave">
              {{ saving ? '保存中...' : '保存' }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.articles { max-width: 1100px; margin: 0 auto; }

.page-header {
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;
}
.page-header h1 { font-size: 22px; font-weight: 700; color: #1a1a1a; margin: 0; }
.create-btn {
  padding: 10px 22px; border: none; border-radius: 8px;
  background: linear-gradient(135deg, #667eea, #764ba2); color: #fff;
  font-size: 14px; font-weight: 500; cursor: pointer; transition: opacity 0.2s;
}
.create-btn:hover { opacity: 0.9; }

/* 搜索栏 */
.search-bar {
  display: flex; gap: 10px; margin-bottom: 16px;
}
.search-bar input {
  flex: 1; max-width: 320px;
  padding: 9px 14px; border: 1.5px solid #e8e8e8; border-radius: 8px;
  font-size: 14px; outline: none; background: #fff; transition: border-color 0.2s;
}
.search-bar input:focus { border-color: #667eea; }
.status-select {
  padding: 9px 12px; border: 1.5px solid #e8e8e8; border-radius: 8px;
  font-size: 14px; outline: none; background: #fff; color: #666; cursor: pointer;
}
.status-select:focus { border-color: #667eea; }
.search-btn {
  padding: 9px 20px; border: none; border-radius: 8px;
  background: #667eea; color: #fff; font-size: 14px; cursor: pointer; transition: opacity 0.2s;
}
.search-btn:hover { opacity: 0.85; }

/* 列表 */
.card {
  background: #fff; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.04); overflow: hidden;
}
.table { width: 100%; border-collapse: collapse; }
.table th {
  text-align: left; padding: 14px 20px; font-size: 13px; font-weight: 500;
  color: #999; background: #fafbfc; border-bottom: 1px solid #eee;
}
.table td {
  padding: 14px 20px; font-size: 14px; color: #333; border-bottom: 1px solid #f5f5f5;
}
.table tbody tr:hover { background: #fafbfc; }
.title-cell { font-weight: 500; }
.top-tag {
  display: inline-block; padding: 1px 6px; border-radius: 3px;
  background: #fef3c7; color: #d97706; font-size: 11px; font-weight: 500;
  margin-right: 6px; vertical-align: 2px;
}
.num, .time { color: #999; font-size: 13px; }
.status-tag {
  display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 12px; font-weight: 500;
}
.status-tag.draft { background: #f3f4f6; color: #999; }
.status-tag.pub   { background: #eef7ee; color: #27ae60; }
.status-tag.priv  { background: #fef2f2; color: #e74c3c; }

.actions { white-space: nowrap; }
.act {
  padding: 4px 14px; border-radius: 5px; font-size: 13px; cursor: pointer;
  border: 1px solid #e5e7eb; background: #fff; transition: all 0.2s; margin-right: 8px;
}
.act.edit { color: #667eea; border-color: #667eea; }
.act.edit:hover { background: #667eea; color: #fff; }
.act.del  { color: #dc2626; border-color: #dc2626; }
.act.del:hover  { background: #dc2626; color: #fff; }

.empty { padding: 64px 24px; text-align: center; color: #ccc; font-size: 14px; }

/* 分页 */
.pager {
  display: flex; justify-content: center; align-items: center; gap: 6px;
  padding: 18px 20px; border-top: 1px solid #f0f0f0;
}
.pager button {
  padding: 6px 14px; border: 1px solid #e5e7eb; border-radius: 6px;
  background: #fff; color: #666; font-size: 13px; cursor: pointer; transition: all 0.2s;
}
.pager button:hover:not(:disabled) { border-color: #667eea; color: #667eea; }
.pager button:disabled { opacity: 0.4; cursor: not-allowed; }
.page-num {
  width: 32px; height: 32px; display: flex; align-items: center; justify-content: center;
  border-radius: 6px; font-size: 13px; color: #666; cursor: pointer; transition: all 0.2s;
}
.page-num:hover { background: #f0f0ff; color: #667eea; }
.page-num.cur { background: #667eea; color: #fff; font-weight: 500; }
.total-info { margin-left: 12px; font-size: 13px; color: #999; }

/* 弹窗 */
.modal-overlay {
  position: fixed; inset: 0; background: rgba(0,0,0,0.35);
  display: flex; justify-content: center; align-items: flex-start; padding-top: 60px; z-index: 1000;
}
.modal {
  background: #fff; border-radius: 16px; width: 680px;
  max-height: calc(100vh - 120px); overflow-y: auto;
  box-shadow: 0 20px 60px rgba(0,0,0,0.15);
}
.modal-header {
  display: flex; justify-content: space-between; align-items: center; padding: 22px 28px 16px;
}
.modal-header h2 { font-size: 18px; font-weight: 600; color: #1a1a1a; margin: 0; }
.close-btn {
  border: none; background: transparent; font-size: 24px; color: #999; cursor: pointer;
}
.close-btn:hover { color: #333; }

.modal-body { padding: 0 28px 20px; }
.modal-footer {
  display: flex; justify-content: flex-end; gap: 12px;
  padding: 16px 28px 22px; border-top: 1px solid #f0f0f0;
}

.err {
  font-size: 13px; color: #e74c3c; background: #fef2f2;
  padding: 8px 12px; border-radius: 8px; margin: 0 0 16px;
}

.field { margin-bottom: 16px; }
.field label { display: block; font-size: 13px; font-weight: 500; color: #666; margin-bottom: 6px; }
.req { color: #e74c3c; }
.field input, .field textarea, .field select {
  width: 100%; padding: 10px 14px; border: 1.5px solid #e8e8e8; border-radius: 8px;
  font-size: 14px; color: #333; outline: none; background: #fafafa;
  transition: border-color 0.2s; box-sizing: border-box; font-family: inherit;
}
.field input:focus, .field textarea:focus, .field select:focus {
  border-color: #667eea; background: #fff;
}
.field textarea { resize: vertical; }

.cover-row { display: flex; gap: 8px; }
.cover-row input { flex: 1; }
.upload-btn {
  display: inline-flex; align-items: center;
  padding: 10px 16px; border: 1px dashed #c0c0c0; border-radius: 8px;
  font-size: 13px; color: #999; cursor: pointer; transition: all 0.2s; white-space: nowrap;
}
.upload-btn:hover { border-color: #667eea; color: #667eea; }

.tag-list { display: flex; flex-wrap: wrap; gap: 8px; }
.tag-chip {
  display: inline-block; padding: 4px 14px; border-radius: 14px;
  font-size: 13px; cursor: pointer; transition: all 0.2s;
  border: 1.5px solid #e5e7eb; color: #666; background: #fff;
}
.tag-chip:hover { border-color: #667eea; color: #667eea; }
.tag-chip.on { background: #667eea; border-color: #667eea; color: #fff; }

.inline { display: flex; gap: 16px; }
.flex-1 { flex: 1; }

.cancel-btn, .save-btn {
  padding: 10px 28px; border-radius: 8px; font-size: 14px; cursor: pointer; transition: all 0.2s;
}
.cancel-btn { border: 1px solid #e5e7eb; background: #fff; color: #666; }
.cancel-btn:hover { background: #f3f4f6; }
.save-btn {
  border: none; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; font-weight: 500;
}
.save-btn:hover { opacity: 0.9; }
.save-btn:disabled { opacity: 0.5; cursor: not-allowed; }

.fade-enter-active, .fade-leave-active { transition: opacity 0.2s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
