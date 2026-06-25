<template>
  <div id="app-root">
    <header v-if="authStore.isLoggedIn" class="app-bar">
      <div class="app-bar-inner">
        <div class="app-bar-start">
          <button class="icon-btn hamburger-btn" @click="showDrawer = !showDrawer">
            <span class="material-icons">menu</span>
          </button>
          <span class="material-icons app-bar-icon">science</span>
          <span class="app-bar-title">实验室设备管理系统</span>
        </div>
        <nav class="app-bar-nav">
          <router-link to="/equipment" class="nav-chip" :class="{ active: $route.path.startsWith('/equipment') }">
            <span class="material-icons">inventory_2</span> 设备列表
          </router-link>
          <router-link to="/my-borrows" class="nav-chip" :class="{ active: $route.path === '/my-borrows' }">
            <span class="material-icons">assignment</span> 我的借还
          </router-link>
          <template v-if="authStore.isAdmin">
            <router-link to="/admin/equipment" class="nav-chip" :class="{ active: $route.path === '/admin/equipment' }">
              <span class="material-icons">settings</span> 设备管理
            </router-link>
            <router-link to="/admin/approvals" class="nav-chip" :class="{ active: $route.path === '/admin/approvals' }">
              <span class="material-icons">fact_check</span> 审批管理
            </router-link>
            <router-link to="/admin/users" class="nav-chip" :class="{ active: $route.path === '/admin/users' }">
              <span class="material-icons">group</span> 用户管理
            </router-link>
          </template>
        </nav>
        <div class="app-bar-end">
          <button class="icon-btn" @click="toggleTheme" :title="isDark ? '切换亮色' : '切换暗色'">
            <span class="material-icons">{{ isDark ? 'light_mode' : 'dark_mode' }}</span>
          </button>
          <router-link to="/profile" class="user-chip">
            <span class="material-icons">account_circle</span>
            <span>{{ authStore.user?.displayName }}</span>
          </router-link>
          <button class="text-btn" @click="logout">退出</button>
        </div>
      </div>
    </header>

    <div v-if="showDrawer" class="drawer-overlay" @click="showDrawer = false"></div>
    <aside class="drawer" :class="{ open: showDrawer }">
      <div class="drawer-header">
        <span class="material-icons" style="font-size:24px;color:var(--md-sys-color-primary)">science</span>
        <span style="font-size:18px;font-weight:500">实验室设备管理系统</span>
      </div>
      <nav class="drawer-nav">
        <router-link to="/equipment" class="drawer-item" :class="{ active: $route.path.startsWith('/equipment') }" @click="showDrawer = false">
          <span class="material-icons">inventory_2</span> 设备列表
        </router-link>
        <router-link to="/my-borrows" class="drawer-item" :class="{ active: $route.path === '/my-borrows' }" @click="showDrawer = false">
          <span class="material-icons">assignment</span> 我的借还
        </router-link>
        <template v-if="authStore.isAdmin">
          <div class="drawer-divider"></div>
          <router-link to="/admin/equipment" class="drawer-item" :class="{ active: $route.path === '/admin/equipment' }" @click="showDrawer = false">
            <span class="material-icons">settings</span> 设备管理
          </router-link>
          <router-link to="/admin/approvals" class="drawer-item" :class="{ active: $route.path === '/admin/approvals' }" @click="showDrawer = false">
            <span class="material-icons">fact_check</span> 审批管理
          </router-link>
          <router-link to="/admin/users" class="drawer-item" :class="{ active: $route.path === '/admin/users' }" @click="showDrawer = false">
            <span class="material-icons">group</span> 用户管理
          </router-link>
        </template>
      </nav>
    </aside>
    <main>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useAuthStore } from './stores/auth'
import { useRouter, useRoute } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()
const isDark = ref(false)
const showDrawer = ref(false)

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
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background: var(--md-sys-color-background);
  color: var(--md-sys-color-on-background);
  -webkit-font-smoothing: antialiased;
}
a { color: inherit; text-decoration: none; }

