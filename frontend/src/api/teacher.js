import request from '@/utils/request'

/**
 * 获取教学班学生名单
 */
export function getTeachingStudents(teachingId) {
  return request({
    url: `/teacher/teaching/${teachingId}/students`,
    method: 'get'
  })
}


/**
 * 获取当前教师的个人信息
 */
export function getTeacherProfile() {
  return request({
    url: '/teacher/profile',
    method: 'get'
  })
}

export function countTeachers() {
  return request({ url: '/teacher/count', method: 'get' })
}


// 分页查询教师
export function getTeacherPage(current, size, keyword) {
  return request({
    url: '/teacher/page',
    method: 'get',
    params: { current, size, keyword }
  })
}

// 新增教师
export function addTeacher(data) {
  return request({
    url: '/teacher',
    method: 'post',
    data
  })
}

// 修改教师
export function updateTeacher(data) {
  return request({
    url: '/teacher',
    method: 'put',
    data
  })
}

// 删除教师
export function deleteTeacher(teacherId) {
  return request({
    url: `/teacher/${teacherId}`,
    method: 'delete'
  })
}

// 获取全部教师（公开，不分页）
export function getPublicAllTeachers() {
  return request({
    url: '/teacher/public/all',
    method: 'get'
  })
}