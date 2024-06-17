<template>
    <div>
        <navbar />
        <div class="main-content">
            <div class="hero min-h-screen">
                <div class="hero-overlay bg-opacity-20"></div>
                <!-- 窗口 -->
                <div class="mockup-window border bg-base-300 lg:w-3/4 my-20 lg:h3/4">
                    <div class="flex justify-center px-4 py-20 bg-base-200">
                        <div class="hero-content flex-col lg:flex-row-reverse">
                            <div class="text-center lg:text-left lg:w-3/5">
                                <h1 class="text-5xl font-bold">欢迎登陆Mouse Admin!</h1>
                                <p class="py-6">Mouse admin是老鼠也能使用的简易订单管理系统。</p>
                            </div>
                            <div class="card shrink-0 w-full max-w-sm shadow-2xl bg-base-100">
                                <form class="card-body" @submit.prevent="login">
                                    <div class="form-control">
                                        <label class="label">
                                            <span class="label-text">用户名</span>
                                        </label>
                                        <input type="text" placeholder="用户名" class="input input-bordered"
                                            v-model="name" />
                                    </div>
                                    <div class="form-control">
                                        <label class="label">
                                            <span class="label-text">密码</span>
                                        </label>
                                        <input type="password" placeholder="密码" class="input input-bordered"
                                            v-model="password" />
                                        <label class="label">
                                            <register />
                                            <findPassword/>
                                        </label>
                                    </div>
                                    <div class="form-control mt-1">
                                        <button type="submit" class="btn btn-primary text-white">登陆</button>
                                    </div>
                                    <div v-if="errorMessage" class="text-red-500 mt-2">{{ errorMessage }}</div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <myfooter />
    </div>
</template>

<style>
.main-content {
    display: flex;
    justify-content: center;
}
</style>

<script setup>
import navbar from '../components/navbar.vue';
import myfooter from '../components/Footer.vue';
import register from '../components/User/register.vue';
import findPassword from '../components/User/findPassword.vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import { ref } from 'vue';


const router = useRouter();
const store = useStore();

const name = ref('');
const password = ref('');
const errorMessage = ref('');

const login = async() => {
    if (name.value.trim() === '' || password.value.trim() === '') {
        errorMessage.value = '用户名和密码不能为空。';
        return;
    }
    try {
        await store.dispatch('login', { username: name.value, password: password.value });
        router.push('/statistics');
    } catch (error) {
        errorMessage.value = '登录失败，请检查用户名和密码。';
    }
};
</script>