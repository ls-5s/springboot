<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { helloApi } from '@/api/test'

const message = ref<string>('')

const fetchHello = async () => {
  try {
    const res = await helloApi()
    message.value = res.data
  } catch (e) {
    message.value = '请求失败，请检查后端是否启动'
  }
}

onMounted(() => {
  fetchHello()
})
</script>

<template>
  <div class="home">
    <h1>Spring Boot + Vue 3</h1>
    <p class="msg">{{ message }}</p>
  </div>
</template>

<style scoped>
.home {
  text-align: center;
  margin-top: 100px;
}
.msg {
  font-size: 24px;
  color: #42b883;
}
</style>
