<template>
    <div class="h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>create products</h1>
      <!-- search -->
      <FormProducts :form-data="productForm" @submit-form="submitForm" />
    </div>
</template>

<script>
import axios from 'axios';
import FormProducts from '../../components/FormProducts.vue'

export default {
  components: {
    FormProducts,
  },
  data() {
    return {
        productForm: {
            title: '',
            description: '',
            price: null,
            image: ''
        },
    };
  },
  methods: {
    async submitForm(newProduct) {
      const BearerToken = localStorage.getItem('token');      
      this.productForm = newProduct;
      try {
        const {data} = await axios.post('http://localhost:8081/products/create', this.productForm, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        })
        this.productForm = {
            title: '',
            description: '',
            price: null,
            image: ''
        }
        this.$router.push('/admin')
      } catch (error) {
        console.error('Error submitting data:', error)
      }
    }
  },
  mounted() {

  },
};
</script>