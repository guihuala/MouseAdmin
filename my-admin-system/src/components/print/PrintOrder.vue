<template>
    <button class="btn" @click="print"><el-icon style="font-size: 1.2rem;"><Printer /></el-icon></button>
    <div style="display: none;">
        <table id="fullOrderTable">
            <thead>
                <tr>
                    <th>编号</th>
                    <th>客户</th>
                    <th>负责员工</th>
                    <th>产品</th>
                    <th>数量</th>
                    <th>订单号</th>
                    <th>总金额</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="order in orders" :key="order.id">
                    <td>{{ order.id }}</td>
                    <td>{{ order.customerName }}</td>
                    <td>{{ order.employeeName }}</td>
                    <td>{{ order.productName }}</td>
                    <td>{{ order.qty }}</td>
                    <td>{{ order.orderNumber }}</td>
                    <td>{{ order.totalAmount }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { onMounted,computed } from 'vue';
import { useStore } from 'vuex';
import { Printer } from '@element-plus/icons-vue';

const store = useStore();
const orders = computed(() => {
    return store.state.orders.allOrders;
});

onMounted(() => {
    store.dispatch('fetchAllOrders');
});

const print = () => {
    const contentToPrint = document.getElementById('fullOrderTable');
    if (!contentToPrint) {
        console.error('Element with ID "fullOrderTable" not found.');
        return;
    }

    const iframe = document.createElement('iframe');
    iframe.style.position = 'absolute';
    iframe.style.width = '0';
    iframe.style.height = '0';
    iframe.style.border = 'none';
    document.body.appendChild(iframe);

    const doc = iframe.contentWindow.document;
    doc.open();
    doc.write(`
        <html>
        <head>
            <title>打印</title>
            <style>
                /* 在这里添加你希望在打印时应用的样式 */
                body {
                    font-family: Arial, sans-serif;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                }
                th, td {
                    border: 1px solid #ddd;
                    padding: 15px;
                }
                th {
                    background-color: #f2f2f2;
                    text-align: left;
                }
            </style>
        </head>
        <body>${contentToPrint.outerHTML}</body>
        </html>
    `);
    doc.close();

    iframe.contentWindow.focus();
    iframe.contentWindow.print();
    document.body.removeChild(iframe);
};
</script>