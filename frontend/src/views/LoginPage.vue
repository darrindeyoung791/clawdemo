<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-icon"><span class="material-icons">science</span></div>
      <h2>欢迎回来</h2>
      <p class="auth-subtitle">登录实验室设备管理系统</p>
      <div class="text-field">
        <label>用户名</label>
        <div class="field-wrap"><input v-model="username" type="text" placeholder="输入用户名" @keyup.enter="login" /></div>
      </div>
      <div class="text-field">
        <label>密码</label>
        <div class="field-wrap"><input v-model="password" type="password" placeholder="输入密码" @keyup.enter="login" /></div>
      </div>
      <p v-if="error" class="msg-error">{{ error }}</p>
      <button class="filled-btn" style="width:100%;justify-content:center;margin-top:8px" @click="login">登录</button>
      <p class="auth-link">还没有账号？<router-link to="/register">注册</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const router = useRouter()
const authStore = useAuthStore()
const username = ref('')
const password = ref('')
const error = ref('')

async function login() {
  try {
    error.value = ''
    const res = await api.post('/auth/login', { username: username.value, password: password.value })
    authStore.setAuth(res.data.token, res.data)
    router.push('/')
  } catch (e) {
    error.value = e.response?.data?.error || '登录失败'
  }
}
</script>

<style scoped>
.auth-page { display: flex; justify-content: center; align-items: center; min-height: 80vh; }
.auth-card {
  background: var(--md-sys-color-surface-container-low);
  border: 1px solid var(--md-sys-color-outline-variant);
  border-radius: 28px; padding: 40px 32px 32px;
  width: 100%; max-width: 380px;
}
.auth-icon { text-align: center; margin-bottom: 12px; }
.auth-icon .material-icons { font-size: 48px; color: var(--md-sys-color-primary); }
h2 { text-align: center; font-size: 28px; font-weight: 400; margin-bottom: 4px; color: var(--md-sys-color-on-surface); }
.auth-subtitle { text-align: center; font-size: 14px; color: var(--md-sys-color-on-surface-variant); margin-bottom: 28px; }
.auth-link { margin-top: 20px; font-size: 14px; text-align: center; color: var(--md-sys-color-on-surface-variant); }
.auth-link a { color: var(--md-sys-color-primary); font-weight: 500; }
</style>
