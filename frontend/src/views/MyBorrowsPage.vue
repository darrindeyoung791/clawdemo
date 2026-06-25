<template>
  <div>
    <h2>我的借还</h2>
    <div class="toolbar">
      <select v-model="statusFilter" @change="fetchList" class="select-input">
        <option value="">全部状态</option>
        <option value="PENDING">待审批</option>
        <option value="APPROVED">已批准</option>
        <option value="REJECTED">已拒绝</option>
        <option value="RETURNED">已归还</option>
      </select>
    </div>
    <table class="table">
      <thead>
        <tr>
          <th>设备ID</th>
          <th>数量</th>
          <th>状态</th>
          <th>申请时间</th>
          <th>拒绝原因</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in list" :key="item.id">
          <td>{{ item.equipmentId }}</td>
          <td>{{ item.quantity }}</td>
          <td><span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] }}</span></td>
          <td>{{ formatTime(item.createdAt) }}</td>
          <td>{{ item.rejectReason || '-' }}</td>
          <td>
            <button v-if="item.status === 'APPROVED'" class="btn-sm" @click="doReturn(item.id)">归还</button>
          </td>
        </tr>
        <tr v-if="list.length === 0"><td colspan="6" class="empty">暂无记录</td></tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const STATUS_LABELS = { PENDING: '待审批', APPROVED: '已批准', REJECTED: '已拒绝', RETURNED: '已归还' }

const list = ref([])
const statusFilter = ref('')

async function fetchList() {
  const params = { page: 1, size: 100 }
  if (statusFilter.value) params.status = statusFilter.value
  const res = await api.get('/borrows', { params })
  list.value = res.data || []
}

function formatTime(t) {
  if (!t) return '-'
  return t.substring(0, 16).replace('T', ' ')
}

async function doReturn(id) {
  if (!confirm('确认归还？')) return
  try {
    await api.put('/borrows/' + id + '/return')
    fetchList()
  } catch (e) {
    alert(e.response?.data?.error || '操作失败')
  }
}

onMounted(fetchList)
</script>

<style scoped>
h2 { margin-bottom: 16px; color: var(--md-sys-color-on-surface); }
.toolbar { margin-bottom: 16px; }
.select-input {
  padding: 8px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 8px;
  font-size: 14px; background: var(--md-sys-color-surface); color: var(--md-sys-color-on-surface);
}
.table { width: 100%; border-collapse: collapse; font-size: 14px; }
.table th, .table td { text-align: left; padding: 10px 12px; border-bottom: 1px solid var(--md-sys-color-outline-variant); }
.table th { color: var(--md-sys-color-on-surface-variant); font-weight: 500; }
.table td { color: var(--md-sys-color-on-surface); }
.status-badge { display: inline-block; font-size: 12px; padding: 2px 8px; border-radius: 4px; }
.status-badge.PENDING { background: var(--md-sys-color-primary-container); color: var(--md-sys-color-on-primary-container); }
.status-badge.APPROVED { background: var(--md-sys-color-primary-container); color: var(--md-sys-color-on-primary-container); }
.status-badge.REJECTED { background: var(--md-sys-color-error-container); color: var(--md-sys-color-on-error-container); }
.status-badge.RETURNED { background: var(--md-sys-color-surface-variant); color: var(--md-sys-color-on-surface-variant); }
.btn-sm { padding: 4px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 6px; background: none; cursor: pointer; font-size: 13px; color: var(--md-sys-color-on-surface); }
.empty { text-align: center; color: var(--md-sys-color-on-surface-variant); padding: 24px; }
</style>
