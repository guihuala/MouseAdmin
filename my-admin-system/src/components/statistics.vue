<template>
    <div class="hero rounded-3xl mb-4">
        <div class="hero-content flex-col lg:flex-row h-80 p-5">
            <img src="/mouse.png" alt="" class="w-1/4 animate-bounce">
            <div class="max-w-md hover:animate-pulse">
                <h1 class="text-4xl font-bold lg:text-6xl">欢迎进入MouseAdmin!</h1>
                <p class="py-6 text-xl">现在时间{{ year }}年{{ month }}月{{ day }}日 {{ hour }}时{{ minute }}分{{ second }}秒</p>
            </div>
        </div>
    </div>
    <div class="flex gap-4 lg:w-full justify-center">
        <ul class="menu menu-horizontal rounded-box">
            <li>
                <a>
                    <el-icon>
                        <User />
                    </el-icon>
                    <router-link to="/customers">客户</router-link>
                </a>
            </li>
            <li>
                <a>
                    <el-icon>
                        <Avatar />
                    </el-icon>
                    <router-link to="/employees">员工</router-link>
                </a>
            </li>
            <li>
                <a>
                    <el-icon>
                        <Tickets />
                    </el-icon>
                    <router-link to="/orders">订单</router-link>
                </a>
            </li>
            <li>
                <a>
                    <el-icon>
                        <ShoppingTrolley />
                    </el-icon>
                    <router-link to="/products">产品</router-link>
                </a>
            </li>
        </ul>
    </div>
    <div class="card lg:card-side p-4 gap-10">
        <div class="card-body items-center text-center w-full p-10 rounded-2xl bg-[#f3f4f6]">
            <v-chart :option="barChartOptions" theme="v-chart-theme" style="height: 400px; width: 100%;"></v-chart>
        </div>
        <div class="card-body items-center text-center w-full p-10 rounded-2xl bg-[#f3f4f6]">
            <v-chart :option="pieChartOptions" theme="v-chart-theme" style="height: 400px; width: 100%;"></v-chart>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import * as echarts from 'echarts/core';
import { BarChart, PieChart, ScatterChart, SankeyChart } from 'echarts/charts';
import { TitleComponent, TooltipComponent, GridComponent, LegendComponent } from 'echarts/components';
import { User, Avatar, Tickets, ShoppingTrolley, FolderAdd } from '@element-plus/icons-vue'
import { CanvasRenderer } from 'echarts/renderers';
import VChart from 'vue-echarts';
import { useStore } from 'vuex';

const store = useStore();

// 使用 ECharts 的组件
echarts.use([BarChart, PieChart, ScatterChart, SankeyChart, TitleComponent, TooltipComponent, GridComponent, LegendComponent, CanvasRenderer]);

// 注册主题
echarts.registerTheme('v-chart-theme', {
    color: ["#516b91", "#72bad4", "#f2cce8", "#93b7e3", "#a5e7f0", "#cbb0e3"]
});

// 图表选项
const pieChartOptions = ref({
    tooltip: { trigger: 'item' },
    legend: { top: '0%', left: 'center' },
    series: [{
        name: '总数据',
        type: 'pie',
        radius: ['40%', '80%'],
        avoidLabelOverlap: false,// 避免标签重叠
        padAngle: 5, // 扇区之间的间距
        itemStyle: {
            borderRadius: 10, // 设置扇区的圆角半径
        },
        label: {
            show: false,// 是否显示标签
            position: 'center'
        },
        data: [],
        emphasis: {
            label: {
                show: true,
                fontSize: '30',
                fontWeight: 'bold'
            }
        },
        labelLine: {
            show: true// 是否显示引导线
        }
    }]
});


const barChartOptions = ref({
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    xAxis: {
        type: 'category',
        data: [] // X轴数据
    },
    yAxis: {
        type: 'value'
    },
    series: [{
        name: '新增客户',
        type: 'bar',
        data: [], // Y轴数据
        itemStyle: {
            borderRadius: [30, 30, 0, 0] // 设置柱状图的圆角
        }
    }]
});


// 时间组件
const now = ref(new Date());

const updateNow = () => {
    now.value = new Date();
};

const year = computed(() => now.value.getFullYear());
const month = computed(() => now.value.getMonth() + 1); // 月份从0开始，需要加1
const day = computed(() => now.value.getDate());
const hour = computed(() => now.value.getHours());
const minute = computed(() => now.value.getMinutes());
const second = computed(() => now.value.getSeconds());

onMounted(() => {
    updateNow();
    const interval = setInterval(updateNow, 1000);// 每秒更新一次时间
    onUnmounted(() => clearInterval(interval));
});

// 获取前七天的日期
const getLastSevenDays = () => {
    const dates = [];
    for (let i = 6; i >= 0; i--) {
        const date = new Date();// 获取当前日期
        date.setDate(date.getDate() - i);// 获取前i天的日期
        dates.push(`${date.getMonth() + 1}/${date.getDate()}`);// 月份从0开始，需要加1
    }
    return dates;
};

// 获取前七天的客户数量
const getCustomerCountsForLastSevenDays = (customers) => {
    const counts = Array(7).fill(0);// 初始化为0
    const today = new Date();
    customers.forEach(customer => {// 遍历客户
        const customerDate = new Date(customer.createdAt);// 客户创建日期
        const diffDays = Math.floor((today - customerDate) / (1000 * 60 * 60 * 24));// 计算相差天数
        if (diffDays < 7) {
            counts[6 - diffDays]++;// 更新对应天数的客户数量
        }
    });
    return counts;
};

// 获取数据并更新图表
const loadData = async () => {
    await store.dispatch('fetchAllCustomers');
    await store.dispatch('fetchAllEmployees');
    await store.dispatch('fetchAllProducts');
    await store.dispatch('fetchAllOrders');

    const customers = store.state.customers.allcustomers;
    const employees = store.state.employees.allemployees;
    const products = store.state.products.allProducts;
    const orders = store.state.orders.allOrders;

    // 更新饼图数据
    pieChartOptions.value.series[0].data = [
        { value: customers.length, name: '客户' },
        { value: employees.length, name: '员工' },
        { value: products.length, name: '产品' },
        { value: orders.length, name: '订单' }
    ];

    // 更新柱形图数据
    barChartOptions.value.xAxis.data = getLastSevenDays();
    barChartOptions.value.series[0].data = getCustomerCountsForLastSevenDays(customers);
};

onMounted(() => {
    loadData();
});
</script>