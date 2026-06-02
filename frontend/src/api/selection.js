import request from '@/utils/request'

/**
 * 获取可选课程列表（选课页面用）
 */
export function getAvailableCourses(semester) {
  return request({
    url: '/selection/available',
    method: 'get',
    params: { semester }
  })
}

/**
 * 选课
 */
export function selectCourse(teachingId) {
  return request({
    url: '/selection/select',
    method: 'post',
    data: { teachingId }
  })
}

/**
 * 获取已选课程列表
 */
export function getMyCourses(semester) {
  return request({
    url: '/selection/my',
    method: 'get',
    params: { semester }
  })
}

/**
 * 退课
 */
export function dropCourse(teachingId) {
  return request({
    url: '/selection/drop',
    method: 'delete',
    params: { teachingId }
  })
}