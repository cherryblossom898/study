<template>
  <div class="schedule-container">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <el-select v-model="currentSemester" placeholder="选择学期" @change="fetchSchedule">
        <el-option label="2024-2025学年第一学期" value="2024-2025学年第一学期" />
        <el-option label="2024-2025学年第二学期" value="2024-2025学年第二学期" />
      </el-select>
      <el-button type="primary" @click="fetchSchedule">查询</el-button>
    </div>

    <!-- 课表表格 -->
    <el-table
      :data="scheduleData"
      border
      style="width: 100%; margin-top: 20px"
      v-loading="loading"
    >
      <!-- 节次列 -->
      <el-table-column prop="section" label="节次" width="100" fixed />
      
      <!-- 周一至周日 -->
      <el-table-column
        v-for="day in weekdays"
        :key="day.value"
        :label="day.label"
        :prop="day.value"
      >
        <template #default="{ row }">
          <div v-if="row[day.value]" class="course-cell">
            <div class="course-name">{{ row[day.value].courseName }}</div>
            <div class="course-info">{{ row[day.value].teacherName }}</div>
            <div class="course-info">{{ row[day.value].classroomName }}</div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态提示 -->
    <el-empty v-if="!loading && scheduleData.length === 0" description="暂无课表数据" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getStudentSchedule } from '@/api/schedule'

// 星期配置
const weekdays = [
  { label: '周一', value: '1' },
  { label: '周二', value: '2' },
  { label: '周三', value: '3' },
  { label: '周四', value: '4' },
  { label: '周五', value: '5' },
  { label: '周六', value: '6' },
  { label: '周日', value: '7' }
]

// 节次配置（需与数据库 time_schedule 表的 section 字段对应）
const sections = [
  '1-2节',
  '3-4节',
  '5-6节',
  '7-8节',
  '9-10节'
]

const loading = ref(false)
const currentSemester = ref('2024-2025学年第一学期')
const rawSchedule = ref([])        // 原始课表数据（数组形式）
const scheduleData = ref([])       // 转换后的表格数据（按节次组织）

// 将原始数据转换为表格行数据
function transformSchedule(data) {
  // 初始化一个以节次为 key 的对象
  const sectionMap = {}
  sections.forEach(section => {
    sectionMap[section] = { section }
    weekdays.forEach(day => {
      sectionMap[section][day.value] = null
    })
  })

  // 填充课程数据
  data.forEach(item => {
    const section = item.section   // 如 "1-2节"
    const weekday = item.weekday   // 如 "1"
    if (sectionMap[section]) {
      sectionMap[section][weekday] = item
    }
  })

  // 转换为数组
  return Object.values(sectionMap)
}

// 获取课表数据
async function fetchSchedule() {
  loading.value = true
  try {
    const data = await getStudentSchedule(currentSemester.value)
    rawSchedule.value = data || []
    scheduleData.value = transformSchedule(rawSchedule.value)
  } catch (error) {
    console.error('获取课表失败', error)
    ElMessage.error('获取课表失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchSchedule()
})
</script>

<style scoped>
.schedule-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
}

.toolbar {
  display: flex;
  gap: 10px;
  align-items: center;
}

.course-cell {
  padding: 8px 4px;
  text-align: center;
}

.course-name {
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}

.course-info {
  font-size: 12px;
  color: #606266;
  line-height: 1.5;
}
</style>