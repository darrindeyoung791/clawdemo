import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', redirect: '/equipment' },
  { path: '/login', component: () => import('../views/LoginPage.vue'), meta: { guest: true } },
  { path: '/register', component: () => import('../views/RegisterPage.vue'), meta: { guest: true } },
  { path: '/equipment', component: () => import('../views/EquipmentListPage.vue'), meta: { auth: true } },
  { path: '/equipment/:id', component: () => import('../views/EquipmentDetailPage.vue'), meta: { auth: true } },
  { path: '/my-borrows', component: () => import('../views/MyBorrowsPage.vue'), meta: { auth: true } },
  { path: '/profile', component: () => import('../views/ProfilePage.vue'), meta: { auth: true } },
  { path: '/admin/equipment', component: () => import('../views/AdminEquipmentPage.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/approvals', component: () => import('../views/AdminApprovalsPage.vue'), meta: { auth: true, admin: true } },
  { path: '/admin/users', component: () => import('../views/AdminUsersPage.vue'), meta: { auth: true, admin: true } },
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.auth && !authStore.isLoggedIn) {
    next('/login')
  } else if (to.meta.guest && authStore.isLoggedIn) {
    next('/')
  } else if (to.meta.admin && !authStore.isAdmin) {
    next('/')
  } else {
    next()
  }
})

export default router
