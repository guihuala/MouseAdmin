<template>
    <div>
        <dialog v-if="show" class="modal modal-open modal-bottom sm:modal-middle">
            <div class="modal-box">
                <h2 class="mb-5">添加员工</h2>
                <form class="gap-5 flex flex-wrap" @submit.prevent="submitForm">
                    <input class="input input-bordered w-full max-w-xs" v-model="localEmployee.name" placeholder="名称" />
                    <input class="input input-bordered w-full max-w-xs" v-model="localEmployee.email"
                        placeholder="邮箱" />
                    <select class="select select-bordered w-full max-w-xs" v-model="localEmployee.position">
                        <option value="" disabled>职位</option>
                        <option value="财务">财务</option>
                        <option value="业务">业务</option>
                        <option value="文宣">文宣</option>
                        <option value="技术">技术</option>
                    </select>
                    <input class="input input-bordered w-full max-w-xs" v-model="localEmployee.salary"
                        placeholder="薪水" />
                    <label for="hireDate" class="block text-sm font-medium text-gray-700">入职日期</label>
                    <input type="date" class="input input-bordered w-full max-w-xs" v-model="localEmployee.hireDate" />
                    <div class="flex gap-5">
                        <button type="submit" class="btn">添加</button>
                        <button type="button" @click="resetForm" class="btn">清除</button>
                    </div>
                </form>
            </div>
            <form method="dialog" class="modal-backdrop">
                <button @click="closeModal">关闭</button>
            </form>
        </dialog>

        <button class="btn bg-accent text-accent-content" @click="showModal">
            <el-icon>
                <Plus />
            </el-icon>
        </button>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useStore } from 'vuex';
import { Plus } from '@element-plus/icons-vue';

// 控制弹窗显示的变量
const show = ref(false);

// 获取员工信息
const store = useStore();
const localEmployee = ref({ id: null, name: '', email: '', position: '', salary: '', hireDate: '' });

// 提交表单
const submitForm = () => {
    if (validateForm()) {
        const employeeToSend = { ...localEmployee.value };

        store.dispatch('addEmployee', employeeToSend);
        store.dispatch('fetchEmployees');

        showSuccessDialog('添加成功');
        resetForm();
        closeModal();
    }
};

// 表单验证
const validateForm = () => {
    if (!localEmployee.value.name || localEmployee.value.name.length < 2 || localEmployee.value.name.length > 50) {
        showAlertDialog('名称必须在2到50个字符之间');
        return false;
    }
    if (!localEmployee.value.email || !validateEmail(localEmployee.value.email)) {
        showAlertDialog('请输入有效的邮箱地址');
        return false;
    }
    if (!localEmployee.value.position) {
        showAlertDialog('职位不能为空');
        return false;
    }
    if (!localEmployee.value.salary || isNaN(localEmployee.value.salary)) {
        showAlertDialog('请输入有效的薪水');
        return false;
    }
    if (!localEmployee.value.hireDate) {
        showAlertDialog('入职日期不能为空');
        return false;
    }
    return true;
};

// 邮箱验证
const validateEmail = (email) => {
    // 设定邮箱验证规则
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    // 返回验证结果
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

// 控制弹窗开与关
const showModal = () => {
    show.value = true;
};

const closeModal = () => {
    show.value = false;
    resetForm();
};

// 清空表单
const resetForm = () => {
    localEmployee.value = { id: null, name: '', email: '', position: '', salary: '', hireDate: '' };
};
</script>