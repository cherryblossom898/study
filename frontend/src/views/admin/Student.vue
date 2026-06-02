<template>
  <div class="manage-container">
    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="学号/姓名">
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
      <el-table-column prop="studentId" label="学号" width="120" />
      <el-table-column prop="studentName" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="major" label="专业" />
      <el-table-column prop="clazz" label="班级" width="120" />
      <el-table-column prop="departmentId" label="院系" width="120" />
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
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="学号" prop="studentId" v-if="!isEdit">
          <el-input
            v-model="form.studentId"
            placeholder="请输入9位学号"
            maxlength="9"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="姓名" prop="studentName">
          <el-input v-model="form.studentName" placeholder="请输入姓名" maxlength="20" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio value="男">男</el-radio>
            <el-radio value="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="入学年份" prop="enrollmentYear">
          <el-select
            v-model="form.enrollmentYear"
            placeholder="请选择入学年份"
            @change="onYearChange"
          >
            <el-option
              v-for="year in yearOptions"
              :key="year"
              :label="year"
              :value="year"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-select
            v-model="form.grade"
            placeholder="请选择年级"
            :disabled="!form.enrollmentYear"
          >
            <el-option
              v-for="grade in gradeOptions"
              :key="grade"
              :label="grade"
              :value="grade"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-select v-model="form.major" placeholder="请选择专业" filterable>
            <el-option
              v-for="major in majorOptions"
              :key="major"
              :label="major"
              :value="major"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="clazz">
          <el-input v-model="form.clazz" placeholder="请输入班级" maxlength="20" />
        </el-form-item>
        <el-form-item label="院系" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择院系">
            <el-option
              v-for="dept in departmentList"
              :key="dept.departmentId"
              :label="dept.departmentName"
              :value="dept.departmentId"
            />
          </el-select>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getStudentPage, addStudent, updateStudent, deleteStudent } from '@/api/student'
import { getAllDepartments } from '@/api/department'

const loading = ref(false)
const tableData = ref([])
const page = reactive({ current: 1, size: 10, total: 0 })
const searchForm = reactive({ keyword: '' })

const dialogVisible = ref(false)
const dialogTitle = ref('新增学生')
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  studentId: '',
  studentName: '',
  gender: '男',
  enrollmentYear: '',
  grade: '',
  major: '',
  clazz: '',
  departmentId: ''
})
const departmentList = ref([])

// 年份选项（近10年）
const yearOptions = ref([])
for (let i = 0; i < 10; i++) {
  yearOptions.value.push(String(new Date().getFullYear() - i))
}

// 年级选项（根据入学年份动态生成）
const gradeOptions = computed(() => {
  if (!form.enrollmentYear) return []
  return [form.enrollmentYear + '级']
})

// 专业选项（静态）
const majorOptions = ref([
  '计算机科学与技术',
  '软件工程',
  '电子信息工程',
  '数据科学与大数据技术',
  '人工智能'
])

// 入学年份变化时自动设置年级
function onYearChange(year) {
  if (year) {
    form.grade = year + '级'
  } else {
    form.grade = ''
  }
}

// 表单校验规则
const rules = {
  studentId: [
    { required: true, message: '请输入学号', trigger: 'blur' },
    { min: 9, max: 9, message: '学号必须为9位数字', trigger: 'blur' },
    { pattern: /^\d{9}$/, message: '学号必须为9位数字', trigger: 'blur' },
    { pattern: /^\d{9}$/, message: '学号必须为9位数字', trigger: 'change' }
  ],
  studentName: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度为2-20个字符', trigger: 'blur' }
  ],
  enrollmentYear: [{ required: true, message: '请选择入学年份', trigger: 'change' }],
  grade: [{ required: true, message: '请选择年级', trigger: 'change' }],
  major: [{ required: true, message: '请选择专业', trigger: 'change' }],
  clazz: [
    { required: true, message: '请输入班级', trigger: 'blur' },
    { max: 20, message: '班级长度不超过20个字符', trigger: 'blur' }
  ],
  departmentId: [{ required: true, message: '请选择院系', trigger: 'change' }]
}

async function fetchDepartments() {
  try {
    const data = await getAllDepartments()
    departmentList.value = data
  } catch (error) {
    console.error('获取院系失败', error)
  }
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getStudentPage(page.current, page.size, searchForm.keyword)
    tableData.value = res.records || []
    page.total = res.total || 0
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
  dialogTitle.value = '新增学生'
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑学生'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该学生吗？', '提示', { type: 'warning' })
    await deleteStudent(row.studentId)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    // 取消删除
  }
}

async function submitForm() {
  // 手动二次校验学号格式
  if (!isEdit.value && !/^\d{9}$/.test(form.studentId)) {
    ElMessage.error('学号必须为9位数字')
    return
  }
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateStudent(form)
      ElMessage.success('修改成功')
    } else {
      await addStudent(form)
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
    studentId: '',
    studentName: '',
    gender: '男',
    enrollmentYear: '',
    grade: '',
    major: '',
    clazz: '',
    departmentId: ''
  })
  formRef.value?.clearValidate()
}

onMounted(() => {
  fetchDepartments()
  fetchData()
})
</script>

<style scoped>
.manage-container { padding: 20px; background: #fff; border-radius: 4px; }
.search-form { margin-bottom: 20px; }
</style>