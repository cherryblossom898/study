<template>
  <div class="schedule-container">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <el-select
        v-model="currentSemester"
        placeholder="选择学期"
        @change="fetchSchedule"
      >
        <el-option
          label="2024-2025学年第一学期"
          value="2024-2025学年第一学期"
        />
        <el-option
          label="2024-2025学年第二学期"
          value="2024-2025学年第二学期"
        />
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
      <!-- 第一列：节次 -->
      <el-table-column prop="section" label="节次" width="100" fixed />

      <!-- 周一至周日列 -->
      <el-table-column
        v-for="day in weekdays"
        :key="day.value"
        :label="day.label"
        :prop="day.value"
      >
        <template #default="{ row }">
          <div v-if="row[day.value]" class="course-cell">
            <div class="course-name">{{ row[day.value].courseName }}</div>
            <div class="course-info">{{ row[day.value].className }}</div>
            <div class="course-info">{{ row[day.value].classroomName }}</div>
            <div class="course-info">
              已选: {{ row[day.value].selectedCount }}/{{ row[day.value].capacity }}
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <el-empty
      v-if="!loading && scheduleData.length === 0"
      description="暂无授课安排"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getTeacherSchedule } from '@/api/schedule'

// 星期配置（value 对应后端返回的 weekday 字段）
const weekdays = [
  { label: '周一', value: '1' },
  { label: '周二', value: '2' },
  { label: '周三', value: '3' },
  { label: '周四', value: '4' },
  { label: '周五', value: '5' },
  { label: '周六', value: '6' },
  { label: '周日', value: '7' }
]

// 固定节次（与数据库 time_schedule 表的 section 字段一致）
const sections = ['1-2节', '3-4节', '5-6节', '7-8节', '9-10节']

// 响应式数据
const loading = ref(false)
const currentSemester = ref('2024-2025学年第一学期')
const scheduleData = ref([])   // 转换后的表格数据

/**
 * 将后端返回的平铺数组转换为表格行数据
 * 返回格式：[ { section: '1-2节', '1': {...}, '2': {...}, ... }, ... ]
 */
function transformSchedule(data) {
  // 1. 初始化一个以节次为 key 的对象
  const sectionMap = {}
  sections.forEach(section => {
    sectionMap[section] = { section }   // 每行都有 section 属性
    weekdays.forEach(day => {
      sectionMap[section][day.value] = null   // 每天初始为空
    })
  })

  // 2. 遍历后端数据，填充到对应位置
  data.forEach(item => {
    const section = item.section   // 如 "1-2节"
    const weekday = item.weekday   // 如 "1"
    if (sectionMap[section]) {
      sectionMap[section][weekday] = item
    }
  })

  // 3. 转换为数组返回
  return Object.values(sectionMap)
}

/**
 * 获取教师授课安排
 */
async function fetchSchedule() {
  loading.value = true
  try {
    const data = await getTeacherSchedule(currentSemester.value)
    scheduleData.value = transformSchedule(data || [])
  } catch (error) {
    console.error('获取授课安排失败', error)
    ElMessage.error('获取授课安排失败')
  } finally {
    loading.value = false
  }
}

// 组件挂载时加载数据
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