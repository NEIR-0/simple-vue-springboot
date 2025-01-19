<template>
  <div class="w-full h-screen flex items-center justify-center flex-col">
    <h1 class="text-2xl font-semibold mb-4">Create Your Token</h1>

    <!-- Status koneksi wallet -->
    <p class="mb-4 text-red-500" v-if="!walletConnected">
      Please connect your wallet first!
    </p>

    <!-- Tombol Connect Wallet -->
    <button
      v-if="!walletConnected"
      @click="connectMetaMask"
      class="bg-blue-500 text-white p-2 rounded mb-4"
    >
      Connect Wallet
    </button>

    <!-- Form input token hanya tampil jika wallet sudah terkoneksi -->
    <div v-if="walletConnected">
      <!-- Input untuk Nama Token -->
      <div class="mb-2">
        <label for="tokenName" class="block text-lg">Token Name:</label>
        <input
          id="tokenName"
          v-model="tokenName"
          type="text"
          class="p-2 border rounded"
          placeholder="Enter token name"
        />
      </div>

      <!-- Input untuk Simbol Token -->
      <div class="mb-2">
        <label for="tokenSymbol" class="block text-lg">Token Symbol:</label>
        <input
          id="tokenSymbol"
          v-model="tokenSymbol"
          type="text"
          class="p-2 border rounded"
          placeholder="Enter token symbol"
        />
      </div>

      <!-- Input untuk Harga Token -->
      <div class="mb-4">
        <label for="tokenPrice" class="block text-lg">Token Price (in Ether):</label>
        <input
          id="tokenPrice"
          v-model="tokenPrice"
          type="number"
          class="p-2 border rounded"
          placeholder="Enter token price in Ether"
        />
      </div>

      <!-- Tombol Deploy -->
      <button @click="deployToken" class="bg-green-500 text-white p-2 rounded">
        Deploy Token
      </button>
    </div>
  </div>

  <!-- modal -->
  <ModalsContainer />
</template>

<script>
import { ethers } from "ethers";
import abi from "../../../abi/tokenize/SimpleToken.js";
import bytecode from "../../../abi/tokenize/byteCodeSimpleToken.js";
import apiMethods from "../../services/apiMothods";
import { ModalsContainer, useModal } from 'vue-final-modal'
import Modal from '../../components/modal/StatusModal.vue'

export default {
  components: {
        Modal,
        ModalsContainer,
    },
  data() {
    return {
      tokenName: "",
      tokenSymbol: "",
      tokenPrice: "",
      walletConnected: false,
      modalInstance: null,
    };
  },
  methods: {
    openModal() {
      this.modalInstance = useModal({
        component: Modal,
        attrs: {
          title: "Deploy Token on Progress...",
          responseMessage: "Please wait for a while, your transaction is being processed",
          isShowButton: false, // Bisa atur true/false
        },
      });
      this.modalInstance.open();
    },

    closeModal() {
      if (this.modalInstance) {
        this.modalInstance.close();
        this.modalInstance = null; // Bersihkan instance modal
      }
    },
    // Fungsi untuk koneksi ke MetaMask
    async connectMetaMask() {
      if (!window.ethereum) {
        this.$toast.open({
          message: "Please install MetaMask!",
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
        return;
      }
      try {
        const accounts = await window.ethereum.request({
          method: "eth_requestAccounts",
        });
        console.log("Connected account:", accounts[0]);
        this.walletConnected = true; // Set walletConnected ke true
      } catch (error) {
        console.error("Error connecting wallet:", error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }else{
          this.$toast.open({
            message: "Error connecting to MetaMask!",
            type: 'error',
            duration: 3000,
            position: 'top-right'
          });
        }
      }
    },

    // Fungsi untuk deploy token
    async deployToken() {
      if (!window.ethereum) {
        this.$toast.open({
          message: "Please install MetaMask!",
          type: 'error',
          duration: 3000,
          position: 'top-right'
        });
        return;
      }

      try {
        if (!this.tokenName || !this.tokenSymbol || !this.tokenPrice) {
          this.$toast.open({
            message: "All inputs are required!",
            type: 'error',
            duration: 3000,
            position: 'top-right'
          });
          return;
        }

        const provider = new ethers.providers.Web3Provider(window.ethereum);
        const signer = provider.getSigner();
        const addressCreator = await signer.getAddress();
        const factory = new ethers.ContractFactory(abi, bytecode, signer);
        const tokenPriceInEther = ethers.utils.parseEther(
          String(this.tokenPrice)
        );
        const contract = await factory.deploy(
          this.tokenName,
          this.tokenSymbol,
          tokenPriceInEther
        );
        await contract.deployed(); // Tunggu kontrak selesai di-deploy
        const deployedContractAddress = contract.address;
        console.log("Token deployed at:", deployedContractAddress);

        if (deployedContractAddress) {
          this.openModal();

          const body = {
            symbol: this.tokenSymbol,
            name: this.tokenName,
            tokenPrice: this.tokenPrice,
            hash: contract.deployTransaction.hash,
            addressCreator: addressCreator,
            totalSupply: 0,
            totalBurn: 0,
            alreadyBurn: 0,
            isApprove: true,
            status: "deployed",
            profitPersen: 0,
            addressToken: deployedContractAddress,
          };

          await this.createTokenDb(body);
          this.closeModal()
        }
      } catch (error) {
        console.error("Error message: ", error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }else{
          this.$toast.open({
            message: "Error deploying token!",
            type: 'error',
            duration: 3000,
            position: 'top-right'
          });
        }
      }
    },

    // Fungsi untuk menyimpan data token ke database
    async createTokenDb(body) {
      try {
        const response = await apiMethods.postData("/token/create", body);
        if (response) {
          const [title, userId] = response?.msg.split('-');
          this.$router.push("/token?userId="+userId); // Redirect ke halaman token
        }
      } catch (error) {
        console.error("Error saving token to DB:", error);
        if (error?.message === "Invalid or expired token") {
          this.$router.push('/login')
        }else{
          this.$toast.open({
            message: "Error saving token to the database!",
            type: 'error',
            duration: 3000,
            position: 'top-right'
          });
        }
      }
    },
  },
  mounted() {
    // Cek jika wallet sudah terkoneksi saat halaman dimuat
    if (window.ethereum && window.ethereum.selectedAddress) {
      this.walletConnected = true;
      console.log("Wallet already connected:", window.ethereum.selectedAddress);
    }
  },
};
</script>

