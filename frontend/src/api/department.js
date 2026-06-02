import request from '@/utils/request'

export function getAllDepartments() {
  return request({
    url: '/department/all',
    method: 'get'
  })
}