<template>
  <Layout :menu-items="menuItems" default-title="教师端" />
</template>

<script setup>
import { markRaw } from 'vue'
import Layout from '@/Layout/Layout.vue'
import { HomeFilled, Calendar, Collection, User, Lock } from '@element-plus/icons-vue'

const menuItems = [
  { path: '/teacher/home', title: '首页', icon: markRaw(HomeFilled) },
  { path: '/teacher/schedule', title: '我的授课', icon: markRaw(Calendar) },
  { path: '/teacher/courses', title: '我的课程', icon: markRaw(Collection) },
  { path: '/teacher/profile', title: '个人信息', icon: markRaw(User) },
  { path: '/teacher/password', title: '修改密码', icon: markRaw(Lock) }
]
</script>