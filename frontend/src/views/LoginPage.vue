<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2>登录</h2>
      <div class="field">
        <label>用户名</label>
        <input v-model="username" type="text" />
      </div>
      <div class="field">
        <label>密码</label>
        <input v-model="password" type="password" @keyup.enter="login" />
      </div>
      <p v-if="error" class="error">{{ error }}</p>
      <button class="btn-primary" @click="login">登录</button>
      <p class="link">没有账号？<router-link to="/register">注册</router-link></p>
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
.auth-page { display: flex; justify-content: center; align-items: center; min-height: 60vh; }
.auth-card {
  background: var(--md-sys-color-surface-container-low);
  padding: 32px; border-radius: 12px; width: 100%; max-width: 380px;
}
h2 { margin-bottom: 24px; color: var(--md-sys-color-on-surface); }
.field { margin-bottom: 16px; }
.field label { display: block; font-size: 14px; margin-bottom: 4px; color: var(--md-sys-color-on-surface-variant); }
.field input {
  width: 100%; padding: 10px 12px; border: 1px solid var(--md-sys-color-outline);
  border-radius: 8px; font-size: 14px; background: var(--md-sys-color-surface);
  color: var(--md-sys-color-on-surface);
}
.btn-primary {
  width: 100%; padding: 10px; border: none; border-radius: 8px;
  background: var(--md-sys-color-primary); color: var(--md-sys-color-on-primary);
  font-size: 15px; cursor: pointer; margin-top: 8px;
}
.error { color: var(--md-sys-color-error); font-size: 13px; margin-bottom: 8px; }
.link { margin-top: 16px; font-size: 13px; text-align: center; color: var(--md-sys-color-on-surface-variant); }
.link a { color: var(--md-sys-color-primary); }
</style>
