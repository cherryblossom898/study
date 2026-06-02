<template>
  <div class="home-container">
    <!-- 欢迎横幅 -->
    <el-card class="welcome-card" shadow="hover">
      <div class="welcome-content">
        <div>
          <h2>欢迎回来，管理员 👋</h2>
          <p class="date-text">{{ currentDate }}</p>
        </div>
      </div>
    </el-card>

    <!-- 统计卡片：第一行 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card" @click="$router.push('/admin/student')">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#409EFF"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.studentCount }}</div>
              <div class="stat-label">学生总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card" @click="$router.push('/admin/teacher')">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#67C23A"><UserFilled /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.teacherCount }}</div>
              <div class="stat-label">教师总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card" @click="$router.push('/admin/course')">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#E6A23C"><School /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.courseCount }}</div>
              <div class="stat-label">课程总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card" @click="$router.push('/admin/classroom')">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#F56C6C"><OfficeBuilding /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.classroomCount }}</div>
              <div class="stat-label">教室总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card" @click="$router.push('/admin/teaching')">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#909399"><Calendar /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.teachingCount }}</div>
              <div class="stat-label">排课总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="8" :md="4">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon class="stat-icon" color="#00D4AA"><Collection /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.enrollmentCount }}</div>
              <div class="stat-label">选课人次</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 + 热门课程 -->
    <el-row :gutter="20" class="bottom-row">
      <el-col :xs="24" :md="10">
        <el-card shadow="hover" class="shortcut-card">
          <template #header>
            <span>快捷入口</span>
          </template>
          <div class="shortcut-grid">
            <div class="shortcut-item" @click="$router.push('/admin/student')">
              <el-icon :size="28"><User /></el-icon>
              <span>学生管理</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/admin/teacher')">
              <el-icon :size="28"><UserFilled /></el-icon>
              <span>教师管理</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/admin/course')">
              <el-icon :size="28"><School /></el-icon>
              <span>课程管理</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/admin/classroom')">
              <el-icon :size="28"><OfficeBuilding /></el-icon>
              <span>教室管理</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/admin/teaching')">
              <el-icon :size="28"><Calendar /></el-icon>
              <span>排课管理</span>
            </div>
            <div class="shortcut-item" @click="$router.push('/admin/profile')">
              <el-icon :size="28"><Setting /></el-icon>
              <span>系统设置</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="14">
        <el-card shadow="hover" class="dynamic-card">
          <template #header>
            <span>热门课程（选课人数 Top 5）</span>
          </template>
          <el-table :data="popularCourses" size="small" v-loading="loadingPopular">
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="teacherName" label="" width="100" />
            <el-table-column prop="currentStudents" label="已选/容量" width="100">
              <template #default="{ row }">
                {{ row.currentStudents }}/{{ row.maxStudents }}
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-if="!loadingPopular && popularCourses.length === 0" description="暂无数据" :image-size="60" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新排课记录 -->
    <el-row :gutter="20" class="recent-row">
      <el-col :span="24">
        <el-card shadow="hover" class="recent-card">
          <template #header>
            <div class="card-header">
              <span>最新排课记录</span>
              <el-button text type="primary" @click="$router.push('/admin/teaching')">
                查看全部
              </el-button>
            </div>
          </template>
          <el-table :data="recentTeachings" size="small" v-loading="loadingRecent">
            <el-table-column prop="teachingId" label="教学班ID" width="100" />
            <el-table-column label="课程" min-width="120">
              <template #default="{ row }">
                {{ getCourseName(row.courseId) }}
              </template>
            </el-table-column>
            <el-table-column label="教师" width="100">
              <template #default="{ row }">
                {{ getTeacherName(row.teacherId) }}
              </template>
            </el-table-column>
            <el-table-column label="教室" width="120">
              <template #default="{ row }">
                {{ getRoomName(row.roomId) }}
              </template>
            </el-table-column>
            <el-table-column prop="semester" label="学期" width="160" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { countStudents } from '@/api/student'