/* App Bar (MD3 Top App Bar) */
.app-bar {
  background: var(--md-sys-color-surface-container);
  border-bottom: 1px solid var(--md-sys-color-outline-variant);
  position: sticky; top: 0; z-index: 100;
}
.app-bar-inner {
  max-width: 1200px; margin: 0 auto; height: 64px;
  display: flex; align-items: center; gap: 8px; padding: 0 16px;
}
.app-bar-start { display: flex; align-items: center; gap: 8px; margin-right: 8px; }
.app-bar-icon { font-size: 24px; color: var(--md-sys-color-primary); }
.app-bar-title { font-size: 22px; font-weight: 500; letter-spacing: -0.01em; color: var(--md-sys-color-on-surface); white-space: nowrap; }
.app-bar-nav { display: flex; gap: 4px; flex: 1; overflow-x: auto; }

/* Nav chip (MD3 assist chip) */
.nav-chip {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 0 16px; height: 36px; border-radius: 18px;
  font-size: 14px; font-weight: 500; white-space: nowrap;
  color: var(--md-sys-color-on-surface-variant);
  transition: background 0.15s, color 0.15s;
}
.nav-chip:hover { background: var(--md-sys-color-surface-container-high); }
.nav-chip.active { background: var(--md-sys-color-secondary-container); color: var(--md-sys-color-on-secondary-container); }
.nav-chip .material-icons { font-size: 18px; }

.app-bar-end { display: flex; align-items: center; gap: 4px; flex-shrink: 0; }
.icon-btn {
  width: 40px; height: 40px; border-radius: 20px; border: none;
  display: flex; align-items: center; justify-content: center;
  background: transparent; color: var(--md-sys-color-on-surface-variant);
  cursor: pointer; transition: background 0.15s;
}
.icon-btn:hover { background: var(--md-sys-color-surface-container-high); }
.icon-btn .material-icons { font-size: 20px; }
.user-chip {
  display: flex; align-items: center; gap: 4px;
  padding: 0 12px; height: 36px; border-radius: 18px;
  font-size: 14px; color: var(--md-sys-color-on-surface);
}
.user-chip .material-icons { font-size: 20px; color: var(--md-sys-color-on-surface-variant); }
.text-btn {
  height: 36px; padding: 0 12px; border-radius: 18px; border: none;
  background: transparent; color: var(--md-sys-color-primary);
  font-size: 14px; font-weight: 500; cursor: pointer;
  display: inline-flex; align-items: center; gap: 8px;
  transition: background 0.15s;
}
.text-btn:hover { background: var(--md-sys-color-primary-container); }

/* MD3 Filled Button */
.filled-btn {
  height: 40px; padding: 0 24px; border-radius: 20px; border: none;
  background: var(--md-sys-color-primary); color: var(--md-sys-color-on-primary);
  font-size: 14px; font-weight: 500; letter-spacing: 0.01em;
  cursor: pointer; transition: background 0.15s;
  display: inline-flex; align-items: center; gap: 8px;
}
.filled-btn:hover { background: color-mix(in srgb, var(--md-sys-color-primary) 90%, black); }
.filled-btn:disabled { opacity: 0.38; cursor: default; }

/* MD3 Filled Tonal Button */
.tonal-btn {
  height: 40px; padding: 0 24px; border-radius: 20px; border: none;
  background: var(--md-sys-color-secondary-container);
  color: var(--md-sys-color-on-secondary-container);
  font-size: 14px; font-weight: 500; letter-spacing: 0.01em;
  cursor: pointer; transition: background 0.15s;
  display: inline-flex; align-items: center; gap: 8px;
}
.tonal-btn:hover { background: color-mix(in srgb, var(--md-sys-color-secondary-container) 90%, black); }

