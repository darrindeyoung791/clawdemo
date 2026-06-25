<template>
  <div class="profile-page">
    <div class="profile-card">
      <div class="profile-icon"><span class="material-icons">account_circle</span></div>
      <h2>{{ authStore.user?.displayName }}</h2>
      <p class="profile-subtitle">@{{ authStore.user?.username }}</p>
      <div class="profile-section">
        <h3>修改密码</h3>
        <div class="text-field">
          <label>当前密码</label>
          <div class="field-wrap"><input v-model="oldPassword" type="password" placeholder="输入当前密码" /></div>
        </div>
        <div class="text-field">
          <label>新密码</label>
          <div class="field-wrap"><input v-model="newPassword" type="password" placeholder="6-18 个字符" /></div>
        </div>
        <div class="text-field">
          <label>确认新密码</label>
          <div class="field-wrap"><input v-model="confirmPassword" type="password" placeholder="再次输入新密码" /></div>
        </div>
        <p v-if="error" class="msg-error">{{ error }}</p>
        <p v-if="success" class="msg-success">{{ success }}</p>
        <button class="filled-btn" style="width:100%;justify-content:center;margin-top:8px" @click="changePassword">保存密码</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const authStore = useAuthStore()
const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const error = ref('')
const success = ref('')

async function changePassword() {
  error.value = ''
  success.value = ''
  if (!oldPassword.value || !newPassword.value || !confirmPassword.value) {
    error.value = '请填写所有字段'
    return
  }
  if (newPassword.value !== confirmPassword.value) {
    error.value = '两次新密码不一致'
    return
  }
  if (newPassword.value.length < 6 || newPassword.value.length > 18) {
    error.value = '新密码必须 6-18 个字符'
    return
  }
  try {
    await api.put('/auth/password', { oldPassword: oldPassword.value, newPassword: newPassword.value })
    success.value = '密码修改成功'
    oldPassword.value = ''
    newPassword.value = ''
    confirmPassword.value = ''
  } catch (e) {
    error.value = e.response?.data?.error || '修改失败'
  }
}
</script>

<style scoped>
.profile-page { display: flex; justify-content: center; padding-top: 32px; }
.profile-card {
  background: var(--md-sys-color-surface-container-low);
  border: 1px solid var(--md-sys-color-outline-variant);
  border-radius: 28px; padding: 40px 32px 32px;
  width: 100%; max-width: 400px;
}
.profile-icon { text-align: center; margin-bottom: 12px; }
.profile-icon .material-icons { font-size: 64px; color: var(--md-sys-color-primary); }
h2 { text-align: center; font-size: 28px; font-weight: 400; margin-bottom: 2px; color: var(--md-sys-color-on-surface); }
.profile-subtitle { text-align: center; font-size: 14px; color: var(--md-sys-color-on-surface-variant); margin-bottom: 32px; }
.profile-section h3 { font-size: 18px; font-weight: 500; margin-bottom: 16px; color: var(--md-sys-color-on-surface); }
</style>
