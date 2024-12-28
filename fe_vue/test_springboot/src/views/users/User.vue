<template>
    <div class="h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>Users</h1>
      <!-- search -->
      <input
        type="text"
        v-model="searchQuery"
        @input="onSearch"
        placeholder="Search by email..."
        class="mb-4 px-4 py-2 border rounded"
      />

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
              <td class="border px-4 py-2">{{ user.role }}</td>
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
import apiMethods from '../../services/apiMothods';

export default {
  data() {
    return {
      users: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,
      searchQuery: '',
    };
  },
  methods: {
    async fetchUsers(query = '') {
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          search: query,
        }
        const data = await apiMethods.getData("/users", params);

        this.users = data;
        this.totalPages = Math.ceil(data.length / this.pageSize);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },
    prevPage(page) {
      this.currentPage = page;
      this.fetchUsers(this.searchQuery);
    },
    nextPage(page) {
      this.currentPage = page;
      this.fetchUsers(this.searchQuery);
    },
    onSearch() {
      this.currentPage = 0;
      this.fetchUsers(this.searchQuery);
    },
  },
  mounted() {
    this.fetchUsers();
  },
};
</script>