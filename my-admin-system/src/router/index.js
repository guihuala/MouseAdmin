import { createRouter, createWebHistory } from 'vue-router';
import login from '../views/Home.vue';
import Statistics from '../views/Statistics.vue';
import Customers from '../views/Customers.vue';
import Orders from '../views/Orders.vue';
import Products from '../views/Products.vue';
import Employees from '../views/Employees.vue';
import Customer from '../views/Customer.vue';
import Employee from '../views/Employee.vue';
import Product from '../views/Product.vue';
import Order from '../views/Order.vue';
import User from '../views/User.vue';
import store from '../store/index';

const routes = [
  { path: '/login', component: login },
  { path: '/customers', component: Customers, meta: { requiresLoading: false } },
  { path: '/orders', component: Orders, meta: { requiresLoading: false } },
  { path: '/employees', component: Employees, meta: { requiresLoading: false } },
  { path: '/products', component: Products, meta: { requiresLoading: false } },
  { path: '/customer/:id', component: Customer, props: true, meta: { requiresLoading: true } },
  { path: '/employee/:id', component: Employee, props: true, meta: { requiresLoading: true } },
  { path: '/product/:id', component: Product, props: true, meta: { requiresLoading: true } },
  { path: '/order/:id', component: Order, props: true, meta: { requiresLoading: true } },
  { path: '/user/:id', component: User, props: true, meta: { requiresLoading: true } },
  { path: '/', component: Statistics, meta: { requiresLoading: true } },
  { path: '/:pathMatch(.*)*', redirect: '/' },// 重定向
];

const router = createRouter({
  history: createWebHistory(import.meta.env.VITE_BASE_URL),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('jwt_token');

  // 检查 token 是否存在
  if (token) {
    try {
      // 尝试获取当前用户信息，验证 token 是否有效
      await store.dispatch('fetchCurrentUser');
    } catch (error) {
      // 如果获取用户信息失败，说明 token 可能无效或过期
      console.error('Token 认证失败', error);
      localStorage.removeItem('jwt_token');
      store.commit('clearUser');
      return next('/login');
    }
  } else if (to.path !== '/login') {
    // 如果没有 token 且目标路由不是登录页面，则重定向到登录页面
    return next('/login');
  }

  // 如果目标路由需要加载动画，则设置动画
  if (to.matched.some(record => record.meta.requiresLoading)) {
    store.dispatch('setLoading', true);
    const timeout = setTimeout(() => {
      store.dispatch('setLoading', false);
    }, 700);
    store.dispatch('setLoadingTimeout', timeout); // 保存定时器
  }

  next();
});

export default router;
