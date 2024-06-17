<template>
    <dialog v-if="show" class="modal modal-open modal-bottom sm:modal-middle">
        <div class="modal-box">
            <h2 class="mb-5">添加订单</h2>
            <form class="gap-5 flex flex-wrap" @submit.prevent="submitForm">
                <input class="input input-bordered w-full max-w-xs" v-model="order.orderNumber" placeholder="订单号"
                    @input="prependOrderNumberPrefix" />
                <select class="select select-bordered w-full max-w-xs" v-model="order.customerId">
                    <option value="" disabled>选择客户</option>
                    <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                        {{ customer.name }}
                    </option>
                </select>
                <select class="select select-bordered w-full max-w-xs" v-model="order.employeeId">
                    <option value="" disabled>选择员工</option>
                    <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                        {{ employee.name }}
                    </option>
                </select>
                <select class="select select-bordered w-full max-w-xs" v-model="order.productId" @change="SumUp">
                    <option value="" disabled>选择产品</option>
                    <option v-for="product in products" :key="product.id" :value="product.id">
                        {{ product.name }}
                    </option>
                </select>
                <input class="input input-bordered w-full max-w-xs" v-model="order.qty" placeholder="数量"
                    @change="SumUp" />
                <input class="input input-bordered w-full max-w-xs" v-model="order.totalAmount" placeholder="总金额"
                    disabled />
                <input type="date" class="input input-bordered w-full max-w-xs" v-model="order.orderDate" />
                <div class="flex gap-5">
                    <button type="submit" class="btn">添加</button>
                    <button type="button" @click="resetForm" class="btn">清除</button>
                </div>
            </form>
        </div>
        <form method="dialog" class="modal-backdrop">
            <button @click="closeModal">close</button>
        </form>
    </dialog>
    <button class="btn bg-accent text-accent-content" @click="showModal">
        <el-icon>
            <Plus />
        </el-icon>
    </button>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { Plus } from '@element-plus/icons-vue';
import { useStore } from 'vuex';

// 控制弹窗显示的变量
const show = ref(false);

const store = useStore();
// 订单信息
const order = ref({
    id: null,
    customerId: '',
    employeeId: '',
    productId: '',
    orderNumber: '',
    qty: '',
    totalAmount: '',
    orderDate: null
});


// 提交表单
const submitForm = () => {
    if (validateForm()) {
        const orderToSend = { ...order.value };

        store.dispatch('addOrder', orderToSend);
        store.dispatch('fetchAllOrders');

        showSuccessDialog('添加成功');
        resetForm();
        closeModal();
    }
};

// 表单验证
const validateForm = () => {
    if (!order.value.orderNumber || order.value.orderNumber.length < 2 || order.value.orderNumber.length > 50) {
        showAlertDialog('订单号必须在2到50个字符之间');
        return false;
    }
    if (!order.value.customerId) {
        showAlertDialog('请选择客户');
        return false;
    }
    if (!order.value.employeeId) {
        showAlertDialog('请选择员工');
        return false;
    }
    if (order.value.qty <= 0) {
        showAlertDialog('数量必须大于0');
        return false;
    }
    return true;
};

const showAlertDialog = (message) => {
    ElMessage({
        message: message,
        type: 'info',
        duration: 2000,
        plain: true
    });
};

const showSuccessDialog = (message) => {
    ElMessage({
        message: message,
        type: 'success',
        duration: 2000,
        plain: true
    });
};

// 控制弹窗开与关
const showModal = () => {
    show.value = true;
};

const closeModal = () => {
    show.value = false;
};

// 清空表单
const resetForm = () => {
    order.value = {
        id: null,
        customerId: null,
        employeeId: null,
        productId: null,
        orderNumber: '',
        qty: 0,
        totalAmount: 0.0,
        orderDate: null
    };
};

// 设置订单信息
const setOrder = (orderData) => {
    order.value = { ...orderData };
    isEdit.value = true;
    showModal();
};


// 客户、员工和产品数据
const customers = computed(() => {
    return store.state.customers.allcustomers;
});
const employees = computed(() => {
    return store.state.employees.allemployees;
});
const products = computed(() => {
    return store.state.products.allProducts;
});

onMounted(() => {
    fetchCustomers();
    fetchEmployees();
    fetchProducts();
});

const fetchCustomers = async () => {
    store.dispatch('fetchAllCustomers');
};

const fetchEmployees = async () => {
    store.dispatch('fetchAllEmployees');
};

const fetchProducts = async () => {
    store.dispatch('fetchAllProducts');
};

// 计算总金额
const SumUp = () => {
    if (order.value.productId === '' || order.value.qty === '' || order.value.qty <= 0) return;
    order.value.totalAmount = order.value.qty * store.state.products.allProducts.find(product => product.id === order.value.productId).price;
};

// 确保订单号以 "O" 开头
const prependOrderNumberPrefix = () => {
    if (!order.value.orderNumber.startsWith('O')) {
        order.value.orderNumber = 'O' + order.value.orderNumber.replace(/^O*/, '');
    }
};
</script>