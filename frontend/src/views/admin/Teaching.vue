<template>
  <div class="manage-container">
    <!-- 搜索栏 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="关键词">
        <el-input v-model="searchForm.keyword" placeholder="教学班ID/课程/教师" clearable />
      </el-form-item>
      <el-form-item label="学期">
        <el-input v-model="searchForm.semester" placeholder="学期" clearable />
      </el-form-item>
      <el-form-item label="教师">
        <el-select v-model="searchForm.teacherId" placeholder="选择教师" clearable filterable>
          <el-option
            v-for="t in teacherList"
            :key="t.teacherId"
            :label="t.teacherName"
            :value="t.teacherId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="教室">
        <el-select v-model="searchForm.roomId" placeholder="选择教室" clearable filterable>
          <el-option
            v-for="r in classroomList"
            :key="r.roomId"
            :label="r.roomName"
            :value="r.roomId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchData">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="success" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableData" border v-loading="loading">
      <el-table-column prop="teachingId" label="教学班ID" width="100" />
      <el-table-column prop="courseName" label="课程" min-width="120" />
      <el-table-column prop="teacherName" label="教师" width="100" />
      <el-table-column prop="roomName" label="教室" width="120" />
      <el-table-column label="上课时间" width="200">
        <template #default="{ row }">
          周{{ row.weekday }} {{ row.section }} ({{ row.startTime }}-{{ row.endTime }})
        </template>
      </el-table-column>
      <el-table-column prop="semester" label="学期" width="160" />
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
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="form.courseId" placeholder="选择课程" filterable>
            <el-option
              v-for="c in courseList"
              :key="c.id"
              :label="`${c.courseName} (${c.courseCode})`"
              :value="c.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教师" prop="teacherId">
          <el-select v-model="form.teacherId" placeholder="选择教师" filterable>
            <el-option
              v-for="t in teacherList"
              :key="t.teacherId"
              :label="t.teacherName"
              :value="t.teacherId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教室" prop="roomId">
          <el-select v-model="form.roomId" placeholder="选择教室" filterable>
            <el-option
              v-for="r in classroomList"
              :key="r.roomId"
              :label="`${r.roomName} (容量:${r.capacity})`"
              :value="r.roomId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间安排" prop="scheduleId">
          <el-select v-model="form.scheduleId" placeholder="选择时间" filterable>
            <el-option
              v-for="s in timeScheduleList"
              :key="s.scheduleId"
              :label="`周${s.weekday} ${s.section} (${s.startTime}-${s.endTime})`"
              :value="s.scheduleId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="form.semester" placeholder="例如：2024-2025学年第一学期" />
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" placeholder="选择开始日期" />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" placeholder="选择结束日期" />
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
import {
  getTeachingPage,
  addTeaching,
  updateTeaching,
  deleteTeaching,
  getAllCourses,
  getAllTeachers,
  getAllTimeSchedules
} from '@/api/teaching'
import { getAllClassrooms } from '@/api/classroom'

const loading = ref(false)
const tableData = ref([])
const page = reactive({ current: 1, size: 10, total: 0 })
const searchForm = reactive({
  keyword: '',
  semester: '',
  teacherId: '',
  roomId: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('新增排课')
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  teachingId: null,
  courseId: null,
  teacherId: '',
  roomId: '',
  scheduleId: '',
  semester: '',
  startDate: null,
  endDate: null
})

const courseList = ref([])
const teacherList = ref([])
const classroomList = ref([])
const timeScheduleList = ref([])

const rules = {
  courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
  teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
  roomId: [{ required: true, message: '请选择教室', trigger: 'change' }],
  scheduleId: [{ required: true, message: '请选择时间安排', trigger: 'change' }],
  semester: [{ required: true, message: '请输入学期', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

// 加载下拉选项
async function fetchOptions() {
  try {
    const [courses, teachers, classrooms, schedules] = await Promise.all([
      getAllCourses(),
      getAllTeachers(),
      getAllClassrooms(),
      getAllTimeSchedules()
    ])
    courseList.value = courses
    teacherList.value = teachers
    classroomList.value = classrooms
    timeScheduleList.value = schedules
  } catch (error) {
    console.error('获取下拉数据失败', error)
  }
}

// 加载表格数据
async function fetchData() {
  loading.value = true
  try {
    const res = await getTeachingPage(
      page.current,
      page.size,
      searchForm.keyword,
      searchForm.semester,
      searchForm.teacherId,
      searchForm.roomId
    )
    tableData.value = res.records || []
    page.total = res.total || 0
  } catch (error) {
    console.error('获取排课列表失败', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

function resetSearch() {
  Object.assign(searchForm, {
    keyword: '',
    semester: '',
    teacherId: '',
    roomId: ''
  })
  fetchData()
}

function handleAdd() {
  isEdit.value = false
  dialogTitle.value = '新增排课'
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑排课'
  Object.assign(form, row)
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定删除该排课记录吗？', '提示', { type: 'warning' })
    await deleteTeaching(row.teachingId)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    // 取消或失败
  }
}

async function submitForm() {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateTeaching(form)
      ElMessage.success('修改成功')
    } else {
      await addTeaching(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    // 校验失败
  }
}

function resetForm() {
  Object.assign(form, {
    teachingId: null,
    courseId: null,
    teacherId: '',
    roomId: '',
    scheduleId: '',
    semester: '',
    startDate: null,
    endDate: null
  })
  formRef.value?.clearValidate()
}

onMounted(() => {
  fetchOptions()
  fetchData()
})
</script>

<style scoped>
.manage-container { padding: 20px; background: #fff; border-radius: 4px; }
.search-form { margin-bottom: 20px; }
</style>