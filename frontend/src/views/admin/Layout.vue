<template>
  <Layout :menu-items="menuItems" default-title="管理员端" />
</template>

<script setup>
import { markRaw } from 'vue'
import Layout from '@/Layout/Layout.vue'
import { HomeFilled, User, School, OfficeBuilding, Calendar, Setting } from '@element-plus/icons-vue'

const menuItems = [
  { path: '/admin/home', title: '首页', icon: markRaw(HomeFilled) },
  { path: '/admin/student', title: '学生管理', icon: markRaw(User) },
  { path: '/admin/teacher', title: '教师管理', icon: markRaw(User) },
  { path: '/admin/course', title: '课程管理', icon: markRaw(School) },
  { path: '/admin/classroom', title: '教室管理', icon: markRaw(OfficeBuilding) },
  { path: '/admin/teaching', title: '排课管理', icon: markRaw(Calendar) },
]
</script>