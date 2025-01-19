<template>
    <div class="w-full h-fit flex items-center justify-center flex-col pt-36">
        <div v-if="token && role === 'admin'"  class="absolute top-20 right-20">
            <router-link class="bg-red-200 rounded-md py-2 px-10 items-center justify-center" to="/token/create">+ create token</router-link>
        </div>
        <div class="w-3/4 flex items-center justify-start space-x-4 mb-6">
            <button @click="sortingByCreated" class="px-7 py-2 rounded ml-2 border-[1px] text-sm capitalize font-medium duration-300 ease-in-out hover:bg-stone-300" :class="sort && 'bg-stone-600 text-white'">{{sort === "desc" ? "Lastet" : "oldest"}}</button>
            <button @click="sortingByPrice" class="px-7 py-2 rounded ml-2 border-[1px] text-sm capitalize font-medium duration-300 ease-in-out hover:bg-stone-300" :class="price && 'bg-stone-600 text-white'">{{price === "desc" ? "Highest Price" : "Lowest Price"}}</button>
            <button @click="sortingByProfit" class="px-7 py-2 rounded ml-2 border-[1px] text-sm capitalize font-medium duration-300 ease-in-out hover:bg-stone-300" :class="profit && 'bg-stone-600 text-white'">{{profit === "desc" ? "Highest Profit" : "Lowest Profit"}}</button>
        </div>
        <table class="table-auto border-collapse border border-gray-300 w-3/4">
            <thead class="bg-gray-200">
                <tr>
                    <th class="border border-gray-300 px-4 py-2">Name</th>
                    <th class="border border-gray-300 px-4 py-2">Symbol</th>
                    <th class="border border-gray-300 px-4 py-2">Token Price</th>
                    <th class="border border-gray-300 px-4 py-2">Profit (%)</th>
                    <th class="border border-gray-300 px-4 py-2">Status</th>
                    <th class="border border-gray-300 px-4 py-2">Total Supply</th>
                    <th class="border border-gray-300 px-4 py-2">Actions</th>
                </tr>
            </thead>
            <tbody>
                <!-- Iterasi data token -->
                <tr
                    v-if="tokens.length > 0"
                    v-for="(token, index) in tokens"
                    :key="index"
                >
                    <td class="border border-gray-300 px-4 py-2">{{ token.name }}</td>
                    <td class="border border-gray-300 px-4 py-2">{{ token.symbol }}</td>
                    <td class="border border-gray-300 px-4 py-2">{{ token.tokenPrice }}</td>
                    <td class="border border-gray-300 px-4 py-2">{{ token.profitPersen }}</td>
                    <td class="border border-gray-300 px-4 py-2">{{ token.status }}</td>
                    <td class="border border-gray-300 px-4 py-2">{{ token.totalSupply }}</td>
                    <td class="border border-gray-300 px-4 py-2 flex items-center justify-center space-x-4">
                        <router-link class="bg-blue-500 text-white font-medium capitalize rounded-md py-2 px-10 items-center justify-center" :to="`/token/detail/${token.addressToken}/${token.id}`">detail token</router-link>
                    </td>
                </tr>
                <!-- Jika tidak ada data -->
                <tr v-if="tokens.length === 0">
                    <td
                        colspan="6"
                        class="border border-gray-300 px-4 py-2 text-center"
                    >
                        No tokens available.
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- Pagination Controls -->
        <div class="w-full flex items-center justify-center mt-4 pb-10 space-x-5">
            <button v-if="currentPage > 0" @click="prevPage(currentPage - 1)" class="px-4 py-2 bg-blue-500 text-white rounded mr-2">Previous</button>
            <span>Page {{ currentPage + 1 }}</span>
            <button v-if="currentPage != totalPages - 1" @click="nextPage(currentPage + 1)" class="px-4 py-2 bg-blue-500 text-white rounded ml-2">Next</button>
        </div>
    </div>
</template>

<script>
import apiMethods from '../../services/apiMothods';

export default {
    data() {
        return {
            tokens: [],
            currentSignerAddress: "",
            isConnected: false,
            token: localStorage.getItem('token'),
            role: localStorage.getItem('role'),
            sort: "",
            price: "",
            profit: "",
            currentPage: 0,
            totalPages: 0,
            pageSize: 10,
        };
    },
    methods: {
        prevPage(page) {
            this.currentPage = page;
            this.fetchTokens(this.sort, this.price, this.profit)
        },
        nextPage(page) {
            this.currentPage = page;
            this.fetchTokens(this.sort, this.price, this.profit)
        },
        async fetchTokens(sort, price, profit) {
            try {
                let params = {
                    page: this.currentPage,
                    size: this.pageSize,
                    sort: sort ? sort : this.sort,
                    price: price ? price : this.price,
                    profit: profit ? profit : this.profit,
                }

                const response = await apiMethods.getData("/token", params);
                this.totalPages = response?.totalPages
                this.tokens = response?.tokens;
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                    this.$router.push('/login')
                }
            }
        },

        sortingByCreated () {
            if(this.sort === "desc") {
                this.sort = "asc"
                this.price = null
                this.profit = null
            }else{
                this.sort = "desc"
                this.price = null
                this.profit = null
            }
            this.fetchTokens(this.sort, this.price, this.profit)
        },
        sortingByPrice () {
            if(this.price === "desc") {
                this.price = "asc"
                this.sort = null
                this.profit = null
            }else{
                this.price = "desc"
                this.sort = null
                this.profit = null
            }
            this.fetchTokens(this.sort, this.price, this.profit)
        },
        sortingByProfit () {
            if(this.profit === "desc") {
                this.profit = "asc"
                this.price = null
                this.sort = null
            }else{
                this.profit = "desc"
                this.price = null
                this.sort = null
            }
            this.fetchTokens(this.sort, this.price, this.profit)
        }
    },
    mounted() {
        this.fetchTokens();
    },
};
</script>
