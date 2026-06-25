<template>
  <div>
    <div class="page-header"><h2>审批管理</h2></div>
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
          <tr><th>用户 ID</th><th>设备 ID</th><th>数量</th><th>状态</th><th>申请时间</th><th>拒绝原因</th><th></th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td style="font-size:13px">{{ item.userId.substring(0,8) }}...</td>
            <td>{{ item.equipmentId }}</td>
            <td>{{ item.quantity }}</td>
            <td><span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] }}</span></td>
            <td style="color:var(--md-sys-color-on-surface-variant);font-size:13px">{{ formatTime(item.createdAt) }}</td>
            <td style="font-size:13px;color:var(--md-sys-color-on-surface-variant)">{{ item.rejectReason || '-' }}</td>
            <td>
              <div v-if="item.status === 'PENDING'" style="display:flex;gap:4px">
                <button class="sm-btn sm-btn-primary" @click="doApprove(item.id)">
                  <span class="material-icons" style="font-size:16px">check</span> 通过
                </button>
                <button class="sm-btn sm-btn-error" @click="openReject(item.id)">
                  <span class="material-icons" style="font-size:16px">close</span> 拒绝
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0" class="msg-empty">暂无记录</p>
    </div>

    <div v-if="showRejectForm" class="modal-overlay open" @click.self="showRejectForm = false"></div>
    <div v-if="showRejectForm" class="modal">
      <h3>拒绝原因</h3>
      <div class="text-field">
        <label>原因</label>
        <div class="field-wrap">
          <textarea v-model="rejectReason" rows="3" style="resize:vertical" placeholder="请输入拒绝原因" />
        </div>
      </div>
      <div class="modal-actions">
        <button class="text-btn" @click="showRejectForm = false">取消</button>
        <button class="filled-btn" @click="doReject">确认拒绝</button>
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
