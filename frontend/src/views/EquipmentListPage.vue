<template>
  <div>
    <div class="page-header"><h2>设备列表</h2></div>
    <div class="filter-bar">
      <div class="text-field" style="flex:1;min-width:200px">
        <label>搜索</label>
        <div class="field-wrap">
          <input v-model="keyword" placeholder="设备名称或编号" @input="search" />
        </div>
      </div>
      <div class="text-field" style="min-width:140px">
        <label>分类</label>
        <div class="field-wrap">
          <select v-model="categoryFilter" @change="fetchList">
            <option value="">全部分类</option>
            <option v-for="c in categories" :key="c" :value="c">{{ CATEGORY_LABELS[c] }}</option>
          </select>
        </div>
      </div>
    </div>
    <div class="equipment-grid">
      <div v-for="item in list" :key="item.id" class="card" @click="$router.push('/equipment/' + item.id)">
        <div class="card-name">{{ item.name }}</div>
        <div class="card-detail-row">
          <span class="material-icons">category</span>
          <span>{{ CATEGORY_LABELS[item.category] || item.category }}</span>
        </div>
        <div class="card-detail-row">
          <span class="material-icons">location_on</span>
          <span>{{ item.location || '-' }}</span>
        </div>
        <div class="card-footer">
          <span class="card-qty">可用 {{ item.availableQuantity }}/{{ item.totalQuantity }}</span>
          <span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] || item.status }}</span>
        </div>
      </div>
      <div v-if="list.length === 0" class="msg-empty">暂无设备</div>
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
function search() { clearTimeout(searchTimer); searchTimer = setTimeout(fetchList, 300) }

async function fetchList() {
  const params = {}
  if (keyword.value) params.keyword = keyword.value
  if (categoryFilter.value) params.category = categoryFilter.value
  const res = await api.get('/equipment', { params })
  list.value = res.data
}

async function fetchCategories() {
  const res = await api.get('/equipment/categories')
  categories.value = res.data
}

onMounted(() => { fetchList(); fetchCategories() })
</script>

<style scoped>
.equipment-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 16px; }
.card-name { font-size: 18px; font-weight: 500; margin-bottom: 12px; color: var(--md-sys-color-on-surface); }
.card-detail-row { display: flex; align-items: center; gap: 6px; font-size: 14px; color: var(--md-sys-color-on-surface-variant); margin-bottom: 6px; }
.card-detail-row .material-icons { font-size: 16px; }
.card-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; padding-top: 12px; border-top: 1px solid var(--md-sys-color-outline-variant); }
.card-qty { font-size: 13px; color: var(--md-sys-color-on-surface-variant); }
</style>
