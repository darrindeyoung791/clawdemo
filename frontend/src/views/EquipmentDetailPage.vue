<template>
  <div v-if="item">
    <button class="text-btn" style="margin-bottom:16px" @click="$router.push('/equipment')">
      <span class="material-icons">arrow_back</span> 返回列表
    </button>
    <div class="detail-card">
      <div class="detail-top">
        <div>
          <h2>{{ item.name }}</h2>
          <p class="detail-code">编号 {{ item.code }}</p>
        </div>
        <span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] || item.status }}</span>
      </div>
      <div class="detail-grid">
        <div class="detail-item">
          <span class="material-icons">category</span>
          <div><span class="detail-label">分类</span><span>{{ CATEGORY_LABELS[item.category] || item.category }}</span></div>
        </div>
        <div class="detail-item">
          <span class="material-icons">location_on</span>
          <div><span class="detail-label">位置</span><span>{{ item.location || '-' }}</span></div>
        </div>
        <div class="detail-item">
          <span class="material-icons">inventory</span>
          <div><span class="detail-label">总数量</span><span>{{ item.totalQuantity }}</span></div>
        </div>
        <div class="detail-item">
          <span class="material-icons">check_circle</span>
          <div><span class="detail-label">可用数量</span><span>{{ item.availableQuantity }}</span></div>
        </div>
      </div>
      <button v-if="item.availableQuantity > 0 && item.status === 'AVAILABLE'" class="filled-btn" style="margin-top:8px" @click="showDialog = true">
        <span class="material-icons">add_circle</span> 借用
      </button>
      <p v-else class="msg-empty" style="text-align:left;padding:12px 0 0">当前设备不可借用</p>
    </div>

    <div v-if="showDialog" class="modal-overlay open" @click.self="showDialog = false"></div>
    <div v-if="showDialog" class="modal">
      <h3>借用设备</h3>
      <div class="text-field">
        <label>借用数量（最多 {{ item.availableQuantity }}）</label>
        <div class="field-wrap"><input v-model.number="borrowQty" type="number" :max="item.availableQuantity" min="1" /></div>
      </div>
      <div class="text-field">
        <label>预计归还时间</label>
        <div class="field-wrap"><input v-model="borrowEndTime" type="datetime-local" /></div>
      </div>
      <p v-if="borrowError" class="msg-error">{{ borrowError }}</p>
      <div class="modal-actions">
        <button class="text-btn" @click="showDialog = false">取消</button>
        <button class="filled-btn" @click="submitBorrow">提交申请</button>
      </div>
    </div>
  </div>
  <div v-else class="msg-empty">加载中...</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api'

const CATEGORY_LABELS = { INSTRUMENT: '仪器', CONSUMABLE: '耗材', SOFTWARE: '软件', OTHER: '其他' }
const STATUS_LABELS = { AVAILABLE: '可用', MAINTENANCE: '维护中', RETIRED: '已报废' }

const route = useRoute()
const item = ref(null)
const showDialog = ref(false)
const borrowQty = ref(1)
const borrowEndTime = ref('')
const borrowError = ref('')

onMounted(async () => {
  const res = await api.get('/equipment/' + route.params.id)
  item.value = res.data
})

async function submitBorrow() {
  try {
    borrowError.value = ''
    await api.post('/borrows', { equipmentId: item.value.id, quantity: borrowQty.value, endTime: borrowEndTime.value || null })
    showDialog.value = false
    alert('借用申请已提交，等待管理员审批')
  } catch (e) {
    borrowError.value = e.response?.data?.error || '提交失败'
  }
}
</script>

<style scoped>
.detail-card {
  background: var(--md-sys-color-surface-container-low);
  border: 1px solid var(--md-sys-color-outline-variant);
  border-radius: 16px; padding: 24px;
}
.detail-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
.detail-top h2 { font-size: 28px; font-weight: 400; color: var(--md-sys-color-on-surface); }
.detail-code { font-size: 14px; color: var(--md-sys-color-on-surface-variant); margin-top: 4px; }
.detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.detail-item { display: flex; align-items: center; gap: 12px; }
.detail-item .material-icons { font-size: 20px; color: var(--md-sys-color-primary); }
.detail-item div { display: flex; flex-direction: column; }
.detail-label { font-size: 12px; color: var(--md-sys-color-on-surface-variant); }
.detail-item span:last-child { font-size: 14px; color: var(--md-sys-color-on-surface); }
</style>
