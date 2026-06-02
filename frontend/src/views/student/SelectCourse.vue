<template>
  <div class="select-course-container">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <el-select v-model="currentSemester" placeholder="选择学期" @change="fetchAvailableCourses">
        <el-option label="2024-2025学年第一学期" value="2024-2025学年第一学期" />
        <el-option label="2024-2025学年第二学期" value="2024-2025学年第二学期" />
      </el-select>
      <el-button type="primary" @click="fetchAvailableCourses">查询</el-button>
      <el-button @click="goToMyCourses">查看已选课程</el-button>
    </div>

    <!-- 可选课程表格 -->
    <el-table
      :data="courseList"
      border
      style="width: 100%; margin-top: 20px"
      v-loading="loading"
    >
      <el-table-column prop="courseName" label="课程名称" min-width="150" />
      <el-table-column prop="teacherName" label="授课教师" width="120" />
      <el-table-column prop="credit" label="学分" width="80" />
      <el-table-column prop="hours" label="学时" width="80" />
      <el-table-column label="上课时间" width="180">
        <template #default="{ row }">
          周{{ row.weekday }} {{ row.section }}节
        </template>
      </el-table-column>
      <el-table-column prop="classroomName" label="教室" width="120" />
      <el-table-column label="课容量" width="100">
        <template #default="{ row }">
          {{ row.currentStudents }}/{{ row.maxStudents }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="!row.isSelected"
            type="primary"
            size="small"
            :disabled="row.currentStudents >= row.maxStudents"
            @click="handleSelect(row)"
          >
            {{ row.currentStudents >= row.maxStudents ? '已满' : '选课' }}
          </el-button>
          <el-tag v-else type="success" size="small">已选</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <el-empty v-if="!loading && courseList.length === 0" description="暂无可选课程" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAvailableCourses, selectCourse } from '@/api/selection'

const router = useRouter()

const loading = ref(false)
const currentSemester = ref('2024-2025学年第一学期')
const courseList = ref([])

// 获取可选课程列表
async function fetchAvailableCourses() {
  loading.value = true
  try {
    const data = await getAvailableCourses(currentSemester.value)
    courseList.value = data || []
  } catch (error) {
    console.error('获取可选课程失败', error)
    ElMessage.error('获取可选课程失败')
  } finally {
    loading.value = false
  }
}

// 选课操作
async function handleSelect(row) {
  try {
    await selectCourse(row.teachingId)
    ElMessage.success('选课成功')
    // 刷新列表，更新按钮状态
    fetchAvailableCourses()
  } catch (error) {
    console.error('选课失败', error)
    // 错误信息已在拦截器中提示
  }
}

// 跳转到已选课程页面
function goToMyCourses() {
  router.push('/student/courses')
}

onMounted(() => {
  fetchAvailableCourses()
})
</script>

<style scoped>
.select-course-container {
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