<template>
     <div class="h-screen flex flex-col items-center justify-center relative">
      <router-link to="/admin" class="flex items-center space-x-2 px-4 py-2 bg-gray-200 rounded-md absolute top-10 left-10">
        <i class="ri-arrow-left-line text-xl"></i>
        <span>Back</span>
      </router-link>
      <!-- search -->
      <FormProducts :form-data="productForm" :imagesList="imagesList" :isUpdate="true" @submit-form="submitUpdateForm" />
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
            stock: null,
            image: "",
        },
        imagesList: []
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
          duration: data.duration,
          stock: data.stock,
        };
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },
    async submitUpdateForm(newProduct) {
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
      } 
      else if (this.productForm.imageStoreName != "") {
        formData.append('imageStoreName', this.productForm.imageStoreName);
      }
      else {
        formData.append('prevImage', this.productForm.image);
      }
      // Send form data to the backend
      this.submitData(formData, BearerToken);
    },

    async submitData(formData, BearerToken) {
      const productId = this.$route.params.id;
      try {
        const config = {
          headers: {
            'Authorization': `${BearerToken}`,
            'Content-Type': 'multipart/form-data',  // Gunakan multipart/form-data
          }
        };

        const {data} = await axios.put('http://localhost:8081/products/update/' + productId, formData, config);
        
        this.productForm = {  // Reset form
          title: '',
          description: '',
          price: null,
          files: [],
          imageStoreName: "",
          duration: null,
          stock: null,
          image: "",
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
    this.fetchProductsById()
    this.fetchAllImages()
  },
};
</script>