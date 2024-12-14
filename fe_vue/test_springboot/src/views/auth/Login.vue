<template>
    <div class="h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>Login</h1>
      <form @submit.prevent="submitForm" class="flex flex-col space-y-2">
        <input v-model="email" type="text" placeholder="Enter email" />
        <input v-model="password" type="text" placeholder="Enter password" />
        <button type="submit">Submit</button>
      </form>
      <router-link to="/register" class="mt-5">Register</router-link>
    </div>
</template>


<script>
import axios from 'axios'

export default {
  data() {
    return {
      email: '',
      password: ''
    }
  },
  methods: {
    async submitForm() {
      try {
        const {data} = await axios.post('http://localhost:8081/auth/login', {
          email: this.email,
          password: this.password
        })
        
        localStorage.setItem('token', data); 

        this.email = ''
        this.password = ''
        
        this.$router.push('/dashboard')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    }
  }
}
</script>