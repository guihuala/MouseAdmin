import axios from '../../api/axios';

const state = {
    orders: [],
    allOrders: [],
    totalOrders: 0,
    foundOrder: [],
    pageSize: 10,
    currentPage: 1
};

const getters = {
    orders: state => state.orders,
    foundOrder: state => state.foundOrder
};

const actions = {
    // 获取全部订单信息
    async fetchOrders({ commit, state }) {
        const response = await axios.get('/api/orders/paged', {
            params: {
                page: state.currentPage,
                size: state.pageSize
            }
        });
        commit('setOrders', response.data);
    },
    async fetchAllOrders({ commit }) {
        try {
            const response = await axios.get('/api/orders');
            commit('setAllOrders', response.data);
        } catch (error) {
            console.error('获取订单数据失败', error);
            throw error;
        }
    },
    // 设置每页显示的条数
    async setoPageSize({ commit, dispatch }, pageSize) {
        commit('setoPageSize', pageSize);
        dispatch('fetchOrders');
    },
    // 设置当前页
    async setoCurrentPage({ commit, dispatch }, currentPage) {
        commit('setoCurrentPage', currentPage);
        dispatch('fetchOrders');
    },
    // 添加订单
    async addOrder({ commit, dispatch }, order) {
        const response = await axios.post('/api/orders', order); // 将数据添加到数据库中
        commit('newOrder', response.data);
        dispatch('fetchOrders');
        dispatch('fetchAllOrders');
    },
    // 删除订单
    async deleteOrder({ commit, dispatch }, id) {
        await axios.delete(`/api/orders/${id}`); // 删除数据库中的数据
        commit('removeOrder', id);
        dispatch('fetchAllOrders');
    },
    // 更新订单
    async updateOrder({ commit }, updatedOrder) {
        const response = await axios.put(`/api/orders/${updatedOrder.id}`, updatedOrder); // 更新数据库中的数据
        commit('updateOrder', response.data);

    },
    // 查找订单
    async findOrderById({ commit }, id) {
        const response = await axios.get(`/api/orders/${id}`); // 查找数据库中的数据
        commit('setFoundOrder', response.data);
    },
    async findOrderByNumber({ commit }, number) {
        const response = await axios.get(`/api/orders/orderNumber/${number}`);
        console.log(response.data);
        commit('setFoundOrder', response.data);
    },
    async findOrderByEmployeeId({ commit }, employeeId) {
        const response = await axios.get(`/api/orders/employeeId/${employeeId}`);
        commit('setFoundOrder', response.data);
    },
    async findOrderByCustomerId({ commit }, customerId) {
        const response = await axios.get(`/api/orders/customerId/${customerId}`);
        commit('setFoundOrder', response.data);
    },
    // 删除选中的订单
    async deleteSelectedOrders({ commit, dispatch }, ids) {
        await axios.delete('/api/orders/deleteSelected', { data: ids });
        commit('deleteSelected', ids);
        dispatch('fetchAllOrders');
    }
};

const mutations = {
    // 获取全部订单信息
    setOrders: (state, ordersData) => {
        state.orders = ordersData.records;
        state.totalOrders = ordersData.total;
    },
    setAllOrders: (state, ordersData) => {
        state.allOrders = ordersData;
    },
    setSearchOrders: (state, ordersData) => {
        state.orders = ordersData;
    },
    // 设置当前页
    setoCurrentPage: (state, currentPage) => (state.currentPage = currentPage),
    // 设置每页显示的条数
    setoPageSize: (state, pageSize) => (state.pageSize = pageSize),
    // 添加订单
    newOrder: (state, order) => state.orders.push(order),
    // 删除订单
    removeOrder: (state, id) => state.orders = state.orders.filter(order => order.id !== id),
    // 更新订单
    updateOrder: (state, updatedOrder) => {
        const index = state.orders.findIndex(order => order.id === updatedOrder.id);
        if (index !== -1) {
            state.orders.splice(index, 1, updatedOrder); // 替换掉原来的数据
        }
    },
    // 设置查找到的订单
    setFoundOrder: (state, order) => {
        state.foundOrder = order;
    },
    deleteSelected(state, ids) {
        state.orders = state.orders.filter(order => !ids.includes(order.id));
    }
};

export default {
    state,
    getters,
    actions,
    mutations,
};
