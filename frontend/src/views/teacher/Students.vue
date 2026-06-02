<template>
  <div class="students-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>选课学生名单</span>
          <el-button @click="$router.go(-1)">返回</el-button>
        </div>
      </template>

      <el-table :data="studentList" border v-loading="loading">
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="major" label="专业" />
        <el-table-column prop="className" label="班级" width="120" />
        <!-- 成绩列：使用 el-input-number 限制范围 -->
        <el-table-column label="成绩" width="150">
          <template #default="{ row }">
            <el-input-number
              v-model="row.grade"
              :min="0"
              :max="100"
              :step="1"
              size="small"
              controls-position="right"
              placeholder="未录入"
            />
          </template>
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              :loading="row.saving"
              @click="saveGrade(row)"
            >
              保存
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="!loading && studentList.length === 0" description="暂无学生选课" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTeachingStudents } from '@/api/teacher'
import { updateGrade } from '@/api/enrollment'

const route = useRoute()
const teachingId = route.params.teachingId
const studentList = ref([])
const loading = ref(false)

// 获取学生名单
async function fetchStudents() {
  loading.value = true
  try {
    const data = await getTeachingStudents(teachingId)
    // 为每个学生添加 saving 状态
    studentList.value = (data || []).map(item => ({
      ...item,
      saving: false
    }))
  } catch (error) {
    console.error('获取学生名单失败', error)
    ElMessage.error('获取学生名单失败')
  } finally {
    loading.value = false
  }
}

// 保存成绩（带前端范围校验）
async function saveGrade(row) {
  // 1. 检查是否已输入成绩
  if (row.grade === undefined || row.grade === null) {
    ElMessage.warning('请输入成绩')
    return
  }
  
  // 2. 前端二次校验范围（防止绕过组件限制）
  if (row.grade < 0 || row.grade > 100) {
    ElMessage.warning('成绩必须在0到100之间')
    return
  }

  row.saving = true
  try {
    await updateGrade({
      enrollmentId: row.enrollmentId,
      grade: row.grade
    })
    ElMessage.success('成绩保存成功')
  } catch (error) {
    console.error('保存成绩失败', error)
    // 错误已在拦截器中提示
  } finally {
    row.saving = false
  }
}

onMounted(() => {
  fetchStudents()
})
</script>

<style scoped>
.students-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>