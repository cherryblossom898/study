<template>
  <div>
    <h1>学生端首页</h1>
    <p>当前登录用户：{{ userStore.userInfo.realName || userStore.userInfo.username }}</p>
    <el-button @click="userStore.logout">退出登录</el-button>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>