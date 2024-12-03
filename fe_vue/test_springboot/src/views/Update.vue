<template>
  <div class="mt-4 h-screen flex flex-col items-center justify-center bg-red-300">
    <h1>Submit Form</h1>
    <form @submit.prevent="submitFormUpdate">
      <input v-model="name" type="text" placeholder="Enter name" />
      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      userId: this.$route.params.id,
      name: '',  // Menyimpan inputan pengguna 
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async submitFormUpdate() {
      try {
        // Mengirim POST request ke API
        const response = await axios.put('http://localhost:8081/users/' + this.userId, {
          name: this.name  // Mengirim data name ke API
        })
        
        console.log('Data berhasil dikirim:', response.data)
        this.name = ''  // Reset input setelah submit
        
        this.$router.push('/')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    },

    async fetchData() {
      try {
        // Ganti dengan URL API yang sesuai
        const {data: response} = await axios.get('http://localhost:8081/users/' + this.userId)  
        console.log(response);
        this.name = response?.name  // Menyimpan data yang didapat ke dalam `items`
      } catch (error) {
        console.error('Error fetching data:', error)
      }
    }
  }
}
</script>