<template>
  <div id="app-root">
    <header v-if="authStore.isLoggedIn">
      <div class="header-inner">
        <span class="header-title">实验室设备管理系统</span>
        <nav>
          <router-link to="/equipment">设备列表</router-link>
          <router-link to="/my-borrows">我的借还</router-link>
          <router-link v-if="authStore.isAdmin" to="/admin/equipment">设备管理</router-link>
          <router-link v-if="authStore.isAdmin" to="/admin/approvals">审批管理</router-link>
        </nav>
        <div class="header-right">
          <span class="material-icons" style="cursor:pointer" @click="toggleTheme">
            {{ isDark ? 'light_mode' : 'dark_mode' }}
          </span>
          <span>{{ authStore.user?.displayName }}</span>
          <button class="btn-text" @click="logout">退出</button>
        </div>
      </div>
    </header>
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from './stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const isDark = ref(false)

function toggleTheme() {
  isDark.value = !isDark.value
  document.body.className = isDark.value ? 'dark' : 'light'
}

function logout() {
  authStore.logout()
  router.push('/login')
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; }
header {
  background: var(--md-sys-color-primary-container);
  padding: 0 24px;
  position: sticky; top: 0; z-index: 100;
}
.header-inner {
  max-width: 1200px; margin: 0 auto;
  display: flex; align-items: center; gap: 24px; height: 56px;
}
.header-title { font-size: 18px; font-weight: 500; color: var(--md-sys-color-on-primary-container); }
nav { display: flex; gap: 16px; flex: 1; }
nav a { color: var(--md-sys-color-on-primary-container); text-decoration: none; font-size: 14px; }
nav a:hover { text-decoration: underline; }
.header-right { display: flex; align-items: center; gap: 12px; color: var(--md-sys-color-on-primary-container); }
.btn-text {
  border: none; background: none; color: var(--md-sys-color-primary);
  cursor: pointer; font-size: 14px; padding: 4px 8px;
}
.btn-text:hover { background: var(--md-sys-color-primary-container); border-radius: 4px; }
main { max-width: 1200px; margin: 0 auto; padding: 24px; }
</style>
