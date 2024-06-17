<template>
    <div>
        <nav class="p-4 flex justify-between items-center h-1/8">
            <div class="flex gap-2 content-center">
                <ProductForm/>
                <button class="btn bg-neutral text-white" @click="toggleSelectAll">
                    {{ isChecked ? '取消全选' : '全选本页' }}
                </button>
                <button class="btn bg-secondary text-white" @click="deleteSelectedProducts">多选删除</button>
                <printProduct/>
            </div>
            <div class="hidden sm:flex space-x-4">
                <label class="flex cursor-pointer gap-2 content-center">
                    <productrSearch @search-results="handleSearchResults" />
                </label>
            </div>
        </nav>
        <div class="lg:hidden sm:flex space-x-4">
                <label class="flex cursor-pointer gap-2 content-center">
                    <productrSearch @search-results="handleSearchResults" />
                </label>
            </div>
        <div class="overflow-x-auto bg-base-200 rounded-lg">
            <table class="table table-pin-rows table-pin-cols">
                <thead>
                    <tr>
                        <th></th>
                        <th>编号</th>
                        <th>名称</th>
                        <th>价格</th>
                        <th>商品简介</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr
                    v-for="(product, index) in products"
                    :key="product.id"
                    class="list-body"
                    :draggable="true"
                    @dragstart="onDragStart(index)"
                    @dragover.prevent
                    @drop="onDrop(index)"
                    >
                        <th>
                            <label>
                                <input type="checkbox" class="checkbox" v-model="product.selected" />
                            </label>
                        </th>
                        <td>{{ product.id }}</td>
                        <td>{{ product.name }}</td>
                        <td>{{ product.price }}</td>
                        <td>{{ product.description }}</td>
                        <td>
                            <div class="flex gap-2">
                                <button class="btn bg-neutral text-white"
                                    @click="ProductDetails(product.id)">详情</button>
                                <button class="btn btn-square btn-outline" @click="deleteProduct(product.id)">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none"
                                        viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M6 18L18 6M6 6l12 12" />
                                    </svg>
                                </button>

                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div v-if="!products.length" class="flex justify-center items-center h-80">
                <p class="text-gray-500">暂无产品</p>
            </div>
        </div>
        <div class="flex mt-5 justify-center">
            <div class="join">
                <button class="join-item btn" @click="prevPage" :disabled="currentPage === 1">«</button>
                <button class="join-item btn">{{ currentPage }}</button>
                <button class="join-item btn" @click="nextPage" :disabled="currentPage === totalPages">»</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import ProductForm from './ProductForm.vue';
import productrSearch from './ProductSearch.vue';
import printProduct from '../print/PrintProduct.vue';

//引入store
const store = useStore();
const products = computed(() => {
    const productList = store.state.products.products;
    return Array.isArray(productList) ?
        productList.map(product => ({
            ...product,
            selected: false
        })) : [];//添加selected属性
});

//删除产品
const deleteProduct = (id) => {
    store.dispatch('deleteProduct', id);
};

//删除选中的产品
const deleteSelectedProducts = () => {
    const selectedProducts = products.value.filter(product => product.selected);
    const selectedIds = selectedProducts.map(product => product.id);
    store.dispatch('deleteSelectedProducts', selectedIds); 
};

//处理搜索结果
const handleSearchResults = (results) => {
    if (results) {
        const productsArray = Array.isArray(results) ? results : [results];
        store.commit('setSearchProducts', productsArray); 
    } else {
        store.dispatch('fetchProducts');
    }
};

//初始化产品数据
import { onMounted } from 'vue';

onMounted(() => {
    store.dispatch('fetchProducts');
});

//查看产品详情
const router = useRouter();

const ProductDetails = (id) => {
    router.push({ path: `/product/${id}` });
};

// 定义全选状态
const isChecked = ref(false);

const toggleSelectAll = () => {
    isChecked.value = !isChecked.value;
    products.value.forEach(product => {
        product.selected = isChecked.value;
    });
};

const disabledSelectAll = () => {
    if (isChecked.value) {
        isChecked.value = false;
        products.value.forEach(product => {
            product.selected = false;
        });
    }
};

//分页
const currentPage = computed(() => store.state.products.currentPage);
const pageSize = computed(() => store.state.products.pageSize);
const total = computed(() => store.state.products.totalProducts);

const totalPages = computed(() => Math.ceil(total.value / pageSize.value));

const setPage = (page) => {
    store.dispatch('setpCurrentPage', page);
};

const nextPage = () => {
    if (currentPage.value < totalPages.value) {       
        setPage(currentPage.value + 1);
        disabledSelectAll();
    }
};

const prevPage = () => {
    if (currentPage.value > 1) {
        setPage(currentPage.value - 1);
        disabledSelectAll();
    }
};

 // 拖拽功能实现
 let draggedIndex = null;
  
  const onDragStart = (index) => {
    draggedIndex = index;
  };
  
  const onDrop = (index) => {
    if (draggedIndex !== null) {
      const draggedItem = products.value[draggedIndex];
      products.value.splice(draggedIndex, 1);
      products.value.splice(index, 0, draggedItem);
      store.state.products.products = products.value;
      draggedIndex = null;
    }
  };
</script>