<template>
  <div>
    <div class="page-header"><h2>用户管理</h2></div>
    <div style="background:var(--md-sys-color-surface-container-low);border-radius:16px;border:1px solid var(--md-sys-color-outline-variant);overflow:hidden">
      <table class="data-table">
        <thead>
          <tr><th>ID</th><th>用户名</th><th>显示名称</th><th>密码哈希</th><th>角色</th><th></th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td style="font-size:13px;font-family:monospace">{{ item.id.substring(0,8) }}...</td>
            <td>{{ item.username }}</td>
            <td>{{ item.displayName }}</td>
            <td style="font-size:12px;font-family:monospace;color:var(--md-sys-color-on-surface-variant);max-width:160px;overflow:hidden;text-overflow:ellipsis">{{ item.password.substring(0,24) }}...</td>
            <td><span class="status-badge" :class="item.role === 'ROLE_ADMIN' ? 'APPROVED' : ''">{{ item.role === 'ROLE_ADMIN' ? '管理员' : '用户' }}</span></td>
            <td>
              <button v-if="item.role !== 'ROLE_ADMIN'" class="sm-btn sm-btn-ghost" @click="doDelete(item.id)">
                <span class="material-icons" style="font-size:16px">delete</span>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="errorMsg" class="msg-error">错误：{{ errorMsg }}</p>
      <p v-else-if="list.length === 0" class="msg-empty">暂无用户</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const list = ref([])
const errorMsg = ref('')

async function fetchList() {
  try {
    errorMsg.value = ''
    const res = await api.get('/users')
    list.value = res.data || []
  } catch (e) {
    errorMsg.value = e.response?.data?.error || e.message || '请求失败'
    list.value = []
  }
}

async function doDelete(id) {
  if (!confirm('确定删除此用户？')) return
  try {
    await api.delete('/users/' + id)
    fetchList()
  } catch (e) {
    alert(e.response?.data?.error || '删除失败')
  }
}

onMounted(fetchList)
</script>
