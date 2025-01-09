<script setup>
import { useRoute, useRouter } from 'vue-router'
const token = localStorage.getItem('token');
const role = localStorage.getItem('role');
const route = useRoute();
const router = useRouter();
const handleLogout = async () => {
  localStorage.clear();
  router.push('/login');
}
</script>

<template>
    <nav :class="`${route?.path.startsWith('/admin') || route?.path.startsWith('/about') || route?.path.startsWith('/token') ? 'bg-slate-600' : 'bg-white/40'} w-full fixed h-10 flex items-center justify-center backdrop-blur-xl z-50`">
      <div class="w-[70%] h-full flex items-center justify-center">
        <router-link class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/">Shop</router-link>
        <router-link class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/about">About</router-link>
        <router-link class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/wallet">Wallet</router-link>
        <router-link v-if="!token" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/login">Login</router-link>
        <router-link v-if="token && role === 'admin'" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/token">Token</router-link>
        <router-link v-if="token && role === 'admin'" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/admin">admin</router-link>
        <router-link v-if="token && role === 'admin'" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/admin/customer">customer</router-link>
        <button v-if="token" @click="handleLogout" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800">logout</button>
      </div>
    </nav>
</template>