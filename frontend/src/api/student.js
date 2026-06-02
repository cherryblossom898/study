import request from '@/utils/request'

/**
 * 获取当前学生个人信息
 */
export function getStudentProfile() {
  return request({
    url: '/student/profile',
    method: 'get'
  })
}

export function countStudents() {
  return request({ url: '/student/count', method: 'get' })
}


// 分页查询学生（支持关键字搜索）
export function getStudentPage(current, size, keyword) {
  return request({
    url: '/student/page',
    method: 'get',
    params: { current, size, keyword }
  })
}

// 新增学生
export function addStudent(data) {
  return request({
    url: '/student',
    method: 'post',
    data
  })
}

// 修改学生
export function updateStudent(data) {
  return request({
    url: '/student',
    method: 'put',
    data
  })
}

// 删除学生
export function deleteStudent(studentId) {
  return request({
    url: `/student/${studentId}`,
    method: 'delete'
  })
}