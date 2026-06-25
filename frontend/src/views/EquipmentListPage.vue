<template>
  <div>
    <h2>设备列表</h2>
    <div class="toolbar">
      <input v-model="keyword" placeholder="搜索设备名称或编号" @input="search" class="search-input" />
      <select v-model="categoryFilter" @change="search" class="select-input">
        <option value="">全部分类</option>
        <option v-for="c in categories" :key="c" :value="c">{{ CATEGORY_LABELS[c] || c }}</option>
      </select>
    </div>
    <div class="equipment-grid">
      <div v-for="item in list" :key="item.id" class="card" @click="$router.push('/equipment/' + item.id)">
        <div class="card-title">{{ item.name }}</div>
        <div class="card-meta">{{ CATEGORY_LABELS[item.category] || item.category }} · {{ item.location }}</div>
        <div class="card-meta">可用 {{ item.availableQuantity }}/{{ item.totalQuantity }}</div>
        <span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] || item.status }}</span>
      </div>
      <div v-if="list.length === 0" class="empty">暂无设备</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const CATEGORY_LABELS = { INSTRUMENT: '仪器', CONSUMABLE: '耗材', SOFTWARE: '软件', OTHER: '其他' }
const STATUS_LABELS = { AVAILABLE: '可用', MAINTENANCE: '维护中', RETIRED: '已报废' }

const list = ref([])
const categories = ref([])
const keyword = ref('')
const categoryFilter = ref('')

let searchTimer

function search() {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(fetchList, 300)
}

async function fetchList() {
  const params = {}
  if (keyword.value) params.keyword = keyword.value
  if (categoryFilter.value) params.category = categoryFilter.value
  const res = await api.get('/equipment/all', { params })
  list.value = res.data
}

async function fetchCategories() {
  const res = await api.get('/equipment/categories')
  categories.value = res.data
}

onMounted(() => { fetchList(); fetchCategories() })
</script>

<style scoped>
h2 { margin-bottom: 16px; color: var(--md-sys-color-on-surface); }
.toolbar { display: flex; gap: 12px; margin-bottom: 20px; flex-wrap: wrap; }
.search-input {
  flex: 1; min-width: 200px; padding: 8px 12px; border: 1px solid var(--md-sys-color-outline);
  border-radius: 8px; font-size: 14px; background: var(--md-sys-color-surface);
  color: var(--md-sys-color-on-surface);
}
.select-input {
  padding: 8px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 8px;
  font-size: 14px; background: var(--md-sys-color-surface); color: var(--md-sys-color-on-surface);
}
.equipment-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 16px; }
.card {
  background: var(--md-sys-color-surface-container-low); padding: 16px; border-radius: 12px; cursor: pointer;
}
.card-title { font-size: 16px; font-weight: 500; margin-bottom: 4px; color: var(--md-sys-color-on-surface); }
.card-meta { font-size: 13px; color: var(--md-sys-color-on-surface-variant); margin-bottom: 2px; }
.status-badge { display: inline-block; font-size: 12px; padding: 2px 8px; border-radius: 4px; margin-top: 8px; }
.status-badge.AVAILABLE { background: var(--md-sys-color-primary-container); color: var(--md-sys-color-on-primary-container); }
.status-badge.MAINTENANCE { background: var(--md-sys-color-error-container); color: var(--md-sys-color-on-error-container); }
.status-badge.RETIRED { background: var(--md-sys-color-surface-variant); color: var(--md-sys-color-on-surface-variant); }
.empty { grid-column: 1 / -1; text-align: center; color: var(--md-sys-color-on-surface-variant); padding: 40px; }
</style>
