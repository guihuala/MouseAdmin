<template>
    <div class="max-w-4xl mx-auto p-6 bg-white rounded-2xl shadow-xl mt-10">
        <div class="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-6">
            <div class="flex justify-center">
                <el-image style="width: 120px; height: 120px;border-radius: 50%;" :src="user.avatar" :fit="fill" :preview-src-list="[user.avatar]"/>
            </div>
            <div>
                <h2 class="text-2xl font-bold text-gray-900">{{ user.username }}</h2>
                <p class="text-gray-600">{{ user.email }}</p>
                <div class="flex gap-2">
                    <button @click="showEditModal = true" class="btn my-5 btn-neutral text-white">
                        修改用户信息
                    </button>
                    <button @click="showConfirmModal = true" class="btn my-5 btn-error text-white">
                        注销账号
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改用户信息弹窗 -->
    <dialog v-if="showEditModal" class="modal modal-open modal-bottom sm:modal-middle">
        <div class="modal-box">
            <h2 class="text-xl font-bold mb-4">修改用户信息</h2>
            <form @submit.prevent="submitForm">
                <div class="mb-4">
                    <input type="file" id="avatar" @change="handleFileUpload"
                        class="file-input file-input-bordered w-full max-w-xs">
                </div>
                <div v-if="showCropper" class="mb-4">
                    <vue-cropper ref="cropperRef" :src="imageUrl" :aspect-ratio="1"
                        style="height: 400px; width: 100%;" />
                    <div class="flex justify-end mt-4">
                        <button type="button" @click="cropImage" class="btn btn-primary text-white">裁剪</button>
                    </div>
                </div>
                <div class="mb-4">
                    <input type="text" id="username" v-model="editUser.username"
                        class="input input-bordered w-full max-w-xs">
                </div>
                <div class="mb-4">
                    <input type="email" id="email" v-model="editUser.email"
                        class="input input-bordered w-full max-w-xs">
                </div>
                <div class="flex justify-end">
                    <button type="button" @click="showEditModal = false" class="btn btn-neutral mr-2 text-white">取消</button>
                    <button type="submit" class="btn btn-primary text-white">保存</button>
                </div>
            </form>
        </div>
        <form method="dialog" class="modal-backdrop">
            <button @click="showEditModal = false">关闭</button>
        </form>
    </dialog>

    <!-- 注销账号确认弹窗 -->
    <dialog v-if="showConfirmModal" class="modal modal-open modal-bottom sm:modal-middle">
        <div class="modal-box">
            <h2 class="text-xl font-bold mb-4">注销账号</h2>
            <p>确定要注销账号吗？</p>
            <div class="flex justify-end mt-4">
                <button type="button" @click="showConfirmModal = false"
                    class="btn btn-neutral mr-2 text-white">取消</button>
                <button type="button" @click="deleteUser" class="btn btn-error text-white">确定</button>
            </div>
        </div>
        <form method="dialog" class="modal-backdrop">
            <button @click="showConfirmModal = false">关闭</button>
        </form>
    </dialog>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import VueCropper from 'vue-cropperjs';
import 'cropperjs/dist/cropper.css';

const store = useStore();
const router = useRouter();
const user = computed(() => store.state.users.user);
const showEditModal = ref(false);
const showConfirmModal = ref(false);
const editUser = ref({ ...user.value });
const avatarFile = ref(null);
const imageUrl = ref('');
const showCropper = ref(false);
const cropperRef = ref(null);

const handleFileUpload = (event) => {
    const file = event.target.files[0];
    const maxSize = 10 * 1024 * 1024; // 10MB

    if (file.size > maxSize) {
        showAlertDialog('文件大小不能超过10MB');
        event.target.value = ''; // 清空文件输入框
        return;
    }

    const reader = new FileReader();
    reader.onload = (e) => {
        imageUrl.value = e.target.result;
        showCropper.value = true;
    };
    reader.readAsDataURL(file);
    avatarFile.value = file;
};

const cropImage = () => {
    const cropper = cropperRef.value.cropper;
    cropper.getCroppedCanvas().toBlob((blob) => {
        avatarFile.value = new File([blob], avatarFile.value.name, { type: avatarFile.value.type });
        showCropper.value = false;
    });
};

const submitForm = () => {
    if (validateForm()) {
        const userToSend = { ...editUser.value };

        const formData = new FormData();
        formData.append('username', userToSend.username);
        formData.append('email', userToSend.email);
        if (avatarFile.value) {
            formData.append('avatar', avatarFile.value);
        }

        store.dispatch('updateUser', { id: userToSend.id, formData })
            .then(() => {
                showSuccessDialog('用户信息更新成功');
                closeModal();
            })
            .catch(error => {
                console.error('更新用户信息失败', error);
                showAlertDialog('更新用户信息失败，请重试');
            });
    }
};

const deleteUser = () => {
    store.dispatch('deleteUser', user.value.id)
        .then(() => {
            showConfirmModal.value = false;
            showSuccessDialog('账号注销成功');
            router.push('/login'); // 跳转到登录页
        })
        .catch(error => {
            console.error('注销账号失败', error);
            showAlertDialog('注销账号失败，请重试');
        });
};

const validateForm = () => {
    if (!editUser.value.username || !validateName(editUser.value.username)) {
        showAlertDialog('名称必须在2到10个字符之间,且为字母或数字');
        return false;
    }
    if (!editUser.value.email || !validateEmail(editUser.value.email)) {
        showAlertDialog('请输入有效的邮箱地址');
        return false;
    }
    return true;
}

const validateName = (name) => {
    // 设定名称验证规则
    const re = /^[a-zA-Z0-9_-]{2,10}$/;
    // 返回验证结果
    return re.test(name);
};

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

const closeModal = () => {
    showEditModal.value = false;
}
</script>