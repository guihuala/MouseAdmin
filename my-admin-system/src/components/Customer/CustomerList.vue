<template>
    <div>
        <nav class="p-4 flex justify-between items-center h-1/8">
            <div class="flex gap-2 content-center">
                <CustomerForm />
                <button class="btn bg-neutral text-white" @click="toggleSelectAll">
                    {{ isChecked ? '取消全选' : '全选本页' }}
                </button>
                <button class="btn bg-secondary text-white" @click="deleteSelectedCustomers">多选删除</button>
                <printCustomer />
            </div>
            <div class="hidden sm:flex space-x-4">
                <label class="flex cursor-pointer gap-2 content-center">
                    <customerSearch @search-results="handleSearchResults" />
                </label>
            </div>
        </nav>
        <div class="lg:hidden sm:flex space-x-4">
                <label class="flex cursor-pointer gap-2 content-center">
                    <customerSearch @search-results="handleSearchResults" />
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
                        <th>地址</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(customer, index) in customers" :key="customer.id" class="list-body" :draggable="true"
                        @dragstart="onDragStart(index)" @dragover.prevent @drop="onDrop(index)">
                        <th>
                            <label>
                                <input type="checkbox" class="checkbox" v-model="customer.selected" />
                            </label>
                        </th>
                        <td>{{ customer.id }}</td>
                        <td>{{ customer.name }}</td>
                        <td>{{ customer.email }}</td>
                        <td>{{ customer.address }}</td>
                        <td>
                            <div class="flex gap-2">
                                <button class="btn bg-neutral text-white"
                                    @click="CustomerDetails(customer.id)">详情</button>
                                <button class="btn btn-square btn-outline" @click="deleteCustomer(customer.id)">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none"
                                        viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M6 18L18 6M6 6l12 12" />
                                    </svg>
                                </button>

                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div v-if="!customers.length" class="flex justify-center items-center h-80">
                <p class="text-gray-500">暂无客户</p>
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
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import CustomerForm from './CustomerForm.vue';
import customerSearch from './CustomerSearch.vue';
import printCustomer from '../print/PrintCustomer.vue';

//引入store
const store = useStore();
const customers = computed(() => {
    const customerList = store.state.customers.customers;
    return Array.isArray(customerList) ?
        customerList.map(customer => ({
            ...customer,
            selected: false
        })) : [];//添加selected属性
});

//删除客户
const deleteCustomer = (id) => {
    store.dispatch('deleteCustomer', id);
};

//删除选中的客户
const deleteSelectedCustomers = () => {
    const selectedCustomers = customers.value.filter(customer => customer.selected);
    const selectedIds = selectedCustomers.map(customer => customer.id);
    store.dispatch('deleteSelectedCustomers', selectedIds);
};

//处理搜索结果
const handleSearchResults = (results) => {
    if (results) {
        const customersArray = Array.isArray(results) ? results : [results];
        store.commit('setSearchCustomers', customersArray);
    } else {
        store.dispatch('fetchCustomers');
    }
};

//初始化客户数据
import { onMounted } from 'vue';

onMounted(() => {
    store.dispatch('fetchCustomers');
});

//查看客户详情
const router = useRouter();

const CustomerDetails = (id) => {
    router.push({ path: `/customer/${id}` });
};

// 定义全选状态
const isChecked = ref(false);

const toggleSelectAll = () => {
    isChecked.value = !isChecked.value;
    customers.value.forEach(customer => {
        customer.selected = isChecked.value;
    });
};

const disabledSelectAll = () => {
    if (isChecked.value) {
        isChecked.value = false;
        customers.value.forEach(customer => {
            customer.selected = false;
        });
    }
};

//分页
const currentPage = computed(() => store.state.customers.currentPage);
const pageSize = computed(() => store.state.customers.pagesize);
const total = computed(() => store.state.customers.total);

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const setPage = (page) => {
    store.dispatch('setCurrentPage', page);
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

let draggedIndex = null;

const onDragStart = (index) => {
    draggedIndex = index;
};

const onDrop = (index) => {
    if (draggedIndex !== null) {
        const draggedItem = customers.value[draggedIndex];
        customers.value.splice(draggedIndex, 1);
        customers.value.splice(index, 0, draggedItem);
        store.state.customers.customers = customers.value;
        draggedIndex = null;
    }
};
</script>