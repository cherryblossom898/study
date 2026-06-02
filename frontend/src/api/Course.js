import request from '@/utils/request'

// 分页查询课程（管理员）
export function getCoursePage(current, size, keyword) {
  return request({
    url: '/course/page',
    method: 'get',
    params: { current, size, keyword }
  })
}

// 新增课程
export function addCourse(data) {
  return request({
    url: '/course',
    method: 'post',
    data
  })
}

// 修改课程
export function updateCourse(data) {
  return request({
    url: '/course',
    method: 'put',
    data
  })
}

// 删除课程
export function deleteCourse(id) {
  return request({
    url: `/course/${id}`,
    method: 'delete'
  })
}

// 获取所有课程类型（下拉用）
export function getAllCourseTypes() {
  return request({
    url: '/course-type/all',
    method: 'get'
  })
}

// 获取所有课程（下拉用）
export function getAllCourses() {
  return request({
    url: '/course/all',
    method: 'get'
  })
}

// 获取课程总数（统计卡片用）
export function countCourses() {
  return request({
    url: '/course/count',
    method: 'get'
  })
}

// 获取热门课程 Top5
export function getPopularCourses() {
  return request({
    url: '/course/popular',
    method: 'get'
  })
}

// 获取全部课程（公开，不分页）
export function getPublicAllCourses() {
  return request({
    url: '/course/public/all',
    method: 'get'
  })
}