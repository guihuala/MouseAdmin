<template>
    <div v-if="isLoading">
        <div class="loading-canvas">
            <span class="loading loading-ball loading-xs"></span>
            <span class="loading loading-ball loading-sm"></span>
            <span class="loading loading-ball loading-md"></span>
            <span class="loading loading-ball loading-lg"></span>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';

const store = useStore();
const isLoading = computed(() => store.state.isLoading);
</script>

<style scoped>
.loading-canvas {
    background-color: rgb(255, 255, 255);
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 9999;
    display: flex;
    justify-content: center;
    align-items: center;
    animation: opacity 0.7s ease-in-out;
}

@keyframes opacity {
    0% {
        opacity: 0.8;
    }

    50% {
        opacity: 0.5;
    }

    100% {
        opacity: 0;
    }
}
</style>