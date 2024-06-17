<template>
    <div class="p-4">
        <el-page-header :icon="ArrowLeft" @click="goBack">
            <template #content>
                <span class="text-large font-600 mr-3" v-if="product"> {{ product.name }}</span>
            </template>
        </el-page-header>
        <div v-if="product" class="my-5">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
            <div class="flex flex-col">
                <label for="name" class="mb-1">名称</label>
                <div class="input input-bordered flex items-center gap-2">
                    <el-icon>
                        <User />
                    </el-icon>
                    <input id="name" type="text" class="flex" placeholder="名称" v-model="product.name" :disabled="!isEditing" />
                </div>
            </div>
            <div class="flex flex-col">
                <label for="price" class="mb-1">价格</label>
                <div class="input input-bordered flex items-center gap-2">
                    <el-icon>
                        <Position />
                    </el-icon>
                    <input id="price" type="text" class="grow" placeholder="价格" v-model="product.price" :disabled="!isEditing" />
                </div>
            </div>
            <div class="flex flex-col col-span-1 md:col-span-2 lg:col-span-3">
                <label for="description" class="mb-1">描述</label>
                <textarea id="description" class="textarea textarea-bordered" placeholder="描述" v-model="product.description" :disabled="!isEditing"></textarea>
            </div>
        </div>
    </div>
        <div class="flex flex-row gap-5">
            <button class="btn bg-primary text-white" @click="toggleEdit">{{ isEditing ? '取消修改' : '编辑信息' }}</button>
            <button class="btn bg-accent text-accent-content" @click="editProduct" :disabled="!isEditing">提交</button>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ArrowLeft,User,Message,Position } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'

const router = useRouter()
const route = useRoute();
const productId = ref(route.params.id);

// 获取产品信息
const store = useStore();
const product = computed(() => store.state.products.foundProduct);

onMounted(() => {
    store.dispatch('findProductById', productId.value);
});

const isEditing = ref(false);

const toggleEdit = () => {
    isEditing.value = !isEditing.value;
    if (!isEditing.value) {
        // 如果取消编辑，重置产品信息
        store.dispatch('findProductById', productId.value);
    }
}

const editProduct = () => {
    store.dispatch('updateProduct', product.value);
    isEditing.value = false;
}

const goBack = () => {
    router.go(-1);
}
</script>