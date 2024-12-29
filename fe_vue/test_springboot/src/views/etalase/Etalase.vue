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
        </div>
      </div>
      <div class="h-fit w-[28%] bg-[#EAE3D8] absolute right-5 top-36 flex items-center justify-center p-5 rounded-md">
        <div class="w-full">
          <input v-model="searchQuery" @input="onSearch" class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-3 rounded-md focus:ring-0 outline-none" type="text" placeholder="Search..." name="search" id="search">
        </div>
      </div>
    </div>

    <!-- modal -->
    <ModalsContainer/>
</template>

<script>
import abi from "../../../abi/TransactionStorage.js";
import { ethers } from "ethers";
import WebSocketService from '../../services/WebSocketService';
import CardProducts from '../../components/CardProducts.vue'
import apiMethods from '../../services/apiMothods';
import { ModalsContainer, useModal } from 'vue-final-modal'
import Modal from '../../components/Modal.vue'

export default {
  components: {
    CardProducts,
    Modal,
    ModalsContainer
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
      tempAddress: false,
      responseMessage: '',
      titleModal: '',
    };
  },

  methods: {
    openModal() {
      const { open, close } = useModal({
        component: Modal,
        attrs: {
          title: this.titleModal,
          responseMessage: this.responseMessage,
          isShowButton: true
        },
      });
      open()
    },
    
    async fetchProducts(query = '') {
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          search: query,
        }
        const data = await apiMethods.getData("/products", params);

        this.products = data;
        this.totalPages = Math.ceil(data.length / this.pageSize);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    },

    async updateTransactions(hash = '', id = null) {
      try {
        const body = {
          id,
          hash,
          status: "success",
        }
        const data = await apiMethods.putData("/transaction/update", body);
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
      try {
        // Validate wallet address
        if (this.tempAddress === "") {
          await this.connectWallets(); // Reconnect if tempAddress is empty
        }

        const accounts = await ethereum.request({ method: "eth_accounts" });
        if (accounts.length > 0 && this.tempAddress !== accounts[0]) {
          this.tempAddress = accounts[0]; // Update tempAddress with the current address
          console.log("Address updated to: ", this.tempAddress);
        }

        if (this.tempAddress) {
          const body = {
            description: `${data?.title} - ${data?.duration} Days`,
            price: data?.price,
            address_signed: this.tempAddress,
            status: "pending",
          };
          console.log("address_signed: ", body.address_signed, ">>><<<");

          const response = await apiMethods.postData("/transaction/create", body);
          this.updateTransactionsRealTime();
          return response;
        } else {
          throw new Error("Wallet not connected");
        }
      } catch (error) {
        console.log("Error creating transaction: ", error);
      }
    },

    async createTransactionsAndBlockchain(data) {
      try {
        const local = await this.createTransactions(data);
        console.log("local: ", local);
        if(local){

          const now = new Date();
          const day = now.getDate().toString().padStart(2, '0'); // Day of the month
          const month = (now.getMonth() + 1).toString().padStart(2, '0'); // Month (0-11, so we add 1)
          const year = now.getFullYear(); // Year
          const hours = now.getHours().toString().padStart(2, '0'); // Hours (24-hour format)
          const minutes = now.getMinutes().toString().padStart(2, '0'); // Minutes
          const seconds = now.getSeconds().toString().padStart(2, '0'); // Seconds

          // Format: "DD/MM/YYYY HH:MM:SS"
          const formattedDate = `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
          const storeData = `${data.description}, Date: ${formattedDate}`;
          const blockchain = await this.createBlockchain(storeData, data.price);
          console.log("blockchain: ", blockchain);
  
          if(blockchain && blockchain?.hash && local && local?.transactionId) {
            const hash = blockchain?.hash;
            const id = local?.transactionId;
            const response = await this.updateTransactions(hash, id);
            if(response){
              this.responseMessage = response?.msg;
              this.titleModal = "Your Transaction Was Successful !!";
              this.openModal();
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
          const accounts = await ethereum.request({ method: "eth_requestAccounts" });
          if (accounts.length > 0) {
            this.tempAddress = accounts[0];
            this.isWalletConnected = true;
            console.log("Wallet connected: ", this.tempAddress);
          }
        } else {
          console.log("Ethereum wallet is not connected.");
        }
      } catch (error) {
        console.log("Error connecting wallet: ", error);
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
    this.fetchProducts()
    WebSocketService.responseUpdateTransactionsRealTime(this.RealTimeTransactions);

    // Monitor wallet address changes
    if (typeof window.ethereum !== "undefined") {
      window.ethereum.on("accountsChanged", (accounts) => {
        if (accounts.length > 0) {
          this.tempAddress = accounts[0]; // Update tempAddress with the new address
          console.log("Wallet address changed to: ", this.tempAddress);
          this.isWalletConnected = true;
        } else {
          this.tempAddress = "";
          this.isWalletConnected = false;
          console.log("No wallet connected.");
        }
      });
    }
  },

  beforeDestroy() {
    WebSocketService.disconnect();
  },
}
</script>