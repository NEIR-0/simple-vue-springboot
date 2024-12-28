<script setup>
import { useRoute } from 'vue-router'
import Navbar from './components/Navbar.vue'
const route = useRoute()
</script>

<template>
  <Navbar 
    v-if="!['/login', '/register', '/admin/create-product'].includes(route?.path) && !route?.path.startsWith('/admin/update-product/')" 
  />
  <router-view></router-view>
</template>
