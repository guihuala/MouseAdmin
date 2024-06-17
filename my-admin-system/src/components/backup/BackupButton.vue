<template>
    <div>
        <button @click="showModal = true">备份</button>
    </div>

    <div v-if="showModal"  class="modal modal-open modal-bottom sm:modal-middle">
        <div class="modal-box">
            <h2 class="text-xl font-bold mb-4 text-neutral-content">备份数据库</h2>
            <p class="text-neutral-content">确定要备份数据库吗？</p>
            <div class="flex justify-end space-x-4">
                <button @click="showModal = false" class="btn btn-secondary text-white">取消</button>
                <button @click="backupDatabase" class="btn btn-primary text-white">确定</button>
            </div>
        </div>
        <form method="dialog" class="modal-backdrop">
            <button @click="showModal = false"></button>
        </form>
    </div>
</template>

<script setup>
import axios from '../../api/axios';
import { ref } from 'vue';

const showModal = ref(false);

const backupDatabase = async () => {
    try {
        const response = await axios.get('/api/backup', {
            responseType: 'blob' // 设置响应类型为二进制
        });
        const url = window.URL.createObjectURL(new Blob([response.data])); // 创建一个url
        const link = document.createElement('a'); // 创建一个a标签
        link.href = url; // 设置a标签的url
        link.setAttribute('download', 'MouseAdmin备份.sql'); // 设置下载的文件名
        document.body.appendChild(link); // 将a标签添加到body中
        link.click(); // 模拟点击a标签
        showModal.value = false; // 关闭模态框
    } catch (error) {
        console.error('Backup failed', error); // 打印错误信息
    }
}
</script>