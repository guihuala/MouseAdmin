<template>
    <div>
        <dialog v-if="show" class="modal modal-open modal-bottom sm:modal-middle">
            <div class="modal-box">
                <h2 class="mb-5">找回密码</h2>
                <form class="gap-5 flex flex-wrap" @submit.prevent="submitForm">
                    <input class="input input-bordered w-full max-w-xs" v-model="username" placeholder="用户名" />
                    <input class="input input-bordered w-full max-w-xs" v-model="email" placeholder="邮箱" />
                    <div class="flex gap-5">
                        <button type="submit" class="btn" v-if="!openPasswordDialog">验证账户</button>
                    </div>
                </form>
                <div v-if="openPasswordDialog">
                    <h2 class="my-5">请重设密码</h2>
                    <form class="gap-5 flex flex-wrap" @submit.prevent="resetPassword">
                        <input type="password" class="input input-bordered w-full max-w-xs" v-model="password" placeholder="新密码"/>
                        <input type="password" class="input input-bordered w-full max-w-xs" v-model="confirmPassword" placeholder="确认密码" />
                        <div class="flex gap-5">
                            <button type="submit" class="btn">找回密码</button>
                        </div>
                    </form>
                </div>
            </div>
            <form method="dialog" class="modal-backdrop">
                <button @click="closeModal">关闭</button>
            </form>
        </dialog>


        <a href="#" class="label-text-alt link link-hover" @click="showModal">忘记密码？</a>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useStore } from 'vuex';

// 控制弹窗显示的变量
const show = ref(false);
const openPasswordDialog = ref(false);

// 获取邮箱信息
const store = useStore();
const username = ref('');
const email = ref('');
const password = ref('');
const confirmPassword = ref('');

// 提交表单
const submitForm = () => {
    if (validateForm()) {
        findUser();
    }
};

// 表单验证
const validateForm = () => {
    if (!username.value) {
        showAlertDialog('请输入用户名');
        return false;
    }
    if (!email.value || !validateEmail(email.value)) {
        showAlertDialog('请输入有效的邮箱地址');
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

const submitPassword = () => {
    if (!password.value || !confirmPassword.value) {
        showAlertDialog('密码不能为空');
        return false;
    }
    if (password.value !== confirmPassword.value) {
        showAlertDialog('两次密码输入不一致');
        return false;
    }
    if (!validatePassword(password.value)) {
        showAlertDialog('密码必须在6到20个字符之间,至少包含一个字母和数字');
        return false;
    }
    return true;
};

// 验证用户是否存在
const findUser = () => {
    store.dispatch('findUsername', { username: username.value, email: email.value })
        .then(() => {
            showSuccessDialog('验证成功，请重设密码');
            openPasswordDialog.value = true;
        })
        .catch(error => {
            showAlertDialog('验证失败，请重试');
        });
};

// 验证并重设密码
const resetPassword = () => {
    if (submitPassword()) {
        store.dispatch('findPassword', { username: username.value, email: email.value, password: password.value })
            .then(() => {
                showSuccessDialog('密码重设成功');
                closeModal();
            })
            .catch(error => {
                showAlertDialog('密码重设失败，请重试');
            });
    }
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
    openPasswordDialog.value = false;
    resetForm();
};

// 清空表单
const resetForm = () => {
    email.value = '';
    username.value = '';
    password.value = '';
    confirmPassword.value = '';
};
</script>