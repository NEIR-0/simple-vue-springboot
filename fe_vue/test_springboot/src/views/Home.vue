<template>
    <div class="mt-4 h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>Data from API</h1>
      <ul>
        <li v-for="(item, index) in items" :key="item.id">
          {{index + 1}}. {{ item.name }}
          <router-link :to="'/update/' + item.id">
            <button class="w-20 h-8 bg-yellow-500 text-white">update</button>
          </router-link>
          <button @click="deleteItem(item.id)" class="w-20 h-8 bg-red-500 text-white">Delete</button>
        </li>
      </ul>
    </div>
</template>
  
<script>
  import axios from 'axios'
  
  export default {
    data() {
      return {
        items: []  // Untuk menyimpan data dari API
      }
    },
    mounted() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        console.log("mauks sini?");
        
        try {
          // Ganti dengan URL API yang sesuai
          const {data: response} = await axios.get('http://localhost:8081/users')  
          this.items = response  // Menyimpan data yang didapat ke dalam `items`
        } catch (error) {
          console.error('Error fetching data:', error)
        }
      },

      async deleteItem(id) {
        try {
          // Mengirim DELETE request ke API
          await axios.delete(`http://localhost:8081/users/${id}`)
          
          // Setelah berhasil menghapus, ambil ulang data
          this.fetchData()
          console.log(`Item dengan ID ${id} berhasil dihapus`)
        } catch (error) {
          console.error('Error deleting item:', error)
        }
      }
    }
  }
  </script>