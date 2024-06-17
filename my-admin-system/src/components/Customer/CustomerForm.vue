<template>

    <dialog v-if="show" class="modal modal-open modal-bottom sm:modal-middle">
        <div class="modal-box">
            <h2 class="mb-5">添加客户</h2>
            <form class="gap-5 flex flex-wrap" @submit.prevent="submitForm">
                <input class="input input-bordered w-full max-w-xs" v-model="customer.name" placeholder="名称" />
                <input class="input input-bordered w-full max-w-xs" v-model="customer.email" placeholder="邮箱" />
                <input class="input input-bordered w-full max-w-xs" v-model="customer.address" placeholder="地址" />
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
import { ref } from 'vue';
import { useStore } from 'vuex';
import { Plus } from '@element-plus/icons-vue';

//控制弹窗显示的变量
const show = ref(false);

//获取客户信息
const store = useStore();
const customer = ref({ id: null, name: '', email: '', address: '' });

//提交表单
const submitForm = () => {
    if (validateForm()) {
        const customerToSend = { ...customer.value };

        store.dispatch('addCustomer', customerToSend);
        store.dispatch('fetchCustomers');

        showSuccessDialog('添加成功');
        resetForm();
        closeModal();
    }
};

// 表单验证
const validateForm = () => {
    if (!customer.value.name || customer.value.name.length < 2 || customer.value.name.length > 50) {
        showAlertDialog('名称必须在2到50个字符之间');
        return false;
    }
    if (!customer.value.email || !validateEmail(customer.value.email)) {
        showAlertDialog('请输入有效的邮箱地址');
        return false;
    }
    if (customer.value.address && customer.value.address.length > 100) {
        showAlertDialog('地址不能超过100个字符');
        return false;
    }
    return true;
};

// 邮箱验证
const validateEmail = (email) => {
    //设定邮箱验证规则
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    //返回验证结果
    return re.test(email);
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

//控制弹窗开与关
const showModal = () => {
    show.value = true;
};

const closeModal = () => {
    show.value = false;
};

//清空表单
const resetForm = () => {
    customer.value = { id: null, name: '', email: '', address: '' };
};
</script>