<template>
  <div class="courses-container">
    <!-- 顶部操作栏 -->
    <div class="toolbar">
      <el-select v-model="currentSemester" placeholder="选择学期" @change="fetchMyCourses">
        <el-option label="2024-2025学年第一学期" value="2024-2025学年第一学期" />
        <el-option label="2024-2025学年第二学期" value="2024-2025学年第二学期" />
      </el-select>
      <el-button type="primary" @click="fetchMyCourses">查询</el-button>
      <el-button type="success" @click="goToSelectCourse">去选课</el-button>
    </div>

    <!-- 已选课程表格 -->
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
      <el-table-column prop="classroom" label="教室" width="120" />
      <!-- 新增：成绩列 -->
      <el-table-column label="成绩" width="100">
        <template #default="{ row }">
          <span v-if="row.grade !== null && row.grade !== undefined">
            {{ row.grade }}
          </span>
          <span v-else class="no-grade">未录入</span>
        </template>
      </el-table-column>
      <!-- 操作列：退课按钮，有成绩时禁用 -->
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button
            type="danger"
            size="small"
            :disabled="row.grade !== null && row.grade !== undefined"
            @click="handleDrop(row)"
          >
            退课
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <el-empty v-if="!loading && courseList.length === 0" description="暂无已选课程" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyCourses, dropCourse } from '@/api/selection'

const router = useRouter()

const loading = ref(false)
const currentSemester = ref('2024-2025学年第一学期')
const courseList = ref([])

// 获取已选课程列表
async function fetchMyCourses() {
  loading.value = true
  try {
    const data = await getMyCourses(currentSemester.value)
    courseList.value = data || []
  } catch (error) {
    console.error('获取已选课程失败', error)
    ElMessage.error('获取已选课程失败')
  } finally {
    loading.value = false
  }
}

// 退课操作
async function handleDrop(row) {
  // 二次确认前再次校验（前端二次保障）
  if (row.grade !== null && row.grade !== undefined) {
    ElMessage.warning('该课程已有成绩，无法退课')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要退选课程「${row.courseName}」吗？`,
      '退课确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await dropCourse(row.teachingId)
    ElMessage.success('退课成功')
    fetchMyCourses()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('退课失败', error)
    }
  }
}

// 跳转到选课页面
function goToSelectCourse() {
  router.push('/student/select-course')
}

onMounted(() => {
  fetchMyCourses()
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

.no-grade {
  color: #c0c4cc;
}
</style>