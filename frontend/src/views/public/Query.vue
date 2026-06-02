<template>
  <div class="public-query-container">
    <div class="header">
      <h1>公开信息查询</h1>
      <el-button type="primary" @click="$router.push('/login')">登录系统</el-button>
    </div>

    <el-tabs v-model="activeTab" type="border-card" @tab-change="handleTabChange">
      <!-- 全部课程标签页 -->
      <el-tab-pane label="全部课程" name="course">
        <el-table :data="courseList" border v-loading="courseLoading">
          <el-table-column prop="courseCode" label="课程代码" width="120" />
          <el-table-column prop="courseName" label="课程名称" min-width="150" />
          <el-table-column prop="credit" label="学分" width="80" />
          <el-table-column prop="hours" label="学时" width="80" />
          <el-table-column prop="typeName" label="课程类型" width="100" />
          <el-table-column prop="semester" label="学期" width="160" />
          <el-table-column prop="maxStudents" label="课容量" width="80" />
          <el-table-column prop="currentStudents" label="已选人数" width="80" />
        </el-table>
        <el-empty v-if="!courseLoading && courseList.length === 0" description="暂无课程数据" />
      </el-tab-pane>

      <!-- 全部教师标签页 -->
      <el-tab-pane label="全部教师" name="teacher">
        <el-table :data="teacherList" border v-loading="teacherLoading">
          <el-table-column prop="teacherId" label="工号" width="100" />
          <el-table-column prop="teacherName" label="姓名" width="100" />
          <el-table-column prop="gender" label="性别" width="80" />
          <el-table-column prop="title" label="职称" width="100" />
          <el-table-column prop="departmentName" label="所属院系" width="120" />
        </el-table>
        <el-empty v-if="!teacherLoading && teacherList.length === 0" description="暂无教师数据" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPublicAllCourses } from '@/api/Course'
import { getPublicAllTeachers } from '@/api/teacher'

const activeTab = ref('course')

const courseLoading = ref(false)
const courseList = ref([])

const teacherLoading = ref(false)
const teacherList = ref([])

// 获取全部课程
async function fetchCourses() {
  courseLoading.value = true
  try {
    courseList.value = await getPublicAllCourses()
  } catch (error) {
    console.error('获取课程失败', error)
    ElMessage.error('获取课程数据失败')
  } finally {
    courseLoading.value = false
  }
}

// 获取全部教师
async function fetchTeachers() {
  teacherLoading.value = true
  try {
    teacherList.value = await getPublicAllTeachers()
  } catch (error) {
    console.error('获取教师失败', error)
    ElMessage.error('获取教师数据失败')
  } finally {
    teacherLoading.value = false
  }
}

// 切换标签时懒加载教师数据
function handleTabChange(tabName) {
  if (tabName === 'teacher' && teacherList.value.length === 0 && !teacherLoading.value) {
    fetchTeachers()
  }
}

onMounted(() => {
  fetchCourses()
})
</script>

<style scoped>
.public-query-container {
  max-width: 1300px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>