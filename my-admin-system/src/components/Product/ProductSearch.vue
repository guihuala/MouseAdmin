<template>
    <div class="p-4">
        <div class="form-control flex flex-row gap-4">
            <select class="select input-bordered" v-model="searchField">
                <option value="id">编号</option>
                <option value="name">名称</option>
            </select>
            <label class="input input-bordered flex items-center gap-2">
                <input type="text" class="grow" placeholder="按下回车搜索" v-model="search" @keyup.enter="performSearch" />
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" fill="currentColor"
                    class="w-4 h-4 opacity-70">
                    <path fill-rule="evenodd"
                        d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"
                        clip-rule="evenodd" />
                </svg>
            </label>
        </div>
    </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import { useStore } from 'vuex';

const store = useStore();

const searchField = ref('id'); // 搜索字段
const search = ref(''); // 搜索框的值
const searchResults = ref([]); // 搜索结果
const emit = defineEmits(['search-results']); // 定义事件

// 根据不同字段搜索产品
const searchProduct = async (field, value) => {
    if (!value) {
        emit('search-results', null);
        return;
    }
    
    switch (field) {
        case 'id':
            await store.dispatch('findProductById', value);
            searchResults.value = store.state.products.foundProduct;
            break;
        case 'name':
            await store.dispatch('findProductByName', value);
            searchResults.value = store.state.products.foundProduct;
            break;
        default:
            searchResults.value = null;
    }

    console.log(searchResults.value);
    emit('search-results', searchResults.value); // 触发事件
};

const performSearch = () => {
    searchProduct(searchField.value, search.value);
};
</script>