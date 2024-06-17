<script setup>
import { RouterView } from 'vue-router'
import loading from './components/Loading.vue'
</script>

<template>
  <div id="app">
    <loading />
    <router-view />
  </div>
</template>

<style>
body {
  color: #2c3e50;
  margin: 0;
  padding: 0;
  overflow-x: hidden;
}
</style>
