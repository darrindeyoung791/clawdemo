<template>
  <div>
    <div class="page-header">
      <h2>设备管理</h2>
      <button class="filled-btn" @click="openAdd"><span class="material-icons">add</span> 添加设备</button>
    </div>
    <div class="filter-bar">
      <div class="text-field" style="width:300px">
        <label>搜索</label>
        <div class="field-wrap"><input v-model="keyword" placeholder="搜索设备" @input="search" /></div>
      </div>
    </div>
    <div style="background:var(--md-sys-color-surface-container-low);border-radius:16px;border:1px solid var(--md-sys-color-outline-variant);overflow:hidden">
      <table class="data-table">
        <thead>
          <tr><th>名称</th><th>编号</th><th>分类</th><th>位置</th><th>总量</th><th>可用</th><th>状态</th><th></th></tr>
        </thead>
        <tbody>
          <tr v-for="item in list" :key="item.id">
            <td style="font-weight:500">{{ item.name }}</td>
            <td style="font-size:13px;color:var(--md-sys-color-on-surface-variant)">{{ item.code }}</td>
            <td>{{ CATEGORY_LABELS[item.category] }}</td>
            <td>{{ item.location }}</td>
            <td>{{ item.totalQuantity }}</td>
            <td>{{ item.availableQuantity }}</td>
            <td><span class="status-badge" :class="item.status">{{ STATUS_LABELS[item.status] }}</span></td>
            <td>
              <div style="display:flex;gap:4px">
                <button class="sm-btn sm-btn-ghost" @click="openEdit(item)"><span class="material-icons" style="font-size:16px">edit</span></button>
                <button class="sm-btn sm-btn-ghost" @click="doDelete(item.id)"><span class="material-icons" style="font-size:16px">delete</span></button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="list.length === 0" class="msg-empty">暂无设备</p>
    </div>

    <div v-if="showForm" class="modal-overlay open" @click.self="showForm = false"></div>
    <div v-if="showForm" class="modal">
      <h3>{{ editing ? '编辑设备' : '添加设备' }}</h3>
      <div class="text-field">
        <label>名称</label>
        <div class="field-wrap"><input v-model="form.name" /></div>
      </div>
      <div class="text-field">
        <label>分类</label>
        <div class="field-wrap">
          <select v-model="form.category">
            <option v-for="c in categories" :key="c" :value="c">{{ CATEGORY_LABELS[c] }}</option>
          </select>
        </div>
      </div>
      <div class="text-field">
        <label>位置</label>
        <div class="field-wrap"><input v-model="form.location" /></div>
      </div>
      <div class="text-field">
        <label>总量</label>
        <div class="field-wrap"><input v-model.number="form.totalQuantity" type="number" min="1" /></div>
      </div>
      <div class="modal-actions">
        <button class="text-btn" @click="showForm = false">取消</button>
        <button class="filled-btn" @click="save">保存</button>
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
