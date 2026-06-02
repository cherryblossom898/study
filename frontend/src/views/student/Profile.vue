<template>
  <div class="profile-container">
    <el-card class="profile-card" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>

      <!-- 头像区域 -->
      <div class="avatar-section">
        <el-upload
          class="avatar-uploader"
          action="http://localhost:8080/api/upload/avatar"
          :headers="{ Authorization: userStore.token }"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
        >
          <img
            v-if="student.avatarUrl"
            :src="getFullAvatarUrl(student.avatarUrl)"
            class="avatar-img"
            alt="头像"
          />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <p class="upload-tip">点击更换头像</p>
      </div>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="学号">{{ student.studentId }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ student.studentName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ student.gender }}</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ formatDate(student.birthDate) }}</el-descriptions-item>
        <el-descriptions-item label="入学年份">{{ student.enrollmentYear }}</el-descriptions-item>
        <el-descriptions-item label="年级">{{ student.grade }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ student.major }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ student.clazz }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ student.studentPhone || '未填写' }}</el-descriptions-item>
        <el-descriptions-item label="所属院系">{{ getDepartmentName(student.departmentId) }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getStudentProfile } from '@/api/student'
import { getAllDepartments } from '@/api/department'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const student = ref({})
const departmentList = ref([])

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 根据院系ID获取院系名称
function getDepartmentName(deptId) {
  if (!deptId) return ''
  const dept = departmentList.value.find(d => d.departmentId === deptId)
  return dept ? dept.departmentName : deptId
}

// 拼接完整头像URL
function getFullAvatarUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  // 将 /uploads/avatars/xxx.png 转换为 /api/upload/avatars/xxx.png
  return 'http://localhost:8080/api/upload/avatars/' + url.substring(url.lastIndexOf('/') + 1)
}

// 头像上传成功回调
function handleAvatarSuccess(response) {
  const avatarUrl = response.avatarUrl
  console.log('上传返回的 avatarUrl:', avatarUrl)
  console.log('更新前 store.avatarUrl:', userStore.avatarUrl)
  
  student.value.avatarUrl = avatarUrl
  userStore.updateAvatar(avatarUrl)
  
  console.log('更新后 store.avatarUrl:', userStore.avatarUrl)
  ElMessage.success('头像上传成功')
}

// 上传前校验
function beforeAvatarUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB！')
    return false
  }
  return true
}

// 获取学生信息
async function fetchProfile() {
  loading.value = true
  try {
    const data = await getStudentProfile()
    student.value = data
  } catch (error) {
    console.error('获取个人信息失败', error)
    ElMessage.error('获取个人信息失败')
  } finally {
    loading.value = false
  }
}

// 获取院系列表
async function fetchDepartments() {
  try {
    const data = await getAllDepartments()
    departmentList.value = data
  } catch (error) {
    console.error('获取院系列表失败', error)
  }
}

onMounted(() => {
  fetchDepartments()
  fetchProfile()
})
</script>

<style scoped>
.profile-container {
  padding: 20px;
}
.profile-card {
  max-width: 900px;
  margin: 0 auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 24px;
}
.avatar-uploader {
  cursor: pointer;
}
.avatar-img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #e4e7ed;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px dashed #d9d9d9;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-uploader-icon:hover {
  border-color: #409eff;
}
.upload-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}
</style>