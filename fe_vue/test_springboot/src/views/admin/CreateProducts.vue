<template>
    <div class="h-screen flex flex-col items-center justify-center relative">
      <router-link to="/admin" class="flex items-center space-x-2 px-4 py-2 bg-gray-200 rounded-md absolute top-10 left-10">
        <i class="ri-arrow-left-line text-xl"></i>
        <span>Back</span>
      </router-link>
      <!-- search -->
      <FormProducts :imagesList="imagesList" :form-data="productForm" @submit-form="submitForm" />
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
            files: [],
            imageStoreName: "",
            duration: null,
            stock: null
        },
        imagesList: []
    };
  },
  methods: {
    async submitForm(newProduct) {
      const BearerToken = localStorage.getItem('token');
      this.productForm = newProduct;
      const formData = new FormData();
      formData.append('title', this.productForm.title);
      formData.append('description', this.productForm.description);
      formData.append('price', this.productForm.price);
      formData.append('duration', this.productForm.duration);
      formData.append('stock', this.productForm.stock);

      // Append the file directly instead of base64
      if (this.productForm.files.length > 0) {
        formData.append('image', this.productForm.files[0]);
      } else {
        // Append individual properties of imageStore, not the whole object
        formData.append('imageStoreName', this.productForm.imageStoreName);
      }
      // Send form data to the backend
      this.submitData(formData, BearerToken);
    },

    async submitData(formData, BearerToken) {
      try {
        const config = {
          headers: {
            'Authorization': `${BearerToken}`,
            'Content-Type': 'multipart/form-data',  // Gunakan multipart/form-data
          }
        };

        const {data} = await axios.post('http://localhost:8081/products/create', formData, config);
        
        this.productForm = {  // Reset form
          title: '',
          description: '',
          price: null,
          files: [],
          imageStoreName: "",
        };
        this.$router.push('/admin');
      } catch (error) {
        console.error('Error submitting data:', error);
      }
    },

    async fetchAllImages() {
      const BearerToken = localStorage.getItem('token');
      try {
        const { data } = await axios.get('http://localhost:8081/image-store', {
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });

        this.imagesList = data
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },
  },
  mounted() {
    this.fetchAllImages()
  },
};
</script>