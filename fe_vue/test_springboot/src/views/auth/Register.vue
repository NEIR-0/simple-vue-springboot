<template>
    <div class="h-screen flex flex-col items-center justify-center bgRegister">
      <div class="w-[500px] p-20 rounded-lg shadow-lg border-2 flex flex-col items-center justify-center bg-white/65 backdrop-blur-xl">
        <h1 class="text-3xl font-medium">Register</h1>
        <div class="h-1 w-1/2 bg-[#ffec72] mb-7 mt-3"></div>
        <form @submit.prevent="submitForm" class="flex flex-col w-full">
          <input v-model="email" type="text" class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none" placeholder="Enter email" />
          <input v-model="password" type="text" class="h-10 mt-3 font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none" placeholder="Enter password" />
          <button type="submit" class="w-full h-10 mt-6 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-[#ffde09] hover:bg-[#ffe74e] duration-300 ease-in-out">Submit</button>
        </form>
        <p class="mt-5">
          Already have an account?
          <router-link to="/login" class="mt-5 underline">Login</router-link>
        </p>
      </div>
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

        this.$toast.open({
          message: "User Successfully Register",
          type: 'success',
          duration: 3000,
          position: 'top-right'
        });
        
        this.$router.push('/login')
      } catch (error) {
        console.error('Error submitting data:', error)
        this.$toast.open({
          message: error?.message,
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
      }
    }
  }
}
</script>