<template>
  <div class="h-fit w-full flex flex-col items-center justify-center py-14 px-5 relative">
    <div class="absolute top-16 right-20">
      <router-link class="bg-red-200 rounded-md py-2 px-10 items-center justify-center" to="/admin/create-product">+ create product</router-link>
    </div>
    <h1 class="text-3xl font-semibold">List Products</h1>
    <div v-if="products.length > 0" class="w-full h-fit flex flex-wrap justify-center gap-10 mt-5 py-10">
      <div v-for="product in products" :key="product.id">
        <CardProducts :products="product" :isAdmin="true" :navigateToUpdate="navigateToUpdate" :deleteProduct="deleteProduct" />
      </div>
    </div>
  </div>
</template>
  
<script>  
import WebSocketService from '../../services/WebSocketService';
import CardProducts from '../../components/CardProducts.vue'
import apiMethods from '../../services/apiMothods';

export default {
  components: {
    CardProducts,
  },
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
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          search: query,
        }
        const response = await apiMethods.getData("/products", params);
        this.products = response;
        this.totalPages = Math.ceil(response.length / this.pageSize);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    async deleteProduct(id=null) {
      try {
        const endpoint = '/products/' + id;
        const response = await apiMethods.deleteData(endpoint);
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