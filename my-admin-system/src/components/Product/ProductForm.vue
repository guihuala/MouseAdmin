<template>
    <dialog v-if="show" class="modal modal-open modal-bottom sm:modal-middle">
        <div class="modal-box">
            <h2 class="mb-5">添加产品</h2>
            <form class="gap-5 flex flex-wrap" @submit.prevent="submitForm">
                <input class="input input-bordered w-full max-w-xs" v-model="product.name" placeholder="名称" />  
                <input class="input input-bordered w-full max-w-xs" v-model="product.price" placeholder="价格" />
                <textarea class="textarea textarea-bordered w-full max-w-xs" v-model="product.description" placeholder="产品简介" />
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
import { Plus, PriceTag } from '@element-plus/icons-vue';

//控制弹窗显示的变量
const show = ref(false);

//获取产品信息
const store = useStore();
const product = ref({ id: null, name: '', description: '', price: '' });

//提交表单
const submitForm = () => {
    if (validateForm()) {
        const productToSend = { ...product.value };

        store.dispatch('addProduct', productToSend);
        store.dispatch('fetchProducts');

        showSuccessDialog('添加成功');
        resetForm();
        closeModal();
    }
};

// 表单验证
const validateForm = () => {
    if (!product.value.name || product.value.name.length < 2 || product.value.name.length > 50) {
        showAlertDialog('名称必须在2到50个字符之间');
        return false;
    }
    if(!product.value.description || product.value.description.length < 2 || product.value.description.length > 100) {
        showAlertDialog('产品简介必须在2到100个字符之间');
        return false;
    }
    if(!product.value.price || product.value.price.length < 1 || product.value.price.length > 100) {
        showAlertDialog('价格必须在1到100个字符之间');
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

//控制弹窗开与关
const showModal = () => {
    show.value = true;
};

const closeModal = () => {
    show.value = false;
};

//清空表单
const resetForm = () => {
    product.value = { id: null, name: '', description: '', price: '' };
};
</script>