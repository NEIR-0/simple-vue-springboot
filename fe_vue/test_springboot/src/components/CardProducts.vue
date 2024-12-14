<template>
    <div class="w-full h-fit p-5 bg-white rounded-lg">
        <img :src="products?.image" :alt="products?.title" class="w-full h-[150px]">
        <div class="w-full mt-2 flex flex-col space-y-5 rounded-lg">
            <div class="w-full space-y-1 text-black">
                <h1 class="capitalize h-[30px] flex items-center overflow-y-scroll text-lg font-bold break-words">{{ products?.title }}</h1>
                <div class="w-full h-[50px] overflow-y-scroll">
                    <p class="text-xs break-words">
                        {{ products?.description }}
                    </p>
                </div>
            </div>
            <button v-if="isWalletConnected" @click="onBuy(products)" class="w-full h-10 rounded-md bg-green-300 text-white font-bold duration-300 ease-in-out hover:bg-green-500">BUY, {{ products?.price }} ETH</button>
            <button v-else="!isWalletConnected" @click="connectWallets" class="w-full h-10 rounded-md bg-green-300 text-white font-bold duration-300 ease-in-out hover:bg-green-500">Connect Wallet</button>
        </div>
    </div>
</template>

<script>
export default {
  props: {
    products: {
      type: Object,
      required: true,
    },
    onBuy: {
      type: Function,
      required: true,
    },
    isWalletConnected: {
      type: Boolean,
    },
    connectWallets: {
      type: Function,
      required: true,
    },
  },
};
</script>