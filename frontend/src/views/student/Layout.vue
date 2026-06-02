<template>
  <Layout :menu-items="menuItems" default-title="学生端" />
</template>

<script setup>
import { markRaw } from 'vue'
import Layout from '@/Layout/Layout.vue'
import { HomeFilled, Calendar, Collection, User, Lock, Plus } from '@element-plus/icons-vue'

const menuItems = [
  { path: '/student/home', title: '首页', icon: markRaw(HomeFilled) },
  { path: '/student/schedule', title: '个人课表', icon: markRaw(Calendar) },
  { path: '/student/courses', title: '已选课程', icon: markRaw(Collection) },
  { path: '/student/select-course', title: '选课中心', icon: markRaw(Plus) },
  { path: '/student/profile', title: '个人信息', icon: markRaw(User) },
  { path: '/student/password', title: '修改密码', icon: markRaw(Lock) }
]
</script>