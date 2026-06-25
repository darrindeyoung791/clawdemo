<template>
  <div v-if="item">
    <button class="text-btn" style="margin-bottom:16px" @click="router.push(backPath)">
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
      <div class="detail-actions">
        <button v-if="item.availableQuantity > 0 && item.status === 'AVAILABLE'" class="filled-btn" @click="showDialog = true">
          <span class="material-icons">add_circle</span> 借用
        </button>
        <p v-else class="msg-empty" style="text-align:left;padding:0">当前设备不可借用</p>
        <div v-if="isAdmin" class="admin-actions">
          <button class="tonal-btn" @click="openEdit"><span class="material-icons">edit</span> 编辑</button>
          <button class="text-btn" style="color:var(--md-sys-color-error)" @click="doDelete"><span class="material-icons">delete</span> 删除</button>
        </div>
      </div>
    </div>

    <div v-if="showDialog" class="modal-overlay open" @click.self="showDialog = false"></div>
    <div v-if="showDialog" class="modal">
      <h3>借用设备</h3>
      <div class="text-field">
        <label>借用数量（最多 {{ item.availableQuantity }}）</label>
        <div class="field-wrap"><input v-model.number="borrowQty" type="number" :max="item.availableQuantity" min="1" /></div>
      </div>
      <div class="form-section">
        <label>预计归还时间</label>
        <div class="filter-chip-group">
          <button v-for="opt in durationOptions" :key="opt.value" class="filter-chip" :class="{ active: selectedDuration === opt.value }" @click="selectDuration(opt.value)">{{ opt.label }}</button>
        </div>
        <div v-if="selectedDuration === 'custom'" class="text-field" style="margin-top:8px">
          <div class="field-wrap"><input v-model="customDate" type="date" :min="todayStr" /></div>
        </div>
      </div>
      <p v-if="borrowError" class="msg-error">{{ borrowError }}</p>
      <div class="modal-actions">
        <button class="text-btn" @click="showDialog = false">取消</button>
        <button class="filled-btn" @click="submitBorrow">提交申请</button>
      </div>
    </div>

    <div v-if="showEditForm" class="modal-overlay open" @click.self="showEditForm = false"></div>
    <div v-if="showEditForm" class="modal">
      <h3>编辑设备</h3>
      <div class="text-field">
        <label>名称</label>
        <div class="field-wrap"><input v-model="editForm.name" /></div>
      </div>
      <div class="text-field">
        <label>分类</label>
        <div class="field-wrap">
          <select v-model="editForm.category">
            <option v-for="c in Object.keys(CATEGORY_LABELS)" :key="c" :value="c">{{ CATEGORY_LABELS[c] }}</option>
          </select>
        </div>
      </div>
      <div class="text-field">
        <label>位置</label>
        <div class="field-wrap"><input v-model="editForm.location" /></div>
      </div>
      <div class="text-field">
        <label>总量</label>
        <div class="field-wrap"><input v-model.number="editForm.totalQuantity" type="number" min="1" /></div>
      </div>
      <p v-if="editError" class="msg-error">{{ editError }}</p>
      <div class="modal-actions">
        <button class="text-btn" @click="showEditForm = false">取消</button>
        <button class="filled-btn" @click="saveEdit">保存</button>
      </div>
    </div>
  </div>
  <div v-else class="msg-empty">加载中...</div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import api from '../api'

const CATEGORY_LABELS = { INSTRUMENT: '仪器', CONSUMABLE: '耗材', SOFTWARE: '软件', OTHER: '其他' }
const STATUS_LABELS = { AVAILABLE: '可用', MAINTENANCE: '维护中', RETIRED: '已报废' }

const durationOptions = [
  { label: '1 天', value: 1 },
  { label: '2 天', value: 2 },
  { label: '3 天', value: 3 },
  { label: '5 天', value: 5 },
  { label: '7 天', value: 7 },
  { label: '自定义', value: 'custom' },
]

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const item = ref(null)
const showDialog = ref(false)
const showEditForm = ref(false)
const editForm = ref({ name: '', category: 'INSTRUMENT', location: '', totalQuantity: 1 })
const borrowQty = ref(1)
const selectedDuration = ref(3)
const customDate = ref('')
const borrowError = ref('')
const editError = ref('')

const isAdmin = computed(() => authStore.isAdmin)
const backPath = computed(() => isAdmin.value ? '/admin/equipment' : '/equipment')

const todayStr = computed(() => {
  const d = new Date()
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
})

onMounted(async () => {
  const res = await api.get('/equipment/' + route.params.id)
  item.value = res.data
})

function openEdit() {
  editError.value = ''
  editForm.value = { ...item.value }
  showEditForm.value = true
}

async function saveEdit() {
  try {
    editError.value = ''
    await api.put('/equipment/' + editForm.value.id, editForm.value)
    showEditForm.value = false
    const res = await api.get('/equipment/' + route.params.id)
    item.value = res.data
  } catch (e) {
    editError.value = e.response?.data?.error || '保存失败'
  }
}

async function doDelete() {
  if (!confirm('确定删除此设备？')) return
  try {
    await api.delete('/equipment/' + item.value.id)
    router.push(backPath.value)
  } catch (e) {
    alert(e.response?.data?.error || '删除失败')
  }
}

function selectDuration(value) {
  selectedDuration.value = value
  if (value !== 'custom') {
    customDate.value = ''
  }
}

function getEndTime() {
  if (selectedDuration.value === 'custom') {
    if (!customDate.value) return null
    return customDate.value + 'T00:00:00'
  }
  const d = new Date()
  d.setDate(d.getDate() + selectedDuration.value)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}T00:00:00`
}

async function submitBorrow() {
  try {
    borrowError.value = ''
    const endTime = getEndTime()
    await api.post('/borrows', { equipmentId: item.value.id, quantity: borrowQty.value, endTime })
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

.detail-actions { display: flex; align-items: center; gap: 12px; margin-top: 16px; padding-top: 16px; border-top: 1px solid var(--md-sys-color-outline-variant); flex-wrap: wrap; }
.admin-actions { display: flex; align-items: center; gap: 8px; margin-left: auto; }

.form-section { margin-bottom: 16px; }
.form-section > label { display: block; font-size: 12px; color: var(--md-sys-color-on-surface-variant); margin-bottom: 8px; }
.filter-chip-group { display: flex; gap: 6px; flex-wrap: wrap; }
.filter-chip {
  height: 32px; padding: 0 14px; border-radius: 16px; border: 1px solid var(--md-sys-color-outline);
  background: transparent; color: var(--md-sys-color-on-surface-variant);
  font-size: 13px; font-weight: 500; cursor: pointer;
  transition: background 0.15s, border-color 0.15s, color 0.15s;
}
.filter-chip:hover { background: var(--md-sys-color-surface-container-high); }
.filter-chip.active {
  background: var(--md-sys-color-secondary-container);
  color: var(--md-sys-color-on-secondary-container);
  border-color: transparent;
}
</style>
