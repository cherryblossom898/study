<template>
  <div class="manage-container">
    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="课程代码/名称">
        <el-input v-model="searchForm.keyword" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border v-loading="loading">
      <el-table-column prop="courseCode" label="课程代码" width="120" />
      <el-table-column prop="courseName" label="课程名称" min-width="150" />
      <el-table-column prop="credit" label="学分" width="80" />
      <el-table-column prop="hours" label="学时" width="80" />
      <el-table-column prop="typeId" label="课程类型" width="100" />
      <el-table-column prop="departmentId" label="开课院系" width="120" />
      <el-table-column prop="semester" label="学期" width="160" />
      <el-table-column prop="maxStudents" label="容量" width="80" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page.current"
      v-model:page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: flex-end"
      @size-change="fetchData"
      @current-change="fetchData"
    />

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="550px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="课程代码" prop="courseCode" v-if="!isEdit">
          <el-input
            v-model="form.courseCode"
            placeholder="格式：C+三位数字，如C001"
            maxlength="4"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" maxlength="50" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input-number v-model="form.credit" :min="0.5" :step="0.5" :precision="1" />
        </el-form-item>
        <el-form-item label="学时" prop="hours">
          <el-input-number v-model="form.hours" :min="1" />
        </el-form-item>
        <el-form-item label="课程类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择课程类型">
            <el-option
              v-for="type in courseTypeList"
              :key="type.typeId"
              :label="type.typeName"
              :value="type.typeId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开课院系" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择院系">
            <el-option
              v-for="dept in departmentList"
              :key="dept.departmentId"
              :label="dept.departmentName"
              :value="dept.departmentId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-select v-model="form.semester" placeholder="请选择学期" filterable>
            <el-option
              v-for="sem in semesterOptions"
              :key="sem"
              :label="sem"
              :value="sem"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="最大选课人数" prop="maxStudents">
          <el-input-number v-model="form.maxStudents" :min="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCoursePage, addCourse, updateCourse, deleteCourse, getAllCourseTypes } from '@/api/Course'
import { getAllDepartments } from '@/api/department'

const loading = ref(false)
const tableData = ref([])
const page = reactive({ current: 1, size: 10, total: 0 })
const searchForm = reactive({ keyword: '' })

const dialogVisible = ref(false)
const dialogTitle = ref('新增课程')
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  courseCode: '',
  courseName: '',
  credit: 1.0,
  hours: 32,
  typeId: '',
  departmentId: '',
  semester: '',
  maxStudents: 50
})
const departmentList = ref([])
const courseTypeList = ref([])

// 学期选项
const semesterOptions = ref([
  '2024-2025学年第一学期',
  '2024-2025学年第二学期',
  '2025-2026学年第一学期',
  '2025-2026学年第二学期'
])

// 表单校验规则（严格限制课程代码为 C + 三位数字）
const rules = {
  courseCode: [
    { required: true, message: '请输入课程代码', trigger: 'blur' },
    { pattern: /^C\d{3}$/, message: '课程代码格式必须为C+三位数字（如C001）', trigger: 'blur' },
    { pattern: /^C\d{3}$/, message: '课程代码格式必须为C+三位数字（如C001）', trigger: 'change' }
  ],
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
    { max: 50, message: '课程名称不超过50位', trigger: 'blur' }
  ],
  credit: [{ required: true, message: '请输入学分', trigger: 'blur' }],
  hours: [{ required: true, message: '请输入学时', trigger: 'blur' }],
  typeId: [{ required: true, message: '请选择课程类型', trigger: 'change' }],
  departmentId: [{ required: true, message: '请选择开课院系', trigger: 'change' }],
  semester: [{ required: true, message: '请选择学期', trigger: 'change' }],
  maxStudents: [{ required: true, message: '请输入最大选课人数', trigger: 'blur' }]
}

async function fetchDepartments() {
  const data = await getAllDepartments()
  departmentList.value = data
}

async function fetchCourseTypes() {
  const data = await getAllCourseTypes()
  courseTypeList.value = data
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getCoursePage(page.current, page.size, searchForm.keyword)
    tableData.value = res.records
    page.total = res.total
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  searchForm.keyword = ''
  fetchData()
}

function handleAdd() {
  isEdit.value = false
  dialogTitle.value = '新增课程'
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑课程'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该课程吗？', '提示', { type: 'warning' })
    await deleteCourse(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {}
}

// 提交前双重校验课程代码格式
async function submitForm() {
  // 手动二次校验（防止绕过表单校验）
  if (!isEdit.value && !/^C\d{3}$/.test(form.courseCode)) {
    ElMessage.error('课程代码格式必须为C+三位数字（如C001）')
    return
  }
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateCourse(form)
      ElMessage.success('修改成功')
    } else {
      await addCourse(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    // 校验失败或请求错误
  }
}

function resetForm() {
  Object.assign(form, {
    id: null,
    courseCode: '',
    courseName: '',
    credit: 1.0,
    hours: 32,
    typeId: '',
    departmentId: '',
    semester: '',
    maxStudents: 50
  })
  formRef.value?.clearValidate()
}

onMounted(() => {
  fetchDepartments()
  fetchCourseTypes()
  fetchData()
})
</script>

<style scoped>
.manage-container { padding: 20px; background: #fff; border-radius: 4px; }
.search-form { margin-bottom: 20px; }
</style>