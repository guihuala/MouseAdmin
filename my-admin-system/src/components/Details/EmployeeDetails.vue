<template>
    <div class="p-4">
        <el-page-header :icon="ArrowLeft" @click="goBack">
            <template #content>
                <span class="text-large font-600 mr-3" v-if="employee">{{ employee.name }}</span>
            </template>
        </el-page-header>
        <div v-if="employee" class="my-5">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
            <div class="flex flex-col">
                <label for="name" class="mb-1">名称</label>
                <div class="input input-bordered flex items-center gap-2">
                    <el-icon>
                        <User />
                    </el-icon>
                    <input id="name" type="text" class="flex" placeholder="名称" v-model="employee.name" :disabled="!isEditing" />
                </div>
            </div>
            <div class="flex flex-col">
                <label for="email" class="mb-1">邮箱</label>
                <div class="input input-bordered flex items-center gap-2">
                    <el-icon>
                        <Message />
                    </el-icon>
                    <input id="email" type="text" class="flex" placeholder="邮箱" v-model="employee.email" :disabled="!isEditing" />
                </div>
            </div>
            <div class="flex flex-col">
                <label for="position" class="mb-1">职位</label>
                <select id="position" class="select select-bordered" v-model="employee.position" :disabled="!isEditing">
                    <option value="" disabled>职位</option>
                    <option value="财务">财务</option>
                    <option value="业务">业务</option>
                    <option value="文宣">文宣</option>
                    <option value="技术">技术</option>
                </select>
            </div>
            <div class="flex flex-col">
                <label for="salary" class="mb-1">工资</label>
                <div class="input input-bordered flex items-center gap-2">
                    <el-icon>
                        <Money />
                    </el-icon>
                    <input id="salary" type="text" class="grow" placeholder="工资" v-model="employee.salary" :disabled="!isEditing" />
                </div>
            </div>
            <div class="flex flex-col">
                <label for="hireDate" class="mb-1">入职日期</label>
                <div class="input input-bordered flex items-center gap-2">
                    <el-icon>
                        <Calendar />
                    </el-icon>
                    <input id="hireDate" type="date" class="grow" placeholder="入职日期" v-model="employee.hireDate" :disabled="!isEditing" />
                </div>
            </div>
        </div>
    </div>
        <div class="flex flex-row gap-5">
            <button class="btn bg-primary text-white" @click="toggleEdit">{{ isEditing ? '取消修改' : '编辑信息' }}</button>
            <button class="btn bg-accent text-accent-content" @click="editEmployee" :disabled="!isEditing">提交</button>
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
            <p class="text-gray-500">暂无负责订单</p>
        </div>

    </div>
</template>

<script lang="ts" setup>
import { ArrowLeft, User, Message, Money, Calendar } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'

const router = useRouter()
const route = useRoute();
const userId = ref(route.params.id);

// 获取客户信息
const store = useStore();
const employee = computed(() => store.state.employees.foundEmployee);
// 获取客户全部订单（待实现）
const orders = computed(() => store.state.orders.foundOrder);

onMounted(() => {
    store.dispatch('findEmployeeById', userId.value);
    store.dispatch('findOrderByEmployeeId', userId.value);
});

const isEditing = ref(false);

const toggleEdit = () => {
    isEditing.value = !isEditing.value;
    if (!isEditing.value) {
        // 如果取消编辑，重置客户信息
        store.dispatch('findEmployeeById', userId.value);
    }
}

const editEmployee = () => {
    store.dispatch('updateEmployee', employee.value);
    isEditing.value = false;
}

const goDetails = (id) => {
    router.push(`/order/${id}`);
}

const goBack = () => {
    router.go(-1);
}
</script>