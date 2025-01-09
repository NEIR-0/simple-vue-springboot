<template>
  <div class="w-full h-screen flex items-center justify-center flex-col">
    <h1 class="text-2xl font-semibold mb-4">Create Your Token</h1>
    
    <!-- Input untuk Nama Token -->
    <div class="mb-2">
      <label for="tokenName" class="block text-lg">Token Name:</label>
      <input id="tokenName" v-model="tokenName" type="text" class="p-2 border rounded" placeholder="Enter token name"/>
    </div>

    <!-- Input untuk Simbol Token -->
    <div class="mb-2">
      <label for="tokenSymbol" class="block text-lg">Token Symbol:</label>
      <input id="tokenSymbol" v-model="tokenSymbol" type="text" class="p-2 border rounded" placeholder="Enter token symbol"/>
    </div>

    <!-- Input untuk Harga Token -->
    <div class="mb-4">
      <label for="tokenPrice" class="block text-lg">Token Price (in Ether):</label>
      <input id="tokenPrice" v-model="tokenPrice" type="number" class="p-2 border rounded" placeholder="Enter token price in Ether"/>
    </div>
    
    <!-- Tombol untuk deploy -->
    <button @click="deployToken" class="bg-blue-500 text-white p-2 rounded">
      Deploy Token
    </button>
    
    <!-- Status -->
    <p v-if="status" class="mt-4">{{ status }}</p>
  </div>
</template>

<script>
  import { ethers } from "ethers";
  import abi from "../../../abi/tokenize/SimpleToken.js"; // ABI dari kontrak
  import bytecode from "../../../abi/tokenize/byteCodeSimpleToken.js"; // Bytecode kontrak
  
  export default {
      data() {
      return {
          tokenName: "",
          tokenSymbol: "",
          tokenPrice: "",
          status: ""
      };
      },
      methods: {
      async deployToken() {
          if (!window.ethereum) {
              this.status = "Please install MetaMask!";
              return;
          }
  
          try {
              if (!this.tokenName || !this.tokenSymbol || !this.tokenPrice) {
                  this.status = "All inputs are required!";
                  return;
              }
  
              // Provider dan signer untuk berinteraksi dengan Ethereum
              const provider = new ethers.providers.Web3Provider(window.ethereum);
              const signer = provider.getSigner();
  
              // Membuat ContractFactory untuk mendeply kontrak baru
              const factory = new ethers.ContractFactory(abi, bytecode, signer);
              
              // Token price dalam Ether dikonversi ke Wei
              const tokenPriceInEther = ethers.utils.parseEther(String(this.tokenPrice));
  
              // Men-deploy kontrak dengan input dari pengguna
              const contract = await factory.deploy(this.tokenName, this.tokenSymbol, tokenPriceInEther);
  
              // Menunggu transaksi untuk dikonfirmasi
              const receipt = await contract.deployTransaction.wait();
              console.log("receipt: ", receipt);
              
              // Mendapatkan alamat kontrak dari receipt
              const deployedContractAddress = contract.address;
  
              this.status = `Token deployed! Contract Address: ${deployedContractAddress}, Transaction Hash: ${contract.deployTransaction.hash}`;
          } catch (error) {
              console.error(error);
              this.status = "Error deploying token!";
          }
      },
      },
  };
</script>
