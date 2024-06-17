<template>
    <div>
        <nav class="p-4 flex justify-between items-center h-1/8">
            <div class="flex gap-2 content-center">
                <OrderForm />
                <button class="btn bg-neutral text-white" @click="toggleSelectAll">
                    {{ isChecked ? '取消全选' : '全选本页' }}
                </button>
                <button class="btn bg-secondary text-white" @click="deleteSelectedOrders">多选删除</button>
                <printOrder />
            </div>
            <div class="hidden sm:flex space-x-4">
                <label class="flex cursor-pointer gap-2 content-center">
                    <OrderSrearh @search-results="handleSearchResults" />
                </label>
            </div>
        </nav>
        <div class="lg:hidden sm:flex space-x-4">
            <label class="flex cursor-pointer gap-2 content-center">
                <OrderSrearh @search-results="handleSearchResults" />
            </label>
        </div>
        <div class="overflow-x-auto bg-base-200 rounded-lg">
            <table class="table table-pin-rows table-pin-cols">
                <thead>
                    <tr>
                        <th></th>
                        <th>订单号</th>
                        <th>客户</th>
                        <th>负责员工</th>
                        <th>产品</th>
                        <th>数量</th>
                        <th>总金额</th>
                        <th>订单日期</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(order, index) in orders" :key="order.id" class="list-body" :draggable="true"
                        @dragstart="onDragStart(index)" @dragover.prevent @drop="onDrop(index)">
                        <th>
                            <label>
                                <input type="checkbox" class="checkbox" v-model="order.selected" />
                            </label>
                        </th>
                        <td>{{ order.orderNumber }}</td>
                        <td>{{ order.customerName }}</td>
                        <td>{{ order.employeeName }}</td>
                        <td>{{ order.productName }}</td>
                        <td>{{ order.qty }}</td>
                        <td>{{ order.totalAmount }}</td>
                        <td>{{ order.orderDate }}</td>
                        <td>
                            <div class="flex gap-2">
                                <button class="btn bg-neutral text-white" @click="OrderDetails(order.id)">编辑</button>
                                <button class="btn btn-square btn-outline" @click="deleteOrder(order.id)">
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
            <div v-if="!orders.length" class="flex justify-center items-center h-80">
                <p class="text-gray-500">暂无订单</p>
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
import OrderForm from './OrderForm.vue';
import OrderSrearh from './OrderSearch.vue';
import printOrder from './print/PrintOrder.vue';

// 引入store
const store = useStore();
const orders = computed(() => {
    const orderList = store.state.orders.orders;
    return Array.isArray(orderList) ?
        orderList.map(order => ({
            ...order,
            selected: false
        })) : [];// 添加 selected 属性
});

// 删除订单
const deleteOrder = (id) => {
    store.dispatch('deleteOrder', id);
};

// 处理搜索结果
const handleSearchResults = (results) => {
    if (results) {
        const ordersArray = Array.isArray(results) ? results : [results];
        store.commit('setSearchOrders', ordersArray);
    } else {
        store.dispatch('fetchOrders');
    }
};

// 删除选中的订单
const deleteSelectedOrders = () => {
    const selectedOrders = orders.value.filter(order => order.selected);
    const selectedIds = selectedOrders.map(order => order.id);
    store.dispatch('deleteSelectedOrders', selectedIds);
};

// 初始化订单数据
import { onMounted } from 'vue';

onMounted(() => {
    store.dispatch('fetchOrders');
});

const router = useRouter();

const OrderDetails = (id) => {
    router.push({ path: `/order/${id}` });
};

// 定义全选状态
const isChecked = ref(false);

const toggleSelectAll = () => {
    isChecked.value = !isChecked.value;
    orders.value.forEach(order => {
        order.selected = isChecked.value;
    });
};

const disabledSelectAll = () => {
    if (isChecked.value) {
        isChecked.value = false;
        orders.value.forEach(order => {
            order.selected = false;
        });
    }
};

// 分页
const currentPage = computed(() => store.state.orders.currentPage);
const pageSize = computed(() => store.state.orders.pageSize);
const total = computed(() => store.state.orders.totalOrders);

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const setPage = (page) => {
    store.dispatch('setoCurrentPage', page);
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
        const draggedItem = orders.value[draggedIndex];
        orders.value.splice(draggedIndex, 1);
        orders.value.splice(index, 0, draggedItem);
        store.state.orders.orders = orders.value;
        draggedIndex = null;
    }
};
</script>