<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="aside">
      <!-- Logo 区域 -->
      <div class="logo">
        <img src="@/assets/logo.png" alt="Logo" class="logo-img" />
        <span v-if="!isCollapse" class="logo-text">排课系统</span>
        <span v-else class="logo-text-collapsed">排</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <!-- 遍历传入的菜单项 -->
        <template v-for="item in menuItems" :key="item.path">
          <!-- 有子菜单 -->
          <el-sub-menu v-if="item.children" :index="item.path">
            <template #title>
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.title }}</span>
            </template>
            <el-menu-item v-for="child in item.children" :key="child.path" :index="child.path">
              <el-icon><component :is="child.icon" /></el-icon>
              <span>{{ child.title }}</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 无子菜单 -->
          <el-menu-item v-else :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.title }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>

    <!-- 右侧主体 -->
    <el-container>
      <!-- 顶部栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <span class="page-title">{{ pageTitle }}</span>
        </div>

        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <!-- 头像：有上传则显示上传图，否则显示默认图片 -->
              <img
                v-if="userStore.avatarUrl"
                :src="getFullAvatarUrl(userStore.avatarUrl)"
                class="user-avatar"
                alt="头像"
              />
              <img
                v-else
                src="@/assets/default-avatar.png"
                class="user-avatar"
                alt="默认头像"
              />
              <span>{{ userStore.userInfo.realName || userStore.userInfo.username }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="uploadAvatar">上传头像</el-dropdown-item>
                <el-dropdown-item
                  command="password"
                  v-if="userStore.userInfo.role !== 'admin'"
                >
                  修改密码
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { Fold, Expand, ArrowDown } from '@element-plus/icons-vue'

// 接收父组件传入的菜单配置
const props = defineProps({
  menuItems: {
    type: Array,
    required: true
  },
  defaultTitle: {
    type: String,
    default: '首页'
  }
})

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 侧边栏折叠状态
const isCollapse = ref(false)

// 当前激活的菜单项
const activeMenu = computed(() => route.path)

// 当前页面标题（可根据路由 meta 设置）
const pageTitle = computed(() => route.meta.title || props.defaultTitle)

// 拼接完整头像 URL（后端返回相对路径 /uploads/avatars/xxx.png）
function getFullAvatarUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  const filename = url.substring(url.lastIndexOf('/') + 1)
  return `http://localhost:8080/api/upload/avatars/${filename}`
}

// 下拉菜单命令处理
function handleCommand(command) {
  if (command === 'logout') {
    userStore.logout()
    ElMessage.success('已退出登录')
  } else if (command === 'profile') {
    if (userStore.isStudent()) {
      router.push('/student/profile')
    } else if (userStore.isTeacher()) {
      router.push('/teacher/profile')
    } else if (userStore.isAdmin()) {
      router.push('/admin/profile')
    }
  } else if (command === 'uploadAvatar') {
    // 跳转到个人信息页（上传功能已集成在其中）
    if (userStore.isStudent()) {
      router.push('/student/profile')
    } else if (userStore.isTeacher()) {
      router.push('/teacher/profile')
    } else if (userStore.isAdmin()) {
      ElMessage.info('管理员头像功能暂未开放')
    }
  } else if (command === 'password') {
    if (userStore.isStudent()) {
      router.push('/student/password')
    } else if (userStore.isTeacher()) {
      router.push('/teacher/password')
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #304156;
  transition: width 0.3s;
}

/* Logo 区域样式 */
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #263445;
}

.logo-img {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.logo-text {
  white-space: nowrap;
}

.logo-text-collapsed {
  font-size: 20px;
}

.el-menu {
  border-right: none;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  margin-right: 15px;
}

.page-title {
  font-size: 16px;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #606266;
}

/* 用户头像圆形样式（与 logo 统一） */
.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(0, 0, 0, 0.1);
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>