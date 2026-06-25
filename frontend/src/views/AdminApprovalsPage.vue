<template>
  <div>
    <h2>审批管理</h2>
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
          <th>用户ID</th>
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
          <td>{{ item.userId }}</td>
          <td>{{ item.equipmentId }}</td>
          <td>{{ item.quantity }}</td>
          <td><span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] }}</span></td>
          <td>{{ formatTime(item.createdAt) }}</td>
          <td>{{ item.rejectReason || '-' }}</td>
          <td>
            <template v-if="item.status === 'PENDING'">
              <button class="btn-sm btn-approve" @click="doApprove(item.id)">通过</button>
              <button class="btn-sm btn-reject" @click="openReject(item.id)">拒绝</button>
            </template>
          </td>
        </tr>
        <tr v-if="list.length === 0"><td colspan="7" class="empty">暂无记录</td></tr>
      </tbody>
    </table>

    <div v-if="showRejectForm" class="modal-overlay" @click.self="showRejectForm = false">
      <div class="modal">
        <h3>拒绝原因</h3>
        <div class="field">
          <textarea v-model="rejectReason" rows="3" class="textarea-input" />
        </div>
        <div class="modal-actions">
          <button class="btn-text" @click="showRejectForm = false">取消</button>
          <button class="btn-primary" @click="doReject">确认拒绝</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const STATUS_LABELS = { PENDING: '待审批', APPROVED: '已批准', REJECTED: '已拒绝', RETURNED: '已归还' }

const list = ref([])
const statusFilter = ref('')
const showRejectForm = ref(false)
const rejectItemId = ref(null)
const rejectReason = ref('')

async function fetchList() {
  const params = {}
  if (statusFilter.value) params.status = statusFilter.value
  const res = await api.get('/borrows/admin', { params })
  list.value = res.data
}

function formatTime(t) {
  if (!t) return '-'
  return t.substring(0, 16).replace('T', ' ')
}

async function doApprove(id) {
  try {
    await api.put('/borrows/' + id + '/approve')
    fetchList()
  } catch (e) {
    alert(e.response?.data?.error || '操作失败')
  }
}

function openReject(id) {
  rejectItemId.value = id
  rejectReason.value = ''
  showRejectForm.value = true
}

async function doReject() {
  try {
    await api.put('/borrows/' + rejectItemId.value + '/reject', { reason: rejectReason.value })
    showRejectForm.value = false
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
.btn-sm { padding: 4px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 6px; background: none; cursor: pointer; font-size: 13px; margin-right: 4px; color: var(--md-sys-color-on-surface); }
.btn-approve { color: var(--md-sys-color-primary); border-color: var(--md-sys-color-primary); }
.btn-reject { color: var(--md-sys-color-error); border-color: var(--md-sys-color-error); }
.empty { text-align: center; color: var(--md-sys-color-on-surface-variant); padding: 24px; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,.3); display: flex; align-items: center; justify-content: center; z-index: 200; }
.modal { background: var(--md-sys-color-surface-container-high); padding: 24px; border-radius: 12px; width: 90%; max-width: 400px; }
.modal h3 { margin-bottom: 16px; color: var(--md-sys-color-on-surface); }
.field { margin-bottom: 12px; }
.textarea-input { width: 100%; padding: 8px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 8px; font-size: 14px; resize: vertical; background: var(--md-sys-color-surface); color: var(--md-sys-color-on-surface); }
.btn-primary { padding: 8px 20px; border: none; border-radius: 8px; background: var(--md-sys-color-primary); color: var(--md-sys-color-on-primary); font-size: 14px; cursor: pointer; }
.btn-text { border: none; background: none; color: var(--md-sys-color-primary); cursor: pointer; font-size: 14px; }
.modal-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 16px; }
</style>
