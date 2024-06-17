<template>
  <div>
    <nav class="p-4 flex justify-between items-center h-1/8">
      <div class="flex gap-2 content-center">
        <EmployeeForm />
        <button class="btn bg-neutral text-white" @click="toggleSelectAll">
          {{ isChecked ? '取消全选' : '全选本页' }}
        </button>
        <button class="btn bg-secondary text-white" @click="deleteSelectedEmployees">多选删除</button>
        <printEmployee />
      </div>
      <div class="hidden sm:flex space-x-4">
        <label class="flex cursor-pointer gap-2 content-center">
          <employeeSearch @search-results="handleSearchResults" />
        </label>
      </div>
    </nav>
    <div class="lg:hidden sm:flex space-x-2">
      <label class="flex cursor-pointer gap-1 content-center">
        <employeeSearch @search-results="handleSearchResults" />
      </label>
    </div>
    <div class="overflow-x-auto bg-base-200 rounded-lg">
      <table class="table table-pin-rows table-pin-cols">
        <thead>
          <tr>
            <th></th>
            <th>编号</th>
            <th>名称</th>
            <th>邮箱</th>
            <th>职位</th>
            <th>工资</th>
            <th>入职日期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(employee, index) in employees" :key="employee.id" class="list-body" :draggable="true"
            @dragstart="onDragStart(index)" @dragover.prevent @drop="onDrop(index)">
            <th>
              <label>
                <input type="checkbox" class="checkbox" v-model="employee.selected" />
              </label>
            </th>
            <td>{{ employee.id }}</td>
            <td>{{ employee.name }}</td>
            <td>{{ employee.email }}</td>
            <td>{{ employee.position }}</td>
            <td>{{ employee.salary }}</td>
            <td>{{ employee.hireDate }}</td>
            <td>
              <div class="flex gap-2">
                <button class="btn bg-neutral text-white" @click="EmployeeDetails(employee.id)">详情</button>
                <button class="btn btn-square btn-outline" @click="deleteEmployee(employee.id)">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="!employees.length" class="flex justify-center items-center h-80">
        <p class="text-gray-500">暂无员工</p>
      </div>
    </div>
    <div class="flex mt-5 justify-center">
      <div class="join">
        <button class="join-item btn" @click="prevPage" :disabled="currentPage === 1">«</button>
        <button class="join-item btn">{{ currentPage }}</button>
        <button class="join-item btn" @click="nextPage" :disabled="currentPage === totalPages">»</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import EmployeeForm from './EmployeeForm.vue';
import printEmployee from './print/PrintEmployee.vue';
import employeeSearch from './EmployeeSearch.vue';

// 引入 store
const store = useStore();

const employees = computed(() => {
  const employeeList = store.state.employees.employees;
  return Array.isArray(employeeList) ? employeeList.map(employee => ({
    ...employee,
    selected: false
  })) : [];// 添加 selected 属性
});

// 删除员工
const deleteEmployee = (id) => {
  store.dispatch('deleteEmployee', id);
};

// 处理搜索结果
const handleSearchResults = (results) => {
  if (results) {
    const employeesArray = Array.isArray(results) ? results : [results];
    store.commit('setSearchEmployees', employeesArray);
  } else {
    store.dispatch('fetchEmployees');
  }
};

// 初始化员工数据
onMounted(() => {
  store.dispatch('fetchEmployees');
});

// 查看员工详情
const router = useRouter();

const EmployeeDetails = (id) => {
  router.push({ path: `/employee/${id}` });
};

// 定义全选状态
const isChecked = ref(false);

const toggleSelectAll = () => {
  isChecked.value = !isChecked.value;
  employees.value.forEach(employee => {
    employee.selected = isChecked.value;
  });
};

const disabledSelectAll = () => {
  if (isChecked.value) {
    isChecked.value = false;
    employees.value.forEach(employee => {
      employee.selected = false;
    });
  }
};

// 删除选中的员工
const deleteSelectedEmployees = () => {
  const selectedEmployees = employees.value.filter(employee => employee.selected);
  const selectedIds = selectedEmployees.map(employee => employee.id);
  store.dispatch('deleteSelectedEmployees', selectedIds);
};

// 分页
const currentPage = computed(() => store.state.employees.currentPage);
const pageSize = computed(() => store.state.employees.pageSize);
const total = computed(() => store.state.employees.total);

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const setPage = (page) => {
  store.dispatch('seteCurrentPage', page);
};

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    setPage(currentPage.value + 1);
    disabledSelectAll();
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    setPage(currentPage.value - 1);
    disabledSelectAll();
  }
};

// 拖拽功能实现
let draggedIndex = null;

const onDragStart = (index) => {
  draggedIndex = index;
};

const onDrop = (index) => {
  if (draggedIndex !== null) {
    const draggedItem = employees.value[draggedIndex];
    employees.value.splice(draggedIndex, 1);
    employees.value.splice(index, 0, draggedItem);
    store.state.employees.employees = employees.value;
    draggedIndex = null;
  }
};
</script>