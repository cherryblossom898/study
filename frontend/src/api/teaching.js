import request from '@/utils/request'

/**
 * 分页查询排课记录（支持关键词搜索）
 */
export function getTeachingPage(current, size, keyword, semester, teacherId, roomId) {
  return request({
    url: '/teaching/page',
    method: 'get',
    params: { current, size, keyword, semester, teacherId, roomId }
  })
}

/**
 * 新增排课
 */
export function addTeaching(data) {
  return request({
    url: '/teaching',
    method: 'post',
    data
  })
}

/**
 * 修改排课
 */
export function updateTeaching(data) {
  return request({
    url: '/teaching',
    method: 'put',
    data
  })
}

/**
 * 删除排课
 */
export function deleteTeaching(teachingId) {
  return request({
    url: `/teaching/${teachingId}`,
    method: 'delete'
  })
}

/**
 * 获取所有课程（下拉用）
 */
export function getAllCourses() {
  return request({
    url: '/course/all',
    method: 'get'
  })
}

/**
 * 获取所有教师（下拉用）
 */
export function getAllTeachers() {
  return request({
    url: '/teacher/all',
    method: 'get'
  })
}

/**
 * 获取所有时间安排（下拉用）
 */
export function getAllTimeSchedules() {
  return request({
    url: '/time-schedule/all',
    method: 'get'
  })
}

// 获取排课总数
export function countTeachings() {
  return request({
    url: '/teaching/count',
    method: 'get'
  })
}

// 获取最新排课记录（最近5条）
export function getRecentTeachings() {
  return request({
    url: '/teaching/recent',
    method: 'get'
  })
}