import request from '@/utils/request'

/**
 * 录入/修改成绩
 */
export function updateGrade(data) {
  return request({
    url: '/enrollment/grade',
    method: 'put',
    data
  })
}

export function countEnrollments() {
  return request({ url: '/enrollment/count', method: 'get' })
}