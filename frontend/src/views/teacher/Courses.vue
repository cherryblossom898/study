<template>
  <div class="courses-container">
    <div class="toolbar">
      <el-select
        v-model="currentSemester"
        placeholder="选择学期"
        @change="fetchCourses"
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
      <el-button type="primary" @click="fetchCourses">查询</el-button>
    </div>

    <el-table
      :data="courseList"
      border
      style="width: 100%; margin-top: 20px"
      v-loading="loading"
    >
      <el-table-column prop="courseName" label="课程名称" min-width="150" />
      <el-table-column prop="credit" label="学分" width="80" />
      <el-table-column prop="hours" label="学时" width="80" />
      <el-table-column prop="classroomName" label="教室" width="120" />
      <el-table-column label="上课时间" width="180">
        <template #default="{ row }">
          周{{ row.weekday }} {{ row.section }}节
        </template>
      </el-table-column>
      <el-table-column label="课容量" width="100">
        <template #default="{ row }">
          {{ row.selectedCount }}/{{ row.capacity }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
          <el-button
            type="primary"
            size="small"
            @click="goToStudents(row.teachingId)"
          >
            查看名单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && courseList.length === 0" description="暂无授课课程" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTeacherSchedule } from '@/api/schedule'

const router = useRouter()

const loading = ref(false)
const currentSemester = ref('2024-2025学年第一学期')
const courseList = ref([])

// 从课表数据中提取去重课程
function extractCourses(scheduleData) {
  const courseMap = new Map()
  scheduleData.forEach(item => {
    // 以 teachingId 为 key 去重
    if (!courseMap.has(item.teachingId)) {
      courseMap.set(item.teachingId, item)
    }
  })
  return Array.from(courseMap.values())
}

async function fetchCourses() {
  loading.value = true
  try {
    const data = await getTeacherSchedule(currentSemester.value)
    courseList.value = extractCourses(data || [])
  } catch (error) {
    console.error('获取课程列表失败', error)
    ElMessage.error('获取课程列表失败')
  } finally {
    loading.value = false
  }
}

function goToStudents(teachingId) {
  router.push(`/teacher/students/${teachingId}`)
}

onMounted(() => {
  fetchCourses()
})
</script>

<style scoped>
.courses-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
}

.toolbar {
  display: flex;
  gap: 10px;
  align-items: center;
}
</style>