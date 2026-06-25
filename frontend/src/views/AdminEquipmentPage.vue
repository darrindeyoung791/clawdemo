<template>
  <div>
    <div class="header-row">
      <h2>设备管理</h2>
      <button class="btn-primary" @click="openAdd">添加设备</button>
    </div>
    <div class="toolbar">
      <input v-model="keyword" placeholder="搜索设备" @input="search" class="search-input" />
    </div>
    <table class="table">
      <thead>
        <tr>
          <th>名称</th>
          <th>编号</th>
          <th>分类</th>
          <th>位置</th>
          <th>总量</th>
          <th>可用</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in list" :key="item.id">
          <td>{{ item.name }}</td>
          <td>{{ item.code }}</td>
          <td>{{ CATEGORY_LABELS[item.category] }}</td>
          <td>{{ item.location }}</td>
          <td>{{ item.totalQuantity }}</td>
          <td>{{ item.availableQuantity }}</td>
          <td>{{ STATUS_LABELS[item.status] }}</td>
          <td>
            <button class="btn-sm" @click="openEdit(item)">编辑</button>
            <button class="btn-sm btn-danger" @click="doDelete(item.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="showForm" class="modal-overlay" @click.self="showForm = false">
      <div class="modal">
        <h3>{{ editing ? '编辑设备' : '添加设备' }}</h3>
        <div class="field"><label>名称</label><input v-model="form.name" /></div>
        <div class="field">
          <label>分类</label>
          <select v-model="form.category" class="select-input">
            <option v-for="c in categories" :key="c" :value="c">{{ CATEGORY_LABELS[c] }}</option>
          </select>
        </div>
        <div class="field"><label>位置</label><input v-model="form.location" /></div>
        <div class="field"><label>总量</label><input v-model.number="form.totalQuantity" type="number" min="1" /></div>
        <div class="modal-actions">
          <button class="btn-text" @click="showForm = false">取消</button>
          <button class="btn-primary" @click="save">保存</button>
        </div>
      </div>
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
const showForm = ref(false)
const editing = ref(false)
const form = ref({ name: '', category: 'INSTRUMENT', location: '', totalQuantity: 1 })

let searchTimer
function search() { clearTimeout(searchTimer); searchTimer = setTimeout(fetchList, 300) }

async function fetchList() {
  const params = {}
  if (keyword.value) params.keyword = keyword.value
  const res = await api.get('/equipment/all', { params })
  list.value = res.data
}

async function fetchCategories() {
  const res = await api.get('/equipment/categories')
  categories.value = res.data
}

function openAdd() {
  editing.value = false
  form.value = { name: '', category: 'INSTRUMENT', location: '', totalQuantity: 1 }
  showForm.value = true
}

function openEdit(item) {
  editing.value = true
  form.value = { ...item }
  showForm.value = true
}

async function save() {
  if (editing.value) {
    await api.put('/equipment/' + form.value.id, form.value)
  } else {
    await api.post('/equipment', form.value)
  }
  showForm.value = false
  fetchList()
}

async function doDelete(id) {
  if (!confirm('确定删除？')) return
  try {
    await api.delete('/equipment/' + id)
    fetchList()
  } catch (e) {
    alert(e.response?.data?.error || '删除失败')
  }
}

onMounted(() => { fetchList(); fetchCategories() })
</script>

<style scoped>
.header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
h2 { color: var(--md-sys-color-on-surface); }
.toolbar { margin-bottom: 16px; }
.search-input {
  padding: 8px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 8px;
  font-size: 14px; width: 300px; background: var(--md-sys-color-surface); color: var(--md-sys-color-on-surface);
}
.table { width: 100%; border-collapse: collapse; font-size: 14px; }
.table th, .table td { text-align: left; padding: 10px 12px; border-bottom: 1px solid var(--md-sys-color-outline-variant); }
.table th { color: var(--md-sys-color-on-surface-variant); font-weight: 500; }
.table td { color: var(--md-sys-color-on-surface); }
.btn-sm { padding: 4px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 6px; background: none; cursor: pointer; font-size: 13px; margin-right: 4px; color: var(--md-sys-color-on-surface); }
.btn-danger { color: var(--md-sys-color-error); border-color: var(--md-sys-color-error); }
.btn-primary { padding: 8px 20px; border: none; border-radius: 8px; background: var(--md-sys-color-primary); color: var(--md-sys-color-on-primary); font-size: 14px; cursor: pointer; }
.btn-text { border: none; background: none; color: var(--md-sys-color-primary); cursor: pointer; font-size: 14px; }
.modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,.3); display: flex; align-items: center; justify-content: center; z-index: 200; }
.modal { background: var(--md-sys-color-surface-container-high); padding: 24px; border-radius: 12px; width: 90%; max-width: 450px; }
.modal h3 { margin-bottom: 16px; color: var(--md-sys-color-on-surface); }
.field { margin-bottom: 12px; }
.field label { display: block; font-size: 13px; margin-bottom: 4px; color: var(--md-sys-color-on-surface-variant); }
.field input, .select-input {
  width: 100%; padding: 8px 12px; border: 1px solid var(--md-sys-color-outline); border-radius: 8px;
  font-size: 14px; background: var(--md-sys-color-surface); color: var(--md-sys-color-on-surface);
}
.modal-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 16px; }
</style>
