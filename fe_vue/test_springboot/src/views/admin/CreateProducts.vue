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
import FormProducts from '../../components/FormProducts.vue'
import apiMethods from '../../services/apiMothods';

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
        const response = await apiMethods.postDataFormData("/products/create", formData);
        
        this.productForm = {  // Reset form
          title: '',
          description: '',
          price: null,
          files: [],
          imageStoreName: "",
        };
        this.$router.push('/admin');
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message) {
          this.$router.push('/login')
        }
      }
    },

    async fetchAllImages() {
      try {
        const response = await apiMethods.getData("/image-store");
        this.imagesList = response
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message) {
          this.$router.push('/login')
        }
      }
    },
  },
  mounted() {
    this.fetchAllImages()
  },
};
</script>