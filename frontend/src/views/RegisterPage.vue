<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-icon"><span class="material-icons">person_add</span></div>
      <h2>创建账号</h2>
      <p class="auth-subtitle">注册实验室设备管理系统</p>
      <div class="text-field">
        <label>用户名</label>
        <div class="field-wrap"><input v-model="username" type="text" maxlength="100" placeholder="3-100 个字符" /></div>
      </div>
      <div class="text-field">
        <label>显示名称</label>
        <div class="field-wrap"><input v-model="displayName" type="text" maxlength="100" placeholder="最多 100 个字符" /></div>
      </div>
      <div class="text-field">
        <label>密码</label>
        <div class="field-wrap"><input v-model="password" type="password" maxlength="18" placeholder="6-18 个字符" /></div>
      </div>
      <p v-if="error" class="msg-error">{{ error }}</p>
      <p v-if="success" class="msg-success">{{ success }}</p>
      <button class="filled-btn" style="width:100%;justify-content:center;margin-top:8px" @click="register">注册</button>
      <p class="auth-link">已有账号？<router-link to="/login">登录</router-link></p>
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
