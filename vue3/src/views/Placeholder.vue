<script setup lang="ts">
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()
const pageName = computed(() => {
  const map: Record<string, string> = {
    Articles: '文章管理',
    Categories: '分类管理',
    Tags: '标签管理',
    Comments: '评论管理',
    Links: '友链管理',
    Settings: '系统配置',
  }
  return map[route.name as string] || route.name
})
</script>

<template>
  <div class="placeholder">
    <div class="card">
      <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round">
        <rect x="3" y="3" width="18" height="18" rx="2" ry="2"/>
        <line x1="12" y1="8" x2="12" y2="16"/>
        <line x1="8" y1="12" x2="16" y2="12"/>
      </svg>
      <h2>{{ pageName }}</h2>
      <p>该模块正在开发中，敬请期待...</p>
    </div>
  </div>
</template>

<style scoped>
.placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}
.card {
  text-align: center;
  color: #999;
}
.icon {
  width: 64px;
  height: 64px;
  color: #ddd;
  margin-bottom: 16px;
}
.card h2 {
  font-size: 20px;
  color: #666;
  margin: 0 0 8px;
  font-weight: 500;
}
.card p {
  font-size: 14px;
  margin: 0;
}
</style>
