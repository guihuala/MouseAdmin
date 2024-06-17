import axios from '../../api/axios';

const state = {
    customers: [],
    allcustomers: [],
    total: 0,
    foundCustomer: [],
    pagesize: 10,
    currentPage: 1
};

const getters = {
    customers: state => state.customers,
    foundCustomer: state => state.foundCustomer
};

const actions = {
    //获取全部客户信息
    async fetchCustomers({ commit, state }) {
        const response = await axios.get('/api/customers/paged', {
            params: {
                page: state.currentPage,
                size: state.pagesize
            }
        }
        );
        commit('setCustomers', response.data);
    },
    async fetchAllCustomers({ commit }) {
        try {
            const response = await axios.get('/api/customers');
            commit('setallcustomers', response.data);
        } catch (error) {
            console.error('获取客户数据失败', error);
            throw error;
        }

    },
    //设置每页显示的条数
    async setPagesize({ commit, dispatch }, pagesize) {
        commit('setPagesize', pagesize);
        dispatch('fetchCustomers');
    },
    //设置当前页
    async setCurrentPage({ commit, dispatch }, currentPage) {
        commit('setCurrentPage', currentPage);
        dispatch('fetchCustomers');
    },
    //添加客户
    async addCustomer({ commit, dispatch }, customer) {
        const response = await axios.post('/api/customers/post', customer);//将数据添加到数据库中
        commit('newCustomer', response.data);
        dispatch('fetchAllCustomers');
    },
    //删除客户
    async deleteCustomer({ commit, dispatch }, id) {
        await axios.delete(`/api/customers/${id}`);//删除数据库中的数据
        commit('removeCustomer', id);
        dispatch('fetchAllCustomers');
    },
    //更新客户
    async updateCustomer({ commit, dispatch }, updatedCustomer) {
        const response = await axios.put(`/api/customers/update/${updatedCustomer.id}`, updatedCustomer);//更新数据库中的数据
        commit('updateCustomer', response.data);
        dispatch('fetchAllCustomers');
    },
    //查找客户
    async findCustomerById({ commit }, id) {
        const response = await axios.get(`/api/customers/${id}`);//查找数据库中的数据
        commit('setFoundCustomer', response.data);
    },
    async findCustomerByName({ commit }, name) {
        const response = await axios.get(`/api/customers/name/${name}`);
        commit('setFoundCustomer', response.data);
    },
    async findCustomerByAddress({ commit }, address) {
        const response = await axios.get(`/api/customers/address/${address}`);
        commit('setFoundCustomer', response.data);
    },
    //删除选中的客户
    async deleteSelectedCustomers({ commit, dispatch }, ids) {
        await axios.delete('/api/customers/delete', { data: ids });
        commit('deleteSelected', ids);
        dispatch('fetchAllCustomers');
    }
};

const mutations = {
    //获取全部客户信息
    setCustomers: (state, customersData) => {
        state.customers = customersData.records;
        state.total = customersData.total;
    },
    setallcustomers: (state, customersData) => {
        state.allcustomers = customersData;
    },
    setSearchCustomers: (state, customersData) => {
        state.customers = customersData;
    },
    //设置当前页
    setCurrentPage: (state, currentPage) => (state.currentPage = currentPage),
    //设置每页显示的条数
    setPagesize: (state, pagesize) => (state.pagesize = pagesize),
    //添加客户
    newCustomer: (state, customer) => state.customers.push(customer),
    //删除客户
    removeCustomer: (state, id) => state.customers = state.customers.filter(customer => customer.id !== id),
    //更新客户
    updateCustomer: (state, updatedCustomer) => {
        const index = state.customers.findIndex(customer => customer.id === updatedCustomer.id);
        if (index !== -1) {
            state.customers.splice(index, 1, updatedCustomer);//替换掉原来的数据
        }
    },
    //设置查找到的客户
    setFoundCustomer: (state, customer) => {
        state.foundCustomer = customer;
    },
    deleteSelected(state, ids) {
        state.customers = state.customers.filter(customer => !ids.includes(customer.id));
    }
};

export default {
    state,
    getters,
    actions,
    mutations,
};
