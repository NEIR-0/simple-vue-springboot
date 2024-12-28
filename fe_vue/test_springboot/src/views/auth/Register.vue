<template>
    <div class="h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>Register</h1>
      <form @submit.prevent="submitForm" class="flex flex-col space-y-2">
        <input v-model="email" type="text" placeholder="Enter email" />
        <input v-model="password" type="text" placeholder="Enter password" />
        <button type="submit">Submit</button>
      </form>
    </div>
</template>


<script>
import apiMethods from '../../services/apiMothods';

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
        const body =  {
          email: this.email,
          password: this.password
        }
        const data = await apiMethods.postData("/auth/register", body);

        this.email = ''
        this.password = ''
        
        this.$router.push('/login')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    }
  }
}
</script>