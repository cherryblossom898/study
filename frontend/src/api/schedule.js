import request from '@/utils/request'

/**
 * 获取学生个人课表
 * @param {string} semester - 学期（可选，不传则查询所有学期）
 */
export function getStudentSchedule(semester) {
  return request({
    url: '/schedule/student',
    method: 'get',
    params: { semester }
  })
}


/**
 * 获取教师授课安排
 */
export function getTeacherSchedule(semester) {
  return request({
    url: '/schedule/teacher',
    method: 'get',
    params: { semester }
  })
}