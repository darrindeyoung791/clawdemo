<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2>注册</h2>
      <div class="field">
        <label>用户名</label>
        <input v-model="username" type="text" />
      </div>
      <div class="field">
        <label>显示名称</label>
        <input v-model="displayName" type="text" />
      </div>
      <div class="field">
        <label>密码</label>
        <input v-model="password" type="password" />
      </div>
      <p v-if="error" class="error">{{ error }}</p>
      <p v-if="success" class="success">{{ success }}</p>
      <button class="btn-primary" @click="register">注册</button>
      <p class="link">已有账号？<router-link to="/login">登录</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '../api'

const username = ref('')
const displayName = ref('')
const password = ref('')
const error = ref('')
const success = ref('')

async function register() {
  try {
    error.value = ''
    success.value = ''
    await api.post('/auth/register', { username: username.value, password: password.value, displayName: displayName.value })
    success.value = '注册成功，请登录'
  } catch (e) {
    error.value = e.response?.data?.error || '注册失败'
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
.success { color: var(--md-sys-color-primary); font-size: 13px; margin-bottom: 8px; }
.link { margin-top: 16px; font-size: 13px; text-align: center; color: var(--md-sys-color-on-surface-variant); }
.link a { color: var(--md-sys-color-primary); }
</style>
