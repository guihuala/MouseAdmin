import axios from '../../api/axios';

const state = {
    products: [],
    allProducts: [],
    totalProducts: 0,
    foundProduct: [],
    pageSize: 10,
    currentPage: 1
};

const getters = {
    products: state => state.products,
    foundProduct: state => state.foundProduct
};

const actions = {
    // 获取全部产品信息
    async fetchProducts({ commit, state }) {
        const response = await axios.get('/api/products/paged', {
            params: {
                page: state.currentPage,
                size: state.pageSize
            }
        });
        commit('setProducts', response.data);
    },
    async fetchAllProducts({ commit }) {
        try {
            const response = await axios.get('/api/products');
            commit('setAllProducts', response.data);
        } catch (error) {
            console.error('获取产品数据失败', error);
            throw error;
        }

    },
    // 设置每页显示的条数
    async setpPageSize({ commit, dispatch }, pageSize) {
        commit('setpPageSize', pageSize);
        dispatch('fetchProducts');
    },
    // 设置当前页
    async setpCurrentPage({ commit, dispatch }, currentPage) {
        commit('setpCurrentPage', currentPage);
        dispatch('fetchProducts');
    },
    // 添加产品
    async addProduct({ commit, dispatch }, product) {
        const response = await axios.post('/api/products', product); // 将数据添加到数据库中
        commit('newProduct', response.data);
        dispatch('fetchAllProducts');
    },
    // 删除产品
    async deleteProduct({ commit, dispatch }, id) {
        await axios.delete(`/api/products/${id}`); // 删除数据库中的数据
        commit('removeProduct', id);
        dispatch('fetchAllProducts');
    },
    // 更新产品
    async updateProduct({ commit }, updatedProduct) {
        const response = await axios.put(`/api/products/${updatedProduct.id}`, updatedProduct); // 更新数据库中的数据
        commit('updateProduct', response.data);
    },
    // 查找产品
    async findProductById({ commit }, id) {
        const response = await axios.get(`/api/products/${id}`); // 查找数据库中的数据
        commit('setFoundProduct', response.data);
    },
    async findProductByName({ commit }, name) {
        const response = await axios.get(`/api/products/name/${name}`);
        commit('setFoundProduct', response.data);
    },
    // 删除选中的产品
    async deleteSelectedProducts({ commit, dispatch }, ids) {
        await axios.delete('/api/products', { data: ids });
        commit('deleteSelected', ids);
        dispatch('fetchAllProducts');
    }
};

const mutations = {
    // 获取全部产品信息
    setProducts: (state, productsData) => {
        state.products = productsData.records;
        state.totalProducts = productsData.total;
    },
    setAllProducts: (state, productsData) => {
        state.allProducts = productsData;
    },
    setSearchProducts: (state, productsData) => {
        state.products = productsData;
    },
    // 设置当前页
    setpCurrentPage: (state, currentPage) => (state.currentPage = currentPage),
    // 设置每页显示的条数
    setpPageSize: (state, pageSize) => (state.pageSize = pageSize),
    // 添加产品
    newProduct: (state, product) => state.products.push(product),
    // 删除产品
    removeProduct: (state, id) => state.products = state.products.filter(product => product.id !== id),
    // 更新产品
    updateProduct: (state, updatedProduct) => {
        const index = state.products.findIndex(product => product.id === updatedProduct.id);
        if (index !== -1) {
            state.products.splice(index, 1, updatedProduct); // 替换掉原来的数据
        }
    },
    // 设置查找到的产品
    setFoundProduct: (state, product) => {
        state.foundProduct = product;
    },
    deleteSelected(state, ids) {
        state.products = state.products.filter(product => !ids.includes(product.id));
    }
};

export default {
    state,
    getters,
    actions,
    mutations,
};