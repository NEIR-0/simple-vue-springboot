<script>
import { useRoute } from 'vue-router';
import apiMethods from '../services/apiMothods';

export default {
  data() {
    return {
      token: localStorage.getItem('token'),
      role: localStorage.getItem('role'),
      notifications: [],
      userId: null,
      showNotif: false,
    };
  },
  methods: {
    async handleLogout() {
      localStorage.clear();
      this.$router.push('/login');
    },

    async dataNotif() {
      try {
        const response = await apiMethods.getData("/notification/unread");
        this.notifications = response;
      } catch (error) {
        this.notifications = [];
        console.log("ERROR: ", error);
      }
    },

    async updateReadNotif(id) {
      try {
        await apiMethods.putData("/notification/update-unread/" + id);
        this.dataNotif();
      } catch (error) {
        console.log("ERROR: ", error);
      }
    },

    getOrdinalWord(number) {
      const ordinals = ["first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth"];
      return ordinals[number - 1] || `${number}th`; // Mengembalikan "first", "second", dst. atau jika lebih dari 10, gunakan angka langsung
    },
  },
  computed: {
    route() {
      return useRoute(); // Getting the current route object
    },
  },
  mounted() {
    this.dataNotif();
  },
  watch: {
    'route.query.userId'() {
        this.dataNotif();
    },
  },
};
</script>

<template>
    <nav :class="`${route?.path.startsWith('/admin') || route?.path.startsWith('/about') || route?.path.startsWith('/token') ? 'bg-slate-600' : 'bg-white/40'} w-full fixed h-10 flex items-center justify-center backdrop-blur-xl z-50`">
      <div class="w-[70%] h-full flex items-center justify-center">
        <router-link class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/">Shop</router-link>
        <router-link class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/about">About</router-link>
        <router-link class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/wallet">Wallet</router-link>
        <router-link v-if="!token" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/login">Login</router-link>
        <router-link v-if="token" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/token">Token</router-link>
        <router-link v-if="token && role === 'admin'" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/admin">admin</router-link>
        <router-link v-if="token && role === 'admin'" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/admin/customer">customer</router-link>
        <button v-if="token" @click="handleLogout" class="h-full px-5 capitalize flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800">logout</button>
      </div>

      <button @click="showNotif = !showNotif" v-if="route?.path.startsWith('/token')" class="w-10 h-full absolute top-0 right-0 flex items-center justify-center">
        <div class="px-0.5 w-fit h-fit bg-red-500 absolute top-0 left-1">
          <p class="text-sm text-white">{{notifications?.length}}</p>
        </div>
        <i class="ri-notification-3-fill text-xl text-white"></i>
      </button>

      <div v-if="showNotif && notifications.length !== 0" class="w-1/3 max-h-[300px] bg-[#FBF5E5] shadow-md border-[1px] rounded-md absolute top-full right-0 flex flex-col items-center justify-start mt-5 overflow-y-scroll overflow-hidden">
        <div @click="updateReadNotif(notif?.id)" v-for="notif in notifications" class="w-full bg-white border-b-[1px] flex items-stretch justify-center flex-col p-5 space-y-3">
          <div class="w-full">
            <h1 class="capitalize">{{ 
              notif?.status === "create_token" ? "Success Create New Token" : 
              notif?.status === "minting_token" ? `Success Minting token on` :
              notif?.status === "withdraw_token" ? `Success Withdrawing token on` :
              notif?.status?.split("-")[0] === "burn_token" ? `Success ${getOrdinalWord(notif?.status.split("-")[1])} Burning on`
              : "" }} "{{notif?.token?.name}}"
            </h1>
          </div>
          <div class="w-full flex items-center justify-between">
            <p>status: {{notif?.token?.status}}</p>
            <p>{{ notif?.token?.createdAt ? new Date(notif.token.createdAt).toLocaleDateString('id-ID') : 'Invalid Date' }}</p>
          </div>
        </div>
      </div>
    </nav>
</template>