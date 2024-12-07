<template>
    <div class="h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>About</h1>
      <!-- Users Table -->
      <div v-if="users.length > 0" class="w-full max-w-md mx-auto bg-white rounded shadow-md">
        <table class="table-auto w-full text-left border-collapse">
          <thead>
            <tr>
              <th class="border px-4 py-2">ID</th>
              <th class="border px-4 py-2">Email</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in users" :key="user.id">
              <td class="border px-4 py-2">{{ user.id }}</td>
              <td class="border px-4 py-2">{{ user.email }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- Pagination Controls -->
      <div class="mt-4">
        <button @click="prevPage(currentPage - 1)" class="px-4 py-2 bg-blue-500 text-white rounded mr-2">Previous</button>
        <span>Page {{ currentPage + 1 }}</span>
        <button @click="nextPage(currentPage + 1)" class="px-4 py-2 bg-blue-500 text-white rounded ml-2">Next</button>
      </div>
    </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      users: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,  // Default page size is 10
    }
  },
  methods: {
    // Function to fetch users based on the current page and size
    async fetchUsers() {
      console.log(this.currentPage, ">>>>>>");
      const BearerToken = localStorage.getItem('token');
      try {
        const { data } = await axios.get('http://localhost:8081/users', {
          params: {
            page: this.currentPage,
            size: this.pageSize,
          },
          headers: {
            'Content-Type': 'application/json',
            "Authorization": BearerToken
          },
        });
        console.log(data, "@>#>!@>#12");
        console.log(data?.length, "@>#>!@>#12");
        
        // Update users and pagination details
        this.users = data;
        // Set total pages based on the total number of users
        this.totalPages = Math.ceil(data.length / this.pageSize);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },
    
    // Change the page when Next/Previous buttons are clicked
    prevPage(page) {
      console.log("click?");
      console.log(page);
      this.currentPage = page;
      this.fetchUsers();  // Re-fetch data for the new page
    },

    nextPage(page) {
      console.log("click?");
      console.log(page);
      this.currentPage = page;
      this.fetchUsers();  // Re-fetch data for the new page
    },
  },
  mounted() {
    this.fetchUsers();  // Fetch users when component is mounted
  },
}
</script>