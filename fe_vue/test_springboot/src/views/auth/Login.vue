<template>
    <div class="h-screen flex justify-between relative">
      <div class="absolute top-10 right-10 space-y-2">
        <button @click="this.$router.push('/')" class="px-10 py-3 flex items-center justify-center rounded-md font-bold text-slate-800 bg-[#EAE3D8] hover:bg-[#e2d7c5] duration-300 ease-in-out">
          GO TO SHOP <i class="ri-arrow-right-line text-xl ms-2"></i>
        </button>
        <div class="w-full h-0.5 flex items-center justify-end">
          <div class="w-1/2 h-full bg-black"></div>
        </div>
      </div>

      <div class="w-[60%] h-full flex flex-col justify-center items-center p-5 relative bgLogin">
        <div class="w-[50%] h-fit absolute top-10 -right-0 bg-white pe-10 py-3 rounded-s-lg border-y-2 border-s-2 border-[#e1d4be]">
          <h1 class="font-bold text-xl w-full text-right">Decentralized Subscription Marketplace</h1>
          <p class="font-light text-sm text-right">Securely Purchase Premium Accounts with Ethereum, Powered by Blockchain Technology</p>
        </div>

        <div class="w-full h-fit flex items-center justify-center flex-wrap gap-2">
          <CardImg title="netflix"/>
          <CardImg title="disney_hotstar" />
          <CardImg title="prime_video" />
          <CardImg title="spotify" />
          <CardImg title="youtube" />
        </div>

        <p class="absolute bottom-5 left-5 text-sm text-[#B76E58] font-light">© 2024 NEIR-0. All Rights Reserved.</p>
      </div>

      <div class="w-[40%] h-full bg-white flex items-center justify-center">
        <div class="w-1/2 h-fit flex flex-col items-center justify-center backdrop-blur-xl bg-white/30 rounded-md p-10 shadow-md border-[1px] border-slate-300">
          <h1 class="font-bold text-[40px] mb-6 underline">Sign In</h1>
          <form @submit.prevent="submitForm" class="flex flex-col space-y-4">
            <div class="w-full">
              <input v-model="email" type="email" placeholder="Enter email" class="h-10 font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none" />
            </div>
            <div class="w-full">
              <input v-model="password" type="password" placeholder="Enter password" class="h-10 font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none" />
            </div>
            <button class="w-full h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-[#ffde09] hover:bg-[#ffe74e] duration-300 ease-in-out" type="submit">Submit</button>
          </form>
          <p class="mt-5">
            Don't have an account?
            <router-link to="/register" class="mt-5 underline">Register</router-link>
          </p>
        </div>
      </div>
    </div>
</template>


<script>
import CardImg from '../../components/auth/cardImg.vue';
import apiMethods from '../../services/apiMothods';

export default {
  data() {
    return {
      email: '',
      password: ''
    }
  },
  components: {
    CardImg,
  },
  methods: {
    async submitForm() {
      try {
        const body =  {
          email: this.email,
          password: this.password
        }
        const data = await apiMethods.postData("/auth/login", body);
        
        localStorage.setItem('token', data?.msg); 
        localStorage.setItem('role', data?.status);

        this.email = ''
        this.password = ''
        
        this.$toast.open({
          message: "Login Success",
          type: 'success',
          duration: 3000,
          position: 'top-right'
        });
        this.$router.push('/')
      } catch (error) {
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