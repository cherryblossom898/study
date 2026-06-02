<template>
  <div class="manage-container">
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="工号/姓名">
        <el-input v-model="searchForm.keyword" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border v-loading="loading">
      <el-table-column prop="teacherId" label="工号" width="120" />
      <el-table-column prop="teacherName" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="title" label="职称" width="100" />
      <el-table-column prop="departmentId" label="院系" width="120" />
      <el-table-column prop="teacherPhone" label="联系电话" width="130" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="page.current"
      v-model:page-size="page.size"
      :total="page.total"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: flex-end"
      @size-change="fetchData"
      @current-change="fetchData"
    />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="工号" prop="teacherId" v-if="!isEdit">
          <el-input v-model="form.teacherId" placeholder="请输入工号" maxlength="10" show-word-limit />
        </el-form-item>
        <el-form-item label="姓名" prop="teacherName">
          <el-input v-model="form.teacherName" placeholder="请输入姓名" maxlength="20" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio value="男">男</el-radio>
            <el-radio value="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-select v-model="form.title" placeholder="请选择职称">
            <el-option label="教授" value="教授" />
            <el-option label="副教授" value="副教授" />
            <el-option label="讲师" value="讲师" />
            <el-option label="助教" value="助教" />
          </el-select>
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
        <el-form-item label="电话" prop="teacherPhone">
          <el-input v-model="form.teacherPhone" placeholder="请输入联系电话" maxlength="15" />
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
import { getTeacherPage, addTeacher, updateTeacher, deleteTeacher } from '@/api/teacher'
import { getAllDepartments } from '@/api/department'

const loading = ref(false)
const tableData = ref([])
const page = reactive({ current: 1, size: 10, total: 0 })
const searchForm = reactive({ keyword: '' })

const dialogVisible = ref(false)
const dialogTitle = ref('新增教师')
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  teacherId: '',
  teacherName: '',
  gender: '男',
  title: '',
  departmentId: '',
  teacherPhone: ''
})
const departmentList = ref([])

const rules = {
  teacherId: [
    { required: true, message: '请输入工号', trigger: 'blur' },
    { min: 1, max: 4, message: '工号长度1-4位', trigger: 'blur' }
  ],
  teacherName: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度2-20位', trigger: 'blur' }
  ],
  departmentId: [{ required: true, message: '请选择院系', trigger: 'change' }]
}

async function fetchDepartments() {
  const data = await getAllDepartments()
  departmentList.value = data
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getTeacherPage(page.current, page.size, searchForm.keyword)
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
  dialogTitle.value = '新增教师'
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑教师'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该教师吗？', '提示', { type: 'warning' })
    await deleteTeacher(row.teacherId)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {}
}

async function submitForm() {
  await formRef.value.validate()
  if (isEdit.value) {
    await updateTeacher(form)
    ElMessage.success('修改成功')
  } else {
    await addTeacher(form)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

function resetForm() {
  Object.assign(form, {
    teacherId: '',
    teacherName: '',
    gender: '男',
    title: '',
    departmentId: '',
    teacherPhone: ''
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