/* MD3 Outlined Text Field */
.text-field { margin-bottom: 16px; }
.text-field label {
  display: block; font-size: 12px; font-weight: 500;
  color: var(--md-sys-color-on-surface-variant); margin-bottom: 4px;
  letter-spacing: 0.01em;
}
.text-field .field-wrap {
  position: relative;
}
.text-field input, .text-field select, .text-field textarea {
  width: 100%; padding: 10px 12px;
  border: 1px solid var(--md-sys-color-outline);
  border-radius: 4px; font-size: 16px; line-height: 1.5;
  background: transparent; color: var(--md-sys-color-on-surface);
  transition: border 0.15s; outline: none;
  font-family: inherit;
}
.text-field input:focus, .text-field select:focus, .text-field textarea:focus {
  border: 2px solid var(--md-sys-color-primary);
  padding: 9px 11px;
}
.text-field input::placeholder { color: var(--md-sys-color-on-surface-variant); opacity: 0.6; }

/* MD3 Outlined Select - same as text field */
.text-field select { appearance: auto; }

/* Standard table */
.data-table { width: 100%; border-collapse: collapse; font-size: 14px; }
.data-table th {
  text-align: left; padding: 12px 16px; font-weight: 500;
  color: var(--md-sys-color-on-surface-variant);
  border-bottom: 1px solid var(--md-sys-color-outline-variant);
  font-size: 12px; letter-spacing: 0.01em; text-transform: uppercase;
}
.data-table td {
  padding: 14px 16px; color: var(--md-sys-color-on-surface);
  border-bottom: 1px solid var(--md-sys-color-outline-variant);
}
.data-table tr:last-child td { border-bottom: none; }
.data-table tr:hover td { background: var(--md-sys-color-surface-container-low); }

/* Badge / status chip */
.status-badge {
  display: inline-flex; align-items: center; height: 24px;
  padding: 0 10px; border-radius: 12px; font-size: 12px; font-weight: 500;
}
.status-badge.PENDING, .status-badge.pending { background: var(--md-sys-color-tertiary-container); color: var(--md-sys-color-on-tertiary-container); }
.status-badge.APPROVED, .status-badge.approved { background: var(--md-sys-color-primary-container); color: var(--md-sys-color-on-primary-container); }
.status-badge.REJECTED, .status-badge.rejected { background: var(--md-sys-color-error-container); color: var(--md-sys-color-on-error-container); }
.status-badge.RETURNED, .status-badge.returned { background: var(--md-sys-color-surface-variant); color: var(--md-sys-color-on-surface-variant); }
.status-badge.AVAILABLE, .status-badge.available { background: var(--md-sys-color-primary-container); color: var(--md-sys-color-on-primary-container); }
.status-badge.MAINTENANCE { background: var(--md-sys-color-error-container); color: var(--md-sys-color-on-error-container); }
.status-badge.RETIRED { background: var(--md-sys-color-surface-variant); color: var(--md-sys-color-on-surface-variant); }

/* Card */
.card {
  background: var(--md-sys-color-surface-container-low);
  border-radius: 12px; padding: 16px; cursor: pointer;
  transition: background 0.15s;
  border: 1px solid var(--md-sys-color-outline-variant);
}
.card:hover { background: var(--md-sys-color-surface-container); }

/* Modal / Dialog overlay */
.modal-overlay {
  position: fixed; inset: 0;
  background: var(--md-sys-color-scrim);
  opacity: 0; visibility: hidden;
  display: flex; align-items: center; justify-content: center;
  z-index: 200; transition: opacity 0.2s, visibility 0.2s;
}
.modal-overlay.open { opacity: 0.4; visibility: visible; }
.modal-overlay.open + .modal { /* handled via sibling, but we use v-if */ }
.modal {
  position: fixed; top: 50%; left: 50%; transform: translate(-50%,-50%);
  background: var(--md-sys-color-surface-container-high);
  border-radius: 28px; padding: 28px; width: 90%; max-width: 420px;
  z-index: 201; box-shadow: 0 8px 30px rgba(0,0,0,0.12);
}
.modal h3 { font-size: 24px; font-weight: 400; margin-bottom: 24px; color: var(--md-sys-color-on-surface); }
.modal-actions { display: flex; justify-content: flex-end; gap: 8px; margin-top: 24px; }

