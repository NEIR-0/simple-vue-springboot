<template>
    <div class="h-screen overflow-hidden w-full flex flex-col items-start justify-center relative bgEtalase">
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
      <div class="h-fit w-[28%] absolute right-5 top-36 flex flex-col items-center justify-center space-y-4">
        <div class="h-fit w-full bg-[#EAE3D8] flex items-center justify-center p-5 rounded-md">
          <div class="w-full">
            <input v-model="searchQuery" @input="onSearch" class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-3 rounded-md focus:ring-0 outline-none" type="text" placeholder="Search..." name="search" id="search">
          </div>
        </div>
        <div class="w-full h-14 bg-[#beaa89] flex items-center justify-start p-5 rounded-md space-x-4">
          <h1 class="text-white font-medium text-lg">Sort By:</h1>
          <select
            v-if="durationProducst?.length > 0"
            @change="selectFilterDuration"
            v-model="selectedDurationProducst"
            id="durations"
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-0 outline-none block w-fit p-2.5"
          >
            <option disabled value="">Choose durations</option>
            <option v-for="item in durationProducst">
              {{ item }}
            </option>
          </select>
          <h1 class="text-white font-medium text-lg">/ days</h1>
        </div>
        <div v-if="token && role == 'user' && !showChatBox" class="w-full flex justify-end">
          <button @click="showChatUser" class="h-10 p-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-[#ffde09] hover:bg-[#ffe74e] duration-300 ease-in-out">Chat customer service</button>
        </div>
      </div>

      <!-- chat -->
      <div v-if="showChatBox" class="absolute bottom-5 right-10 w-fit h-fit">
        <ChatBox 
          :messages="messages" 
          :sendMessageUser="sendMessageUser" 
          :newMessage="newMessage"
          :closeChatUser="closeChatUser"
          @update:newMessage="newMessage = $event"
        />
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
import ChatBox from "../../components/chatBox.vue";

export default {
  components: {
    CardProducts,
    Modal,
    ModalsContainer,
    ChatBox
  },
  data() {
    return {
      token: localStorage.getItem('token'),
      role: localStorage.getItem('role'),
      transactions: [],
      products: [],
      currentPage: 0,
      pageSize: 10,
      searchQuery: '',
      isWalletConnected: false,
      tempAddress: false,
      responseMessage: '',
      titleModal: '',
      showChatBox: false,
      messages: [],
      newMessage: '',
      durationProducst: [],
      selectedDurationProducst: "",
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

    selectFilterDuration(event) {
      const selectedValue = event.target.value;
      this.selectedDurationProducst = selectedValue;
      this.currentPage = 0;
      this.fetchProducts(this.searchQuery, this.selectedDurationProducst);
    },

    async showChatUser() {
      this.showChatBox = !this.showChatBox
      try {
        const data = await apiMethods.getData("/message");
        this.messages = data
        return data;
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
      }
    },

    closeChatUser() {
      this.showChatBox = !this.showChatBox
    },
    
    async fetchProducts(query = '', duration = '') {
      try {
        const params = {
          page: this.currentPage,
          size: this.pageSize,
          search: query,
          duration: duration
        }
        const data = await apiMethods.getData("/products", params);
        this.products = data?.product;
        this.durationProducst = data?.durations
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
        this.$toast.open({
          message: error?.message,
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
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
        this.$toast.open({
          message: "Transaction Success",
          type: 'success',
          duration: 3000,
          position: 'top-right'
        });
        return data;
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
        this.$toast.open({
          message: error?.message,
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
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
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
        this.$toast.open({
          message: "Something wrong with your wallet, please try again",
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
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
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
        this.$toast.open({
          message: error?.message,
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
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
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
        this.$toast.open({
          message: error?.message,
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
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
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
        this.$toast.open({
          message: "Something wrong with your wallet, please try again",
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
      }
    },

    async sendMessageUser() {      
      if (this.newMessage.trim() === "") return;
      try {
        const body = {
            content: this.newMessage,
            sendTo: this.currentUserId
        }
        console.log(body);
        const response = await apiMethods.postData("/message/create", body);

        const payload = {};
        if (response) {
          const [senderId, reciverId] = response?.msg.split('-');
          payload.senderId = senderId;
          payload.reciverId = reciverId;
          this.SendRealTimeMessager(payload);
        }
        this.newMessage = "";
              
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }
        this.$toast.open({
          message: error?.message,
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
      }
    },
    RealTimeMessager(newMessage) {
      this.messages = newMessage;
    },
    SendRealTimeMessager(payload) {
      WebSocketService.UpdateSendMessageRealTime(payload);
    },

    onSearch() {
      this.currentPage = 0;
      this.fetchProducts(this.searchQuery, this.selectedDurationProducst);
    },

    updateTransactionsRealTime() {
        WebSocketService.updateTransactionsRealTime();
    },
  },

  mounted() {
    this.fetchProducts()
    WebSocketService.responseSendMessageRealTime(this.RealTimeMessager);

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