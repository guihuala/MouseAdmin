<template>
    <div class="p-4">
        <el-page-header :icon="ArrowLeft" @click="goBack">
            <template #content>
                <span class="text-large font-600 mr-3" v-if="customer"> {{ customer.name }}</span>
            </template>
        </el-page-header>
        <div v-if="customer" class="my-5">
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
                <div class="flex flex-col">
                    <label for="name" class="mb-1">名称</label>
                    <div class="input input-bordered flex items-center gap-2">
                        <el-icon>
                            <User />
                        </el-icon>
                        <input id="name" type="text" class="flex" placeholder="名称" v-model="customer.name"
                            :disabled="!isEditing" />
                    </div>
                </div>
                <div class="flex flex-col">
                    <label for="email" class="mb-1">邮箱</label>
                    <div class="input input-bordered flex items-center gap-2">
                        <el-icon>
                            <Message />
                        </el-icon>
                        <input id="email" type="text" class="flex" placeholder="邮箱" v-model="customer.email"
                            :disabled="!isEditing" />
                    </div>
                </div>
                <div class="flex flex-col">
                    <label for="address" class="mb-1">地址</label>
                    <div class="input input-bordered flex items-center gap-2">
                        <el-icon>
                            <Position />
                        </el-icon>
                        <input id="address" type="text" class="grow" placeholder="地址" v-model="customer.address"
                            :disabled="!isEditing" />
                    </div>
                </div>
            </div>
        </div>
        <div class="flex flex-row gap-5">
            <button class="btn bg-primary text-white" @click="toggleEdit">{{ isEditing ? '取消修改' : '编辑信息' }}</button>
            <button class="btn bg-accent text-accent-content" @click="editCustomer" :disabled="!isEditing">提交</button>
        </div>
        <table class="table table-pin-rows table-pin-cols">
            <thead>
                <tr>
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
            <tbody v-for="order in orders" :key="order.id" class="list-body">
                <tr>
                    <td>{{ order.orderNumber }}</td>
                    <td>{{ order.customerName }}</td>
                    <td>{{ order.employeeName }}</td>
                    <td>{{ order.productName }}</td>
                    <td>{{ order.qty }}</td>
                    <td>{{ order.totalAmount }}</td>
                    <td>{{ order.orderDate }}</td>
                    <td>
                        <div class="flex gap-2">
                            <button class="btn bg-neutral text-white" @click="goDetails(order.id)">编辑</button>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-if="!orders.length" class="flex justify-center items-center h-80">
            <p class="text-gray-500">暂无订单</p>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ArrowLeft, User, Message, Position } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'

const router = useRouter()
const route = useRoute();
const userId = ref(route.params.id);

// 获取客户信息
const store = useStore();
const customer = computed(() => store.state.customers.foundCustomer);
// 获取客户全部订单（待实现）
const orders = computed(() => store.state.orders.foundOrder);

onMounted(() => {
    store.dispatch('findCustomerById', userId.value);
    store.dispatch('findOrderByCustomerId', userId.value);
});

const isEditing = ref(false);

const toggleEdit = () => {
    isEditing.value = !isEditing.value;
    if (!isEditing.value) {
        // 如果取消编辑，重置客户信息
        store.dispatch('findCustomerById', userId.value);
    }
}

const editCustomer = () => {
    store.dispatch('updateCustomer', customer.value);
    isEditing.value = false;
}

const goDetails = (id) => {
    router.push(`/order/${id}`);
}

const goBack = () => {
    router.go(-1);
}
</script>