/* Page layout */
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 28px; font-weight: 400; color: var(--md-sys-color-on-surface); }

/* Toolbar / filter bar */
.filter-bar { display: flex; gap: 12px; margin-bottom: 20px; flex-wrap: wrap; align-items: flex-end; }
.filter-bar .text-field { margin-bottom: 0; }
.filter-bar .text-field input, .filter-bar .text-field select { font-size: 14px; padding: 8px 12px; }
.filter-bar .text-field input:focus, .filter-bar .text-field select:focus { padding: 7px 11px; }
.filter-bar .filled-btn, .filter-bar .tonal-btn { height: 36px; padding: 0 20px; }

/* Small table action buttons */
.sm-btn {
  height: 32px; padding: 0 12px; border-radius: 16px; border: none;
  font-size: 13px; font-weight: 500; cursor: pointer;
  display: inline-flex; align-items: center; gap: 4px;
  transition: background 0.15s;
}
.sm-btn-primary { background: var(--md-sys-color-primary-container); color: var(--md-sys-color-on-primary-container); }
.sm-btn-primary:hover { background: color-mix(in srgb, var(--md-sys-color-primary-container) 90%, black); }
.sm-btn-error { background: var(--md-sys-color-error-container); color: var(--md-sys-color-on-error-container); }
.sm-btn-error:hover { background: color-mix(in srgb, var(--md-sys-color-error-container) 90%, black); }
.sm-btn-ghost { background: transparent; color: var(--md-sys-color-on-surface-variant); }
.sm-btn-ghost:hover { background: var(--md-sys-color-surface-container-high); }

main { max-width: 1200px; margin: 0 auto; padding: 24px 16px; }

/* Error/success messages */
.msg-error { color: var(--md-sys-color-error); font-size: 13px; margin-bottom: 8px; }
.msg-success { color: var(--md-sys-color-primary); font-size: 13px; margin-bottom: 8px; }
.msg-empty { text-align: center; padding: 48px 16px; color: var(--md-sys-color-on-surface-variant); font-size: 14px; }

/* Hamburger (hidden on wide screens) */
.hamburger-btn { display: none; }

/* Drawer overlay + panel */
.drawer-overlay {
  position: fixed; inset: 0; background: var(--md-sys-color-scrim); opacity: 0.4;
  z-index: 300; display: none;
}
.drawer {
  position: fixed; top: 0; left: 0; bottom: 0; width: 300px;
  background: var(--md-sys-color-surface-container-high);
  z-index: 301; transform: translateX(-100%);
  transition: transform 0.2s; display: none;
  flex-direction: column;
}
.drawer.open { transform: translateX(0); }
.drawer-header {
  display: flex; align-items: center; gap: 12px;
  padding: 20px 24px; border-bottom: 1px solid var(--md-sys-color-outline-variant);
}
.drawer-nav { padding: 8px 12px; flex: 1; }
.drawer-item {
  display: flex; align-items: center; gap: 12px;
  padding: 0 16px; height: 48px; border-radius: 24px;
  font-size: 14px; font-weight: 500; color: var(--md-sys-color-on-surface-variant);
  transition: background 0.15s;
}
.drawer-item:hover { background: var(--md-sys-color-surface-container-high); }
.drawer-item.active { background: var(--md-sys-color-secondary-container); color: var(--md-sys-color-on-secondary-container); }
.drawer-item .material-icons { font-size: 20px; }
.drawer-divider { height: 1px; background: var(--md-sys-color-outline-variant); margin: 8px 16px; }

/* Narrow screens */
@media (max-width: 767px) {
  .hamburger-btn { display: flex; }
  .app-bar-icon, .app-bar-title { display: none; }
  .app-bar-nav { display: none; }
  .drawer-overlay, .drawer { display: flex; }
  .app-bar-start { gap: 0; }
  .user-chip span:last-child { display: none; }
}
</style>
