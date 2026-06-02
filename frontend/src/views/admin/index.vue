<template>
  <div>
    <h1>管理员端首页</h1>
    <p>当前登录用户：{{ userInfo?.realName || userInfo?.username }}</p>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>