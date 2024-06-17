<template>
    <div>
        <dialog v-if="show" class="modal modal-open modal-bottom sm:modal-middle">
            <div class="modal-box">
                <h2 class="mb-5">注册</h2>
                <form class="gap-5 flex flex-wrap" @submit.prevent="submitForm">
                    <input class="input input-bordered w-full max-w-xs" v-model="localUser.username"
                        placeholder="用户名(字母或数字)" />
                    <input class="input input-bordered w-full max-w-xs" v-model="localUser.email" placeholder="邮箱" />
                    <input type="password" class="input input-bordered w-full max-w-xs" v-model="localUser.password"
                        placeholder="密码">
                    <select class="select select-bordered w-full max-w-xs" v-model="localUser.role">
                        <option value="" disabled>账号角色</option>
                        <option value="用户">用户</option>
                        <option value="管理员">管理员</option>
                    </select>
                    <div class="flex gap-5">
                        <button type="submit" class="btn">注册</button>
                    </div>
                </form>
            </div>
            <form method="dialog" class="modal-backdrop">
                <button @click="closeModal">关闭</button>
            </form>
        </dialog>
        <a href="#" class="label-text-alt link link-hover" @click="showModal">没有账号？注册！</a>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useStore } from 'vuex';

// 控制弹窗显示的变量
const show = ref(false);

// 获取员工信息
const store = useStore();
const localUser = ref({ id: null, username: '', email: '', password: '', role: '' });

// 提交表单
const submitForm = () => {
    if (validateForm()) {
        const userToSend = { ...localUser.value };

        store.dispatch('register', userToSend)
            .then(() => {
                showSuccessDialog('注册成功');
                resetForm();
                closeModal();
            })
            .catch(error => {
                showAlertDialog('注册失败，用户名已存在，请重试');
            });
    }
};

// 表单验证
const validateForm = () => {
    if (!localUser.value.username || !validateName(localUser.value.username)) {
        showAlertDialog('用户名必须在2到10个字符之间,且为字母或数字');
        return false;
    }
    if (!localUser.value.email || !validateEmail(localUser.value.email)) {
        showAlertDialog('请输入有效的邮箱地址');
        return false;
    }
    if (!localUser.value.password || !validatePassword(localUser.value.password)) {
        showAlertDialog('密码必须在6到20个字符之间,至少包含一个字母和数字');
        return false;
    }
    if (!localUser.value.role) {
        showAlertDialog('请选择账号角色');
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

const validatePassword = (password) => {
    // 设定密码验证规则
    const re = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/;
    // 返回验证结果
    return re.test(password);
};

const validateName = (name) => {
    // 设定名称验证规则
    const re = /^[a-zA-Z0-9_-]{2,10}$/;
    // 返回验证结果
    return re.test(name);
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
    localUser.value = { id: null, username: '', email: '', password: '' };
};
</script>