<template>
  <div class="h-screen flex flex-col items-center justify-center bg-red-300">
    <h1>Dashboard</h1>
    <router-link to="/dashboard/about">About</router-link>

    <button @click="handleLogout">logout</button>
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