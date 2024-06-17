import { createStore } from 'vuex';
import customers from './modules/customers';
import orders from './modules/orders';
import employees from './modules/employees';
import products from './modules/products';
import users from './modules/user';

const store = createStore({
  state: {
    isLoading: false,
    loadingTimeout: null // 添加 loadingTimeout 状态
  },
  mutations: {
    setLoading(state, isLoading) {
      state.isLoading = isLoading;
    },
    setLoadingTimeout(state, timeout) { // 添加 setLoadingTimeout mutation
      state.loadingTimeout = timeout;
    }
  },
  actions: {
    setLoading({ commit }, isLoading) {
      commit('setLoading', isLoading);
    },
    setLoadingTimeout({ commit }, timeout) {
      commit('setLoadingTimeout', timeout);
    }
  },
  modules: {
    customers,
    orders,
    employees,
    products,
    users
  },
});

export default store;
