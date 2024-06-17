import axios from '../../api/axios';

const state = {
    employees: [],
    allemployees: [],
    total: 0,
    foundEmployee: [],
    pageSize: 10,
    currentPage: 1
};

const getters = {
    employees: state => state.employees,
    foundEmployee: state => state.foundEmployee
};

const actions = {
    // 获取全部员工信息
    async fetchEmployees({ commit, state }) {
        const response = await axios.get('/api/employees/paged', {
            params: {
                page: state.currentPage,
                size: state.pageSize
            }
        });
        commit('setEmployees', response.data);
    },
    async fetchAllEmployees({ commit }) {
        try {
            const response = await axios.get('/api/employees');
            commit('setallemployees', response.data);
        } catch (error) {
            console.error('获取员工数据失败', error);
            throw error;
        }

    },
    // 设置每页显示的条数
    async setePageSize({ commit, dispatch }, pageSize) {
        commit('setePageSize', pageSize);
        dispatch('fetchEmployees');
    },
    // 设置当前页
    async seteCurrentPage({ commit, dispatch }, currentPage) {
        commit('seteCurrentPage', currentPage);
        dispatch('fetchEmployees');
    },
    // 添加员工
    async addEmployee({ commit, dispatch }, employee) {
        const response = await axios.post('/api/employees', employee); // 将数据添加到数据库中
        commit('newEmployee', response.data);
        dispatch('fetchAllEmployees');
    },
    // 删除员工
    async deleteEmployee({ commit, dispatch }, id) {
        await axios.delete(`/api/employees/${id}`); // 删除数据库中的数据
        commit('removeEmployee', id);
        dispatch('fetchAllEmployees');
    },
    // 更新员工
    async updateEmployee({ commit, dispatch }, updatedEmployee) {
        const response = await axios.put(`/api/employees/${updatedEmployee.id}`, updatedEmployee); // 更新数据库中的数据
        commit('updateEmployee', response.data);
        dispatch('fetchAllEmployees');
    },
    // 查找员工
    async findEmployeeById({ commit }, id) {
        const response = await axios.get(`/api/employees/${id}`); // 查找数据库中的数据
        commit('setFoundEmployee', response.data);
    },
    async findEmployeeByName({ commit }, name) {
        const response = await axios.get(`/api/employees/name/${name}`);
        commit('setFoundEmployee', response.data);
    },
    async findEmployeeByPosition({ commit }, position) {
        const response = await axios.get(`/api/employees/position/${position}`);
        commit('setFoundEmployee', response.data);
    },
    // 删除选中的员工
    async deleteSelectedEmployees({ commit, dispatch }, ids) {
        await axios.delete('/api/employees/deleteSelected', { data: ids });
        commit('deleteSelected', ids);
        dispatch('fetchAllEmployees');
    }
};

const mutations = {
    // 获取全部员工信息
    setEmployees: (state, employeesData) => {
        state.employees = employeesData.records;
        state.total = employeesData.total;
    },
    setallemployees: (state, employeesData) => {
        state.allemployees = employeesData;
    },
    setSearchEmployees: (state, employeesData) => {
        state.employees = employeesData;
    },
    // 设置当前页
    seteCurrentPage: (state, currentPage) => (state.currentPage = currentPage),
    // 设置每页显示的条数
    setePageSize: (state, pageSize) => (state.pageSize = pageSize),
    // 添加员工
    newEmployee: (state, employee) => state.employees.push(employee),
    // 删除员工
    removeEmployee: (state, id) => state.employees = state.employees.filter(employee => employee.id !== id),
    // 更新员工
    updateEmployee: (state, updatedEmployee) => {
        const index = state.employees.findIndex(employee => employee.id === updatedEmployee.id);
        if (index !== -1) {
            state.employees.splice(index, 1, updatedEmployee); // 替换掉原来的数据
        }
    },
    // 设置查找到的员工
    setFoundEmployee: (state, employee) => {
        state.foundEmployee = employee;
    },
    deleteSelected(state, ids) {
        state.employees = state.employees.filter(employee => !ids.includes(employee.id));
    }
};

export default {
    state,
    getters,
    actions,
    mutations,
};