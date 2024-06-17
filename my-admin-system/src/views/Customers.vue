<template>
    <div>
        <div class="relative min-h-screen">          
            <navbar/>
            <div class="lg:overflow-hidden lg:w-5/6 pt-16">               
                <main class="p-4">
                    <sidebar class="invisible lg:visible"/>              
                    <CustomerList />
                </main>
                <myfooter/>
            </div>
            
        </div>
    </div>

</template>

<script setup>
import CustomerList from '../components/Customer/CustomerList.vue';
import navbar from '../components/navbar.vue';
import sidebar from '../components/Sidebar.vue';
import myfooter from '../components/Footer.vue';
</script>