<template>
     <div class="p-4">
        <el-page-header :icon="ArrowLeft" @click="goBack">
            <template #content>
                <span class="text-large font-600 mr-3">订单详情</span>
            </template>
        </el-page-header>
        <div v-if="order" class="my-5">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
                <div class="flex flex-col">
                    <label for="orderNumber" class="mb-1">订单号</label>
                    <div class="input input-bordered flex items-center gap-2">
                        <el-icon>
                            <User />
                        </el-icon>
                        <input id="orderNumber" type="text" class="flex" placeholder="订单号" v-model="order.orderNumber" :disabled="!isEditing"
                            @input="prependOrderNumberPrefix" />
                    </div>
                </div>
                <div class="flex flex-col">
                    <label for="customerId" class="mb-1">选择客户</label>
                    <select id="customerId" class="select select-bordered" v-model="order.customerId" :disabled="!isEditing">
                        <option value="" disabled>选择客户</option>
                        <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                            {{ customer.name }}
                        </option>
                    </select>
                </div>
                <div class="flex flex-col">
                    <label for="employeeId" class="mb-1">选择员工</label>
                    <select id="employeeId" class="select select-bordered" v-model="order.employeeId" :disabled="!isEditing">
                        <option value="" disabled>选择员工</option>
                        <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                            {{ employee.name }}
                        </option>
                    </select>
                </div>
                <div class="flex flex-col">
                    <label for="productId" class="mb-1">选择产品</label>
                    <select id="productId" class="select select-bordered" v-model="order.productId" @change="SumUp" :disabled="!isEditing">
                        <option value="" disabled>选择产品</option>
                        <option v-for="product in products" :key="product.id" :value="product.id">
                            {{ product.name }}
                        </option>
                    </select>
                </div>
                <div class="flex flex-col">
                    <label for="qty" class="mb-1">数量</label>
                    <div class="input input-bordered flex items-center gap-2">
                        <el-icon>
                            <ShoppingTrolley />
                        </el-icon>
                        <input id="qty" type="number" class="grow" placeholder="数量" v-model.number="order.qty" :disabled="!isEditing"
                            @change="SumUp" />
                    </div>
                </div>
                <div class="flex flex-col">
                    <label for="totalAmount" class="mb-1">总金额</label>
                    <div class="input input-bordered flex items-center gap-2">
                        <el-icon>
                            <Money />
                        </el-icon>
                        <input id="totalAmount" type="text" class="grow" placeholder="总金额" v-model="order.totalAmount" :disabled="true" />
                    </div>
                </div>
                <div class="flex flex-col">
                    <label for="orderDate" class="mb-1">订单日期</label>
                    <div class="input input-bordered flex items-center gap-2">
                        <el-icon>
                            <Calendar />
                        </el-icon>
                        <input id="orderDate" type="date" class="grow" placeholder="订单日期" v-model="order.orderDate"
                            :disabled="!isEditing" />
                    </div>
                </div>
            </div>
        </div>
        <div class="flex flex-row gap-5 mt-5">
            <button class="btn bg-primary text-white" @click="toggleEdit">{{ isEditing ? '取消修改' : '编辑信息' }}</button>
            <button class="btn bg-accent text-accent-content" @click="editOrder" :disabled="!isEditing">提交</button>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ArrowLeft, User, Position, ShoppingTrolley, Money,Calendar } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'

const router = useRouter()
const route = useRoute();
const orderId = ref(route.params.id);

// 获取订单信息
const store = useStore();
const order = computed(() => store.state.orders.foundOrder);

onMounted(() => {
    store.dispatch('findOrderById', orderId.value);
});

const isEditing = ref(false);

const toggleEdit = () => {
    isEditing.value = !isEditing.value;
    if (!isEditing.value) {
        // 如果取消编辑，重置订单信息
        store.dispatch('findOrderById', orderId.value);
    }
}

const editOrder = () => {
    store.dispatch('updateOrder', order.value);
    console.log(order.value);
    isEditing.value = false;
}

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

const fetchCustomers = () => {
    store.dispatch('fetchAllCustomers');
};

const fetchEmployees = () => {
    store.dispatch('fetchAllEmployees');
};

const fetchProducts = () => {
    store.dispatch('fetchAllProducts');
};

// 计算总金额
const SumUp = () => {
    if (order.value.productId === '' || order.value.qty === '' || order.value.qty <= 0) return;
    const selectedProduct = store.state.products.allProducts.find(product => product.id === order.value.productId);
    if (selectedProduct) {
        order.value.totalAmount = order.value.qty * selectedProduct.price;
    }
};

// 确保订单号以 "O" 开头
const prependOrderNumberPrefix = () => {
    if (!order.value.orderNumber.startsWith('O')) {
        order.value.orderNumber = 'O' + order.value.orderNumber.replace(/^O*/, '');
    }
};

const goBack = () => {
    router.go(-1);
}
</script>