<template>
  <div class="login-container">
    <!-- 登录卡片 -->
    <el-card class="login-card">
      <h2 class="title">排课系统登录</h2>

      <!-- 登录表单 -->
      <el-form :model="form" label-width="60px" @submit.prevent="handleLogin">
        <!-- 用户名 -->
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入学号/工号" clearable />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>

        <!-- 角色选择 -->
        <el-form-item label="身份">
          <el-radio-group v-model="form.role">
            <el-radio value="student">学生</el-radio>
            <el-radio value="teacher">教师</el-radio>
            <el-radio value="admin">管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 公共查询入口 -->
      <div class="public-link">
        <el-divider>或</el-divider>
        <el-button type="success" plain style="width: 100%" @click="goToPublic">
          无需登录，浏览公开课程与教师
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const form = reactive({
  username: '',
  password: '',
  role: 'student'
})

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    const result = await login(form)
    userStore.setLoginData(result)
    ElMessage.success('登录成功')

    if (result.role === 'admin') {
      router.push('/admin')
    } else if (result.role === 'student') {
      router.push('/student')
    } else {
      router.push('/teacher')
    }
  } catch (error) {
    console.log('登录失败', error)
  } finally {
    loading.value = false
  }
}

// 跳转到公共查询页面
function goToPublic() {
  router.push('/public')
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 20px;
  border-radius: 8px;
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.public-link {
  margin-top: 20px;
  text-align: center;
}
</style>