import { countTeachers } from '@/api/teacher'
import { countCourses, getPopularCourses } from '@/api/Course'
import { countClassrooms } from '@/api/classroom'
import { countTeachings, getRecentTeachings, getAllCourses, getAllTeachers } from '@/api/teaching'
import { getAllClassrooms } from '@/api/classroom'
import { countEnrollments } from '@/api/enrollment'
import { User, UserFilled, School, OfficeBuilding, Calendar, Collection, Setting } from '@element-plus/icons-vue'

// 当前日期
const currentDate = computed(() => {
  const now = new Date()
  return `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日`
})

// 统计数据
const stats = reactive({
  studentCount: 0,
  teacherCount: 0,
  courseCount: 0,
  classroomCount: 0,
  teachingCount: 0,
  enrollmentCount: 0
})

// 热门课程
const popularCourses = ref([])
const loadingPopular = ref(false)

// 最新排课
const recentTeachings = ref([])
const loadingRecent = ref(false)

// 下拉数据（用于ID转名称）
const courseList = ref([])
const teacherList = ref([])
const classroomList = ref([])

// 转换函数
function getCourseName(courseId) {
  const course = courseList.value.find(c => c.id === courseId)
  return course ? course.courseName : courseId
}
function getTeacherName(teacherId) {
  const teacher = teacherList.value.find(t => t.teacherId === teacherId)
  return teacher ? teacher.teacherName : teacherId
}
function getRoomName(roomId) {
  const room = classroomList.value.find(r => r.roomId === roomId)
  return room ? room.roomName : roomId
}

// 获取所有统计数量
async function fetchStats() {
  try {
    const [
      studentRes,
      teacherRes,
      courseRes,
      classroomRes,
      teachingRes,
      enrollmentRes
    ] = await Promise.all([
      countStudents(),
      countTeachers(),
      countCourses(),
      countClassrooms(),
      countTeachings(),
      countEnrollments()
    ])
    stats.studentCount = studentRes
    stats.teacherCount = teacherRes
    stats.courseCount = courseRes
    stats.classroomCount = classroomRes
    stats.teachingCount = teachingRes
    stats.enrollmentCount = enrollmentRes
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

// 获取热门课程
async function fetchPopularCourses() {
  loadingPopular.value = true
  try {
    popularCourses.value = await getPopularCourses()
  } finally {
    loadingPopular.value = false
  }
}

// 获取最新排课及下拉数据
async function fetchRecentTeachings() {
  loadingRecent.value = true
  try {
    const [teachings, courses, teachers, classrooms] = await Promise.all([
      getRecentTeachings(),
      getAllCourses(),
      getAllTeachers(),
      getAllClassrooms()
    ])
    recentTeachings.value = teachings
    courseList.value = courses
    teacherList.value = teachers
    classroomList.value = classrooms
  } finally {
    loadingRecent.value = false
  }
}

onMounted(() => {
  fetchStats()
  fetchPopularCourses()
  fetchRecentTeachings()
})
</script>

<style scoped>
.home-container { padding: 20px; }
.welcome-card { margin-bottom: 20px; }
.welcome-content { display: flex; justify-content: space-between; align-items: center; }
.welcome-content h2 { margin: 0 0 8px 0; font-weight: 500; }
.date-text { color: #909399; margin: 0; }

.stats-row { margin-bottom: 20px; }
.stat-card { cursor: pointer; transition: transform 0.2s; }
.stat-card:hover { transform: translateY(-3px); }
.stat-content { display: flex; align-items: center; gap: 16px; }
.stat-icon { font-size: 40px; }
.stat-value { font-size: 24px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-label { font-size: 14px; color: #909399; }

.bottom-row { margin-bottom: 20px; }
.shortcut-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; }
.shortcut-item { display: flex; flex-direction: column; align-items: center; padding: 16px; border-radius: 8px; background-color: #f5f7fa; cursor: pointer; transition: all 0.2s; }
.shortcut-item:hover { background-color: #ecf5ff; color: #409EFF; }
.shortcut-item span { margin-top: 8px; font-size: 14px; }

.card-header { display: flex; justify-content: space-between; align-items: center; }
.recent-row { margin-top: 20px; }
</style>