<template>
    <nav>
        <router-link to="/home">Home</router-link> |
        <router-link to="/home/form">Form</router-link>
        <button @click="navigate" class="ml-5">navigate</button>
        <button @click="navigateHome" class="ml-5">navigateHome</button>


        <form @submit.prevent="navigateWithForm">
            <button type="submit">Submit</button>
        </form>
    </nav>
</template>

<script>
export default {
  methods: {
    async navigate() {
      try {
        console.log("masuk sini");
        
        this.$router.push('/home/about')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    },
    async navigateHome() {
      try {
        console.log("masuk sini");
        
        this.$router.push('/home')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    },
    async navigateWithForm() {
      try {
        this.$router.push('/home/about')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    }
  }
}
</script>