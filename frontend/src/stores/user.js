import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

export const useUserStore = defineStore('user', () => {
  const router = useRouter()

  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  const avatarUrl = computed(() => userInfo.value.avatarUrl || '')

  const isLoggedIn = () => !!token.value
  const isAdmin = () => userInfo.value.role === 'admin'
  const isStudent = () => userInfo.value.role === 'student'
  const isTeacher = () => userInfo.value.role === 'teacher'

  function setLoginData(data) {
    token.value = data.token
    userInfo.value = data
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify(data))
  }

  function updateAvatar(url) {
    userInfo.value = { ...userInfo.value, avatarUrl: url }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  function logout() {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }

  return {
    token,
    userInfo,
    avatarUrl,
    isLoggedIn,
    isAdmin,
    isStudent,
    isTeacher,
    setLoginData,
    updateAvatar,
    logout
  }
})