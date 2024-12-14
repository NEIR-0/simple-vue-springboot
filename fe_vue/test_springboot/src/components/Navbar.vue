<template>
    <nav class="w-full fixed h-10 flex items-center justify-center backdrop-blur-xl bg-white/40 z-50">
      <div class="w-[70%] h-full flex items-center justify-center">
        <router-link class="h-full px-5 flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/dashboard/about">About</router-link>
        <router-link class="h-full px-5 flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/dashboard/etalase">Shop</router-link>
        <router-link class="h-full px-5 flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/dashboard/wallet">Wallet</router-link>
        <router-link class="h-full px-5 flex items-center justify-center duration-300 ease-in-out text-white hover:bg-stone-50 hover:text-slate-800" to="/admin/">admin</router-link>
      </div>
    </nav>
</template>