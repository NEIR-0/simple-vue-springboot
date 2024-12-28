<template>
    <div class="h-screen overflow-hidden w-full flex flex-col items-start justify-center bgEtalase">
      <div class="text-white w-full h-full py-14 space-y-2 px-5">
        <div class="">
          <h1 class="font-bold text-white text-3xl capitalize">choose your products</h1>
          <p class="font-light text-white text-sm">Lorem ipsum dolor sit amet consectetur adipisicing elit. A, totam. </p>
        </div>
        <div class="w-[70%] h-full overflow-y-scroll">
          <div v-if="products.length > 0" class="w-full h-fit grid grid-cols-4 gap-2 pt-4 pb-10">
            <div v-for="product in products" :key="product.id">
              <CardProducts :products="product" :onBuy="createTransactionsAndBlockchain" :isWalletConnected="isWalletConnected" :connectWallets="connectWallets" />
            </div>
          </div>
    
          <!-- <ul v-if="transactions?.length > 0" class="mt-10">
            <li v-for="transaction in transactions">{{transaction?.id}} {{transaction?.status}}</li>
          </ul> -->
        </div>
      </div>
      <div class="h-fit w-[28%] bg-[#EAE3D8] absolute right-5 top-36 flex items-center justify-center p-5 rounded-md">
        <div class="w-full">
          <input v-model="searchQuery" @input="onSearch" class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-3 rounded-md focus:ring-0 outline-none" type="text" placeholder="Search..." name="search" id="search">
        </div>
      </div>
    </div>
</template>

<script>
import axios from 'axios';
import abi from "../../../abi/TransactionStorage.js";
import { ethers } from "ethers";
import WebSocketService from '../../services/WebSocketService';
import CardProducts from '../../components/CardProducts.vue'

export default {
  components: {
    CardProducts,
  },
  data() {
    return {
      transactions: [],
      products: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,
      searchQuery: '',
      isWalletConnected: false,
    };
  },
  methods: {
    async fetchTransactions() {
      const BearerToken = localStorage.getItem('token');
      try {
        const { data } = await axios.get('http://localhost:8081/transaction', {
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });
        this.transactions = data;
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    async fetchProducts(query = '') {
      const BearerToken = localStorage.getItem('token');
      try {
        const { data } = await axios.get('http://localhost:8081/products', {
          params: {
            page: this.currentPage,
            size: this.pageSize,
            search: query,
          },
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });  
        this.products = data;
        this.totalPages = Math.ceil(data.length / this.pageSize);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    async updateTransactions(hash = '', id = null) {
      const BearerToken = localStorage.getItem('token');
      const body = {
        transactionId: id,
        hash,
        status: "success",
      }
      try {
        const { data } = await axios.put('http://localhost:8081/transaction/update', 
        body,
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });
        return data;
      } catch (error) {
        console.log(error, "!!!!!!!!!!!!!!!!!");        
      }
    },

    async createBlockchain(stringData, price) {
      try {
        if (typeof window.ethereum !== "undefined") {
          await ethereum.request({ method: "eth_requestAccounts" });
          const prov = new ethers.providers.Web3Provider(window.ethereum);
          const sig = await prov.getSigner();

          const contractAddress = import.meta.env
            .VITE_ADDRESS_CONTACT_SIMPLESENDERETHER;

          // local
          const smartContract = new ethers.Contract(contractAddress, abi, sig); 

          const tx = await smartContract.storeStringWithPayment(stringData, {
            value: ethers.utils.parseEther(String(price)),
          });
          await tx.wait();
          return tx;
        } else {
          console.log("Ethereum wallet is not connected. !!!!!");
        }
      } catch (error) {
        console.log(error, "!!!!!!!!!!!!!!!!!");        
      }
    },

    async createTransactions(data) {
      const BearerToken = localStorage.getItem('token');
      const body = {
        des: data.description,
        price: data.price,
        status: "pending",
      }
      try {
        const { data } = await axios.post('http://localhost:8081/transaction/create', 
        body,
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: BearerToken,
          },
        });
        this.updateTransactionsRealTime();
        return data;
      } catch (error) {
        console.log(error, "!!!!!!!!!!!!!!!!!");        
      }
    },

    async createTransactionsAndBlockchain(data) {
      try {
        const local = await this.createTransactions(data);
        console.log("local: ", local);
        if(local){
          const blockchain = await this.createBlockchain(data.description, data.price);
          console.log("blockchain: ", blockchain);
  
          if(blockchain && blockchain?.hash && local && local?.transactionId) {
            const hash = blockchain?.hash;
            const id = local?.transactionId;
            const response = await this.updateTransactions(hash, id);
            if(response){
              console.log(response?.msg, "????");
              this.updateTransactionsRealTime();
            }
          }
        }
      } catch (error) {
        console.log(error, "!!!!!!!!!!!!!!!!!");
      }
    },

    async connectWallets() {
      try {
        if (typeof window.ethereum !== "undefined") {
          await ethereum.request({ method: "eth_requestAccounts" });
          const prov = new ethers.providers.Web3Provider(window.ethereum);
          const sig = await prov.getSigner();
          const address = await sig.getAddress();
          if(address){
            this.isWalletConnected = true
          }
        } else {
          console.log("Ethereum wallet is not connected. !!!!!");
        }
      } catch (error) {
        console.log(error, "!!!!!!!!!!!!!!!!!");        
      }
    },

    onSearch() {
      this.currentPage = 0;
      this.fetchProducts(this.searchQuery);
    },

    updateTransactionsRealTime() {
        WebSocketService.updateTransactionsRealTime();
    },

    RealTimeTransactions(newMessage) {
      this.transactions = (newMessage);
    },
  },

  mounted() {
    this.fetchTransactions()
    this.fetchProducts()
    WebSocketService.responseUpdateTransactionsRealTime(this.RealTimeTransactions);
  },

  beforeDestroy() {
    WebSocketService.disconnect();
  },
}
</script>