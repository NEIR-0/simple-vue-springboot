<template>
  <div class="h-fit w-full flex flex-col items-center justify-center py-20 px-5 relative">
    <div class="absolute top-20 right-20">
      <router-link class="bg-red-200 rounded-md py-2 px-10 items-center justify-center" to="/admin/create-product">+ create product</router-link>
    </div>
    <h1 class="text-3xl font-semibold">List Products</h1>
    <div class="w-full h-14 px-7 flex items-center justify-start space-x-5">
      <div class="h-full w-[400px] bg-[#EAE3D8] flex items-center justify-center p-2 rounded-md">
        <div class="w-full">
          <input v-model="searchQuery" @input="onSearch" class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-3 rounded-md focus:ring-0 outline-none" type="text" placeholder="Search..." name="search" id="search">
        </div>
      </div>

      <div class="w-fit h-full bg-[#beaa89] flex items-center justify-start p-5 rounded-md space-x-4">
          <h1 class="text-white font-medium text-lg">Sort By:</h1>
          <select
            v-if="durationProducst?.length > 0"
            @change="selectFilterDuration"
            v-model="selectedDurationProducst"
            id="durations"
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-0 outline-none block w-fit p-2.5"
          >
            <option disabled value="">Choose durations</option>
            <option v-for="item in durationProducst">
              {{ item }}
            </option>
          </select>
          <h1 class="text-white font-medium text-lg">/ days</h1>
        </div>
    </div>
    <div v-if="products.length > 0" class="w-full h-fit flex flex-wrap justify-center gap-10 mt-5 py-10">
      <div v-for="product in products" :key="product.id">
        <CardProducts :products="product" :isAdmin="true" :navigateToUpdate="navigateToUpdate" :deleteProduct="deleteProduct" />
      </div>
    </div>
    <!-- Pagination Controls -->
    <div class="mt-4">
      <button v-if="currentPage > 0" @click="prevPage(currentPage - 1)" class="px-4 py-2 bg-blue-500 text-white rounded mr-2">Previous</button>
      <span>Page {{ currentPage + 1 }}</span>
      <button v-if="currentPage != totalPages - 1" @click="nextPage(currentPage + 1)" class="px-4 py-2 bg-blue-500 text-white rounded ml-2">Next</button>
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
      durationProducst: [],
      selectedDurationProducst: "",
    };
  },
  methods: {
    prevPage(page) {
      this.currentPage = page;
      this.fetchProducts(this.searchQuery, this.selectedDurationProducst);
    },
    nextPage(page) {
      this.currentPage = page;
      this.fetchProducts(this.searchQuery, this.selectedDurationProducst);
    },
    selectFilterDuration(event) {
      const selectedValue = event.target.value;
      this.selectedDurationProducst = selectedValue;
      this.currentPage = 0;
      this.fetchProducts(this.searchQuery, this.selectedDurationProducst);
    },
    onSearch() {
      this.currentPage = 0;
      this.fetchProducts(this.searchQuery, this.selectedDurationProducst);
    },
    async fetchProducts(query = '', duration = '') {
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          search: query,
          duration: duration
        }
        const response = await apiMethods.getData("/products/admin-site", params);
        this.products = response?.product;
        this.durationProducst = response?.durations
        this.totalPages = Math.ceil(response?.totalPages / this.pageSize);
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
      }
    },

    async deleteProduct(id=null) {
      try {
        const endpoint = '/products/' + id;
        const response = await apiMethods.deleteData(endpoint);
        this.updateDataDeleteProducts();
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
      }
    },

    navigateToUpdate(id=null) {
      this.$router.push('/admin/update-product/' + id);
    },

    updateDataDeleteProducts() {
        WebSocketService.UpdateDataDeleteProductsRealTime();
    },

    GetDataUpdateProducts(newMessage) {
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