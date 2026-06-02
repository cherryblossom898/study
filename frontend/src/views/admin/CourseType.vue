<template>
  <div class="manage-container">
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="类型编号/名称">
        <el-input v-model="searchForm.keyword" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" border v-loading="loading">
      <el-table-column prop="typeId" label="类型编号" width="120" />
      <el-table-column prop="typeName" label="类型名称" min-width="150" />
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="400px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="类型编号" prop="typeId" v-if="!isEdit">
          <el-input v-model="form.typeId" placeholder="请输入编号" maxlength="10" />
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入名称" maxlength="20" />
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
import { getCourseTypePage, addCourseType, updateCourseType, deleteCourseType } from '@/api/courseType'

const loading = ref(false)
const tableData = ref([])
const page = reactive({ current: 1, size: 10, total: 0 })
const searchForm = reactive({ keyword: '' })

const dialogVisible = ref(false)
const dialogTitle = ref('新增课程类型')
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  typeId: '',
  typeName: ''
})

const rules = {
  typeId: [
    { required: true, message: '请输入类型编号', trigger: 'blur' },
    { max: 10, message: '编号不超过10位', trigger: 'blur' }
  ],
  typeName: [
    { required: true, message: '请输入类型名称', trigger: 'blur' },
    { max: 20, message: '名称不超过20位', trigger: 'blur' }
  ]
}

async function fetchData() {
  loading.value = true
  try {
    const res = await getCourseTypePage(page.current, page.size, searchForm.keyword)
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
  dialogTitle.value = '新增课程类型'
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑课程类型'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该课程类型吗？', '提示', { type: 'warning' })
    await deleteCourseType(row.typeId)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {}
}

async function submitForm() {
  await formRef.value.validate()
  if (isEdit.value) {
    await updateCourseType(form)
    ElMessage.success('修改成功')
  } else {
    await addCourseType(form)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  fetchData()
}

function resetForm() {
  Object.assign(form, {
    typeId: '',
    typeName: ''
  })
  formRef.value?.clearValidate()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.manage-container { padding: 20px; background: #fff; border-radius: 4px; }
.search-form { margin-bottom: 20px; }
</style>