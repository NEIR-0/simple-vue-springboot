<template>
  <div class="h-screen flex flex-col items-center justify-center p-5 bg-red-300">
    <h1>List Products</h1>
    <router-link to="/admin/create-product">create product</router-link>

    <div v-if="products.length > 0" class="w-full h-fit flex flex-wrap justify-center gap-2 mt-20">
      <div v-for="product in products" :key="product.id">
        <div class="w-[300px] h-fit p-5 bg-white rounded-md">
          <img :src="product?.image" class="w-full h-[150px]" alt="">
          <div class="w-full mt-2 flex flex-col space-y-3">
            <h1>{{ product?.title }}</h1>
            <div class="w-full h-[50px] overflow-y-scroll">
              <p class="text-xs break-words">{{ product?.description }}</p>
            </div>
            <p class="text-xs">{{ product?.price }} ETH</p>
          </div>

          <div class="w-full flex justify-between mt-5">
            <button @click="navigateToUpdate(product.id)" class="w-[48%] h-10 flex items-center justify-center bg-yellow-300">edit</button>
            <button @click="deleteProduct(product.id)" class="w-[48%] h-10 flex items-center justify-center bg-red-300">delete</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>  
import axios from 'axios';
import WebSocketService from '../../services/WebSocketService';

export default {
  data() {
    return {
      products: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,
      searchQuery: '',
    };
  },
  methods: {
    async fetchProducts(query = '') {
      const BearerToken = localStorage.getItem('token');
      try {
        const { data } = await axios.get('http://localhost:8081/products', {
          params: {
            page: this.currentPage,
            size: this.pageSize,
            search: query,
          },
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });
        
        this.products = data;
        this.totalPages = Math.ceil(data.length / this.pageSize);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    async deleteProduct(id=null) {
      const BearerToken = localStorage.getItem('token');
      try {
        const { data } = await axios.delete('http://localhost:8081/products/' + id, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });
        console.log(data);
        this.updateDataDeleteProducts();
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    navigateToUpdate(id=null) {
      this.$router.push('/admin/update-product/' + id);
    },

    updateDataDeleteProducts() {
        WebSocketService.UpdateDataDeleteProductsRealTime();
    },

    GetDataUpdateProducts(newMessage) {
      console.log(newMessage, "?????");
      this.products = newMessage
    },
  },
  mounted() {
    this.fetchProducts()
    WebSocketService.responseUpdateDataDeleteProductsRealTime(this.GetDataUpdateProducts);
  },

  beforeDestroy() {
    WebSocketService.disconnect();
  },
}
</script>