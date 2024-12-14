<template>
  <div class="h-screen flex flex-col items-center justify-center bg-red-300">
    <h1>Dashboard</h1>
    <router-link to="/dashboard/about">About</router-link>
    <router-link to="/dashboard/wallet">Wallet</router-link>
    <router-link to="/dashboard/etalase">Etalase</router-link>
    <router-link to="/admin/">Shop</router-link>
    <button class="mt-5" @click="handleLogout">logout</button>
  </div>
</template>

<script>

export default {
  methods: {
    async handleLogout() {
      localStorage.removeItem('token'); 
      this.$router.push('/login')
    }
  }
}
</script>