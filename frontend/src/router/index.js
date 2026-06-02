import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: () => import('@/views/login/index.vue') },
  
  // 学生端（嵌套路由）
  {
    path: '/student',
    component: () => import('@/views/student/Layout.vue'),
    meta: { requiresAuth: true, role: 'student' },
    children: [
      { path: '', redirect: '/student/home' },
      { path: 'home', component: () => import('@/views/student/Home.vue'), meta: { title: '首页' } },
      { path: 'schedule', component: () => import('@/views/student/Schedule.vue'), meta: { title: '个人课表' } },
      { path: 'courses', component: () => import('@/views/student/Courses.vue'), meta: { title: '已选课程' } },
      { path: 'profile', component: () => import('@/views/student/Profile.vue'), meta: { title: '个人信息' } },
      { path: 'password', component: () => import('@/views/student/Password.vue'), meta: { title: '修改密码' } },
      { path: 'select-course', component: () => import('@/views/student/SelectCourse.vue'), meta: { title: '选课中心' } },
    ]
  },
  
  // 教师端
  
  {
  path: '/teacher',
  component: () => import('@/views/teacher/Layout.vue'),
  meta: { requiresAuth: true, role: 'teacher' },
  children: [
    { path: '', redirect: '/teacher/home' },
    { path: 'home', component: () => import('@/views/teacher/Home.vue'), meta: { title: '首页' } },
    { path: 'schedule', component: () => import('@/views/teacher/Schedule.vue'), meta: { title: '我的授课' } },
    { path: 'profile', component: () => import('@/views/teacher/Profile.vue'), meta: { title: '个人信息' } },
    { path: 'password', component: () => import('@/views/teacher/Password.vue'), meta: { title: '修改密码' } },
    { path: 'students/:teachingId', component: () => import('@/views/teacher/Students.vue'), meta: { title: '学生名单' } },
    { path: 'courses', component: () => import('@/views/teacher/Courses.vue'), meta: { title: '我的课程' } }
  ]
  },
  
  // 管理员端（暂时占位）
  {
  path: '/admin',
  component: () => import('@/views/admin/Layout.vue'),
  meta: { requiresAuth: true, role: 'admin' },
  children: [
    { path: '', redirect: '/admin/home' },
    { path: 'home', component: () => import('@/views/admin/Home.vue'), meta: { title: '首页' } },
    { path: 'student', component: () => import('@/views/admin/Student.vue'), meta: { title: '学生管理' } },
    { path: 'teacher', component: () => import('@/views/admin/Teacher.vue'), meta: { title: '教师管理' } },
    { path: 'course', component: () => import('@/views/admin/Course.vue'), meta: { title: '课程管理' } },
    { path: 'classroom', component: () => import('@/views/admin/Classroom.vue'), meta: { title: '教室管理' } },
    { path: 'teaching', component: () => import('@/views/admin/Teaching.vue'), meta: { title: '排课管理' } }
  ]
  },

  // 公开查询页面
    {
    path: '/public',
    name: 'PublicQuery',
    component: () => import('@/views/public/Query.vue')
  },
  
  { path: '/:pathMatch(.*)*', component: () => import('@/views/error/404.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth) {
    if (!userStore.isLoggedIn()) {
      next('/login')
    } else {
      if (to.meta.role && userStore.userInfo.role !== to.meta.role) {
        // 角色不匹配，跳转到对应首页
        if (userStore.isAdmin()) next('/admin')
        else if (userStore.isStudent()) next('/student')
        else if (userStore.isTeacher()) next('/teacher')
        else next('/login')
      } else {
        next()
      }
    }
  } else {
    next()
  }
})

export default router