import request from '@/utils/request'

export function getClassroomPage(current, size, keyword) {
  return request({
    url: '/classroom/page',
    method: 'get',
    params: { current, size, keyword }
  })
}

export function addClassroom(data) {
  return request({
    url: '/classroom',
    method: 'post',
    data
  })
}

export function updateClassroom(data) {
  return request({
    url: '/classroom',
    method: 'put',
    data
  })
}

export function deleteClassroom(roomId) {
  return request({
    url: `/classroom/${roomId}`,
    method: 'delete'
  })
}

/**
 * 获取所有教室（用于下拉框）
 */
export function getAllClassrooms() {
  return request({
    url: '/classroom/all',
    method: 'get'
  })
}

export function countClassrooms() {
  return request({ url: '/classroom/count', method: 'get' })
}