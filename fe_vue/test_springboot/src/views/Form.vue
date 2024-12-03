<template>
  <div class="mt-4 h-screen flex flex-col items-center justify-center bg-red-300">
    <h1>Submit Form</h1>
    <form @submit.prevent="submitForm">
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
      name: ''  // Menyimpan inputan pengguna
    }
  },
  methods: {
    async submitForm() {
      try {
        // Mengirim POST request ke API
        const response = await axios.post('http://localhost:8081/users', {
          name: this.name  // Mengirim data name ke API
        })
        
        console.log('Data berhasil dikirim:', response.data)
        this.name = ''  // Reset input setelah submit
        
        this.$router.push('/')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    }
  }
}
</script>