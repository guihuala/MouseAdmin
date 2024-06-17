<template>
    <button class="btn" @click="print"><el-icon style="font-size: 1.2rem;"><Printer /></el-icon></button>
    <div style="display: none;">
        <table id="fullEmployeeTable">
            <thead>
                <tr>
                    <th>编号</th>
                    <th>名称</th>
                    <th>邮箱</th>
                    <th>职位</th>
                    <th>工资</th>
                    <th>入职日期</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employees" :key="employee.id">
                    <td>{{ employee.id }}</td>
                    <td>{{ employee.name }}</td>
                    <td>{{ employee.email }}</td>
                    <td>{{ employee.position }}</td>
                    <td>{{ employee.salary }}</td>
                    <td>{{ employee.hireDate }}</td>
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
const employees = computed(() => {
    return store.state.employees.allemployees;
});

onMounted(() => {
    store.dispatch('fetchAllEmployees');
});

const print = () => {
    const contentToPrint = document.getElementById('fullEmployeeTable');
    if (!contentToPrint) {
        console.error('Element with ID "fullEmployeeTable" not found.');
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