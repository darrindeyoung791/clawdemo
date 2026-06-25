<template>
  <div>
    <div class="page-header"><h2>我的借还</h2></div>
    <div class="filter-bar">
      <div class="text-field" style="min-width:160px">
        <label>状态</label>
        <div class="field-wrap">
          <select v-model="statusFilter" @change="fetchList">
            <option value="">全部状态</option>
            <option value="PENDING">待审批</option>
            <option value="APPROVED">已批准</option>
            <option value="REJECTED">已拒绝</option>
            <option value="RETURNED">已归还</option>
          </select>
        </div>
      </div>
    </div>
    <div style="background:var(--md-sys-color-surface-container-low);border-radius:16px;border:1px solid var(--md-sys-color-outline-variant);overflow:hidden">
      <table class="data-table">
        <thead>
          <tr><th>设备 ID</th><th>数量</th><th>状态</th><th>申请时间</th><th>拒绝原因</th><th></th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td>{{ item.equipmentId }}</td>
            <td>{{ item.quantity }}</td>
            <td><span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] }}</span></td>
            <td style="color:var(--md-sys-color-on-surface-variant);font-size:13px">{{ formatTime(item.createdAt) }}</td>
            <td style="font-size:13px;color:var(--md-sys-color-on-surface-variant)">{{ item.rejectReason || '-' }}</td>
            <td>
              <button v-if="item.status === 'APPROVED'" class="sm-btn sm-btn-primary" @click="doReturn(item.id)">
                <span class="material-icons" style="font-size:16px">undo</span> 归还
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0" class="msg-empty">暂无借还记录</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const STATUS_LABELS = { PENDING: '待审批', APPROVED: '已批准', REJECTED: '已拒绝', RETURNED: '已归还' }

const list = ref([])
const statusFilter = ref('')

async function fetchList() {
  const params = {}
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
