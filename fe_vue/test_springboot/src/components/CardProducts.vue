<template>
    <div :class="`${isAdmin ? 'w-[300px] bg-orange-100' : 'w-full bg-white'} h-fit p-5 rounded-lg`">
        <img :src="`http://localhost:8081${products?.image}`" :alt="products?.title" class="w-full h-[150px]">
        <div class="w-full mt-2 flex flex-col space-y-5 rounded-lg">
            <div class="w-full space-y-1 text-black">
                <h1 class="capitalize h-[30px] flex items-center overflow-y-scroll text-lg font-bold break-words">{{ products?.title }}</h1>
                <div class="w-full flex items-center justify-end space-x-2">
                  <p class="text-xs break-words">
                      {{ products?.duration }} / Days
                  </p>
                  <p class="text-xs break-words">
                      stock: {{ products?.stock }}
                  </p>
                </div>
                <div class="w-full h-[50px] overflow-y-scroll">
                    <p class="text-xs break-words">
                        {{ products?.description }}
                    </p>
                </div>
            </div>
            <div v-if="isAdmin" class="w-full h-fit flex justify-between">
              <button @click="navigateToUpdate(products.id)" class="w-[48%] h-10 flex items-center justify-center bg-yellow-300">edit</button>
              <button @click="deleteProduct(products.id)" class="w-[48%] h-10 flex items-center justify-center bg-red-300">delete</button>
            </div>
            <div v-if="!isAdmin" class="w-full h-fit flex justify-between">
              <button v-if="isWalletConnected" @click="onBuy(products)" class="w-full h-10 rounded-md bg-green-300 text-white font-bold duration-300 ease-in-out hover:bg-green-500">BUY, {{ products?.price }} ETH</button>
              <button v-else="!isWalletConnected" @click="connectWallets" class="w-full h-10 rounded-md bg-green-300 text-white font-bold duration-300 ease-in-out hover:bg-green-500">Connect Wallet</button>
            </div>
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
    },
    isWalletConnected: {
      type: Boolean,
    },
    connectWallets: {
      type: Function,
    },
    isAdmin: {
      type: Boolean,
    },
    navigateToUpdate: {
      type: Function,
    },
    deleteProduct: {
      type: Function,
    },
  },
};
</script>