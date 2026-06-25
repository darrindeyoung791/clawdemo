<template>
  <div v-if="item">
    <button class="btn-text" @click="$router.push('/equipment')">← 返回列表</button>
    <div class="detail-card">
      <h2>{{ item.name }}</h2>
      <div class="detail-grid">
        <div><strong>设备编号：</strong>{{ item.code }}</div>
        <div><strong>分类：</strong>{{ CATEGORY_LABELS[item.category] || item.category }}</div>
        <div><strong>位置：</strong>{{ item.location }}</div>
        <div><strong>总量：</strong>{{ item.totalQuantity }}</div>
        <div><strong>可用：</strong>{{ item.availableQuantity }}</div>
        <div><strong>状态：</strong>{{ STATUS_LABELS[item.status] || item.status }}</div>
      </div>
      <button v-if="item.availableQuantity > 0 && item.status === 'AVAILABLE'" class="btn-primary" @click="showDialog = true">借用</button>
      <p v-else class="unavailable">当前设备不可借用</p>
    </div>

    <div v-if="showDialog" class="modal-overlay" @click.self="showDialog = false">
      <div class="modal">
        <h3>借用设备</h3>
        <div class="field">
          <label>数量（最多 {{ item.availableQuantity }}）</label>
          <input v-model.number="borrowQty" type="number" :max="item.availableQuantity" min="1" />
        </div>
        <div class="field">
          <label>预计归还时间</label>
          <input v-model="borrowEndTime" type="datetime-local" />
        </div>
        <p v-if="borrowError" class="error">{{ borrowError }}</p>
        <div class="modal-actions">
          <button class="btn-text" @click="showDialog = false">取消</button>
          <button class="btn-primary" @click="submitBorrow">提交申请</button>
        </div>
      </div>
    </div>
  </div>
  <div v-else class="loading">加载中...</div>
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
.btn-text { border: none; background: none; color: var(--md-sys-color-primary); cursor: pointer; padding: 8px 0; font-size: 14px; }
.detail-card {
  background: var(--md-sys-color-surface-container-low); padding: 24px; border-radius: 12px; margin-top: 12px;
}
h2 { margin-bottom: 16px; color: var(--md-sys-color-on-surface); }
.detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; margin-bottom: 20px; font-size: 14px; color: var(--md-sys-color-on-surface); }
.btn-primary {
  padding: 10px 24px; border: none; border-radius: 8px;
  background: var(--md-sys-color-primary); color: var(--md-sys-color-on-primary); font-size: 15px; cursor: pointer;
}
.unavailable { color: var(--md-sys-color-on-surface-variant); font-size: 14px; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,.3); display: flex; align-items: center; justify-content: center; z-index: 200; }
.modal {
  background: var(--md-sys-color-surface-container-high); padding: 24px; border-radius: 12px; width: 90%; max-width: 400px;
}
.modal h3 { margin-bottom: 16px; color: var(--md-sys-color-on-surface); }
.field { margin-bottom: 12px; }
.field label { display: block; font-size: 13px; margin-bottom: 4px; color: var(--md-sys-color-on-surface-variant); }
.field input {
  width: 100%; padding: 8px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 8px;
  font-size: 14px; background: var(--md-sys-color-surface); color: var(--md-sys-color-on-surface);
}
.error { color: var(--md-sys-color-error); font-size: 13px; margin-bottom: 8px; }
.modal-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 16px; }
.loading { text-align: center; padding: 40px; color: var(--md-sys-color-on-surface-variant); }
</style>
