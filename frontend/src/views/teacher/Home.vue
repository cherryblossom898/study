<template>
  <div class="home-container">
    <!-- 欢迎横幅 -->
    <el-card class="welcome-card" shadow="hover">
      <div class="welcome-content">
        <div>
          <h2>
            欢迎回来，{{ userStore.userInfo.realName || userStore.userInfo.username }} 老师
            <span class="title-tag" v-if="teacherInfo.title">{{ teacherInfo.title }}</span>
          </h2>
          <p class="date-text">{{ currentDate }}</p>
        </div>
        <el-button type="primary" @click="$router.push('/teacher/courses')">
          查看我的课程
        </el-button>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="stat-card" @click="$router.push('/teacher/courses')">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409EFF"><Collection /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.courseCount }}</div>
              <div class="stat-label">授课门数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67C23A"><UserFilled /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalStudents }}</div>
              <div class="stat-label">学生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#E6A23C"><Clock /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.weekCourseCount }}</div>
              <div class="stat-label">本周课时</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 + 今日课程 -->
    <el-row :gutter="20" class="bottom-row">
      <el-col :xs="24" :md="10">
        <el-card shadow="hover" class="shortcut-card">
          <template #header>
            <span>快捷入口</span>
          </template>
          <div class="shortcut-grid">
            <div class="shortcut-item" @click="$router.push('/teacher/schedule')">
              <el-icon :size="28"><Calendar /></el-icon>
              <span>我的授课</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/teacher/courses')">
              <el-icon :size="28"><Collection /></el-icon>
              <span>我的课程</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/teacher/courses')">
              <el-icon :size="28"><User /></el-icon>
              <span>学生名单</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/teacher/profile')">
              <el-icon :size="28"><Setting /></el-icon>
              <span>个人信息</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="14">
        <el-card shadow="hover" class="today-card">
          <template #header>
            <div class="card-header">
              <span>今日课程（{{ currentWeekday }}）</span>
              <el-button text type="primary" @click="$router.push('/teacher/schedule')">
                查看完整课表
              </el-button>
            </div>
          </template>
          <div v-if="todayCourses.length > 0">
            <div
              v-for="course in todayCourses"
              :key="course.teachingId"
              class="course-item"
            >
              <div class="course-time">{{ course.section }} ({{ course.startTime }}-{{ course.endTime }})</div>
              <div class="course-info">
                <span class="course-name">{{ course.courseName }}</span>
                <span class="course-location">{{ course.classroomName }}</span>
              </div>
              <div class="course-class">{{ course.className }}</div>
            </div>
          </div>
          <el-empty v-else description="今天没有课程，好好休息吧~" :image-size="80" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getTeacherSchedule } from '@/api/schedule'
import { getTeacherProfile } from '@/api/teacher'
import { Collection, UserFilled, Clock, Calendar, User, Setting } from '@element-plus/icons-vue'

const userStore = useUserStore()

// 当前日期
const currentDate = computed(() => {
  const now = new Date()
  return `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日`
})

// 当前星期几（中文）
const currentWeekday = computed(() => {
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return weekdays[new Date().getDay()]
})

// 教师详细信息（用于显示职称）
const teacherInfo = ref({})

// 统计数据
const stats = ref({
  courseCount: 0,
  totalStudents: 0,
  weekCourseCount: 0
})

// 今日课程
const todayCourses = ref([])

// 获取教师详细信息（职称）
async function fetchTeacherProfile() {
  try {
    const data = await getTeacherProfile()
    teacherInfo.value = data
  } catch (error) {
    console.error('获取教师信息失败', error)
  }
}

// 获取课表数据，计算统计信息并筛选今日课程
async function fetchSchedule() {
  try {
    const schedule = await getTeacherSchedule('2024-2025学年第一学期')
    
    // 计算授课门数（去重 teachingId）
    const teachingIds = new Set(schedule.map(item => item.teachingId))
    stats.value.courseCount = teachingIds.size
    
    // 计算学生总数（累加每门课的 selectedCount）
    // 注意：同一门课在不同时间段可能重复出现，需去重
    const courseMap = new Map()
    schedule.forEach(item => {
      if (!courseMap.has(item.teachingId)) {
        courseMap.set(item.teachingId, item.selectedCount || 0)
      }
    })
    stats.value.totalStudents = Array.from(courseMap.values()).reduce((sum, count) => sum + count, 0)
    
    // 本周课时（每个教学班按1次课计算，可根据实际调整）
    stats.value.weekCourseCount = schedule.length

    // 筛选今日课程
    const today = String(new Date().getDay())
    const mappedToday = today === '0' ? '7' : today
    todayCourses.value = schedule.filter(item => item.weekday === mappedToday)
  } catch (error) {
    console.error('获取课表失败', error)
  }
}

onMounted(() => {
  fetchTeacherProfile()
  fetchSchedule()
})
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.welcome-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content h2 {
  margin: 0 0 8px 0;
  font-weight: 500;
}

.title-tag {
  margin-left: 12px;
  font-size: 16px;
  font-weight: normal;
  color: #909399;
}

.date-text {
  color: #909399;
  margin: 0;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-3px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 48px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.shortcut-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.shortcut-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  border-radius: 8px;
  background-color: #f5f7fa;
  cursor: pointer;
  transition: all 0.2s;
}

.shortcut-item:hover {
  background-color: #ecf5ff;
  color: #409EFF;
}

.shortcut-item span {
  margin-top: 8px;
  font-size: 14px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.course-item {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

.course-item:last-child {
  border-bottom: none;
}

.course-time {
  font-size: 13px;
  color: #909399;
  margin-bottom: 6px;
}

.course-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 4px;
}

.course-name {
  font-weight: 500;
  color: #303133;
}

.course-location {
  color: #606266;
  font-size: 13px;
}

.course-class {
  font-size: 13px;
  color: #909399;
}
</style>