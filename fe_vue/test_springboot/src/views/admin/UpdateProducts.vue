<template>
    <div class="h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>create products</h1>
      <!-- search -->
      <FormProducts :form-data="productForm" @submit-form="submitUpdateForm" />
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
    async fetchProductsById() {
      const BearerToken = localStorage.getItem('token');
      const productId = this.$route.params.id;
      try {
        const { data } = await axios.get('http://localhost:8081/products/' + productId, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });

        this.productForm = {
          title: data.title,
          description: data.description,
          price: data.price,
          image: data.image,
        };
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },
    async submitUpdateForm(updatedForm) {
      const BearerToken = localStorage.getItem('token');
      const productId = this.$route.params.id;
      this.productForm = updatedForm;
      try {
        const {data} = await axios.put('http://localhost:8081/products/update/' + productId, this.productForm,{
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
    this.fetchProductsById()
  },
};
</script>