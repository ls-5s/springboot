<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { helloApi } from '@/api/test'

const message = ref<string>('')

onMounted(async () => {
  try {
    const res = await helloApi()
    message.value = res.data
  } catch {
    message.value = '请求失败，请检查后端是否启动'
  }
})
</script>

<template>
  <div class="home">
    <div class="welcome">
      <h1>欢迎回来</h1>
      <p class="desc">{{ message }}</p>
    </div>

    <!-- 数据概览 -->
    <div class="stats">
      <div class="stat-card">
        <span class="stat-num">0</span>
        <span class="stat-label">文章</span>
      </div>
      <div class="stat-card">
        <span class="stat-num">0</span>
        <span class="stat-label">分类</span>
      </div>
      <div class="stat-card">
        <span class="stat-num">0</span>
        <span class="stat-label">标签</span>
      </div>
      <div class="stat-card">
        <span class="stat-num">0</span>
        <span class="stat-label">评论</span>
      </div>
    </div>

    <!-- 最近文章 -->
    <div class="card">
      <div class="card-header">
        <h2>最近文章</h2>
        <span class="hint">接口开发中...</span>
      </div>
      <div class="empty">暂无文章</div>
    </div>
  </div>
</template>

<style scoped>
.home {
  max-width: 960px;
  margin: 0 auto;
}

.welcome {
  margin-bottom: 28px;
}
.welcome h1 {
  font-size: 24px;
  font-weight: 700;
  color: #1a1a1a;
  margin: 0 0 6px;
}
.desc {
  font-size: 14px;
  color: #999;
  margin: 0;
}

/* 数据概览 */
.stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 28px;
}
.stat-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.stat-num {
  display: block;
  font-size: 32px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 4px;
}
.stat-label {
  font-size: 13px;
  color: #999;
}

/* 文章卡片 */
.card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  overflow: hidden;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 24px 14px;
  border-bottom: 1px solid #f0f0f0;
}
.card-header h2 {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}
.hint {
  font-size: 12px;
  color: #ccc;
}
.empty {
  padding: 48px 24px;
  text-align: center;
  color: #ccc;
  font-size: 14px;
}
</style>
