<template>
    <div class="h-screen p-5 bg-red-300">
      <div v-if="products.length > 0" class="w-full h-fit flex space-x-2">
        <div v-for="product in products" :key="product.id">
          <div class="w-[300px] h-fit p-5 bg-white rounded-md">
            <img :src="product?.image" alt="">
            <div class="w-full mt-2 flex flex-col space-y-3">
              <h1>{{ product?.title }}</h1>
              <p class="text-xs">{{ product?.description }}</p>
              <button @click="createTransactionsAndBlockchain(product)" class="w-full h-10 rounded-md bg-green-300 text-white font-bold">BUY, {{ product?.price }} ETH</button>
            </div>
          </div>
        </div>
      </div>

      <ul v-if="transactions?.length > 0" class="mt-10">
        <li v-for="transaction in transactions">{{transaction?.id}} {{transaction?.status}}</li>
      </ul>
    </div>
</template>

<script>
import axios from 'axios';
import abi from "../../../abi/TransactionStorage.js";
import { ethers } from "ethers";
import WebSocketService from '../../services/WebSocketService';

export default {
  data() {
    return {
      transactions: [],
      products: [],
      currentPage: 0,
      totalPages: 0,
      pageSize: 10,
      searchQuery: '',
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

      } catch (error) {
        console.log(error, "!!!!!!!!!!!!!!!!!");
      }
    },

    updateTransactionsRealTime() {
        WebSocketService.updateTransactionsRealTime();
    },

    RealTimeTransactions(newMessage) {
      console.log("newMessage: ", newMessage, "?AS?D?ASD?AS?D");
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