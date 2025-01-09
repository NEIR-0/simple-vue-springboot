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
    <p v-if="responseError" class="mt-4">{{ responseError }}</p>
  </div>
</template>

<script>
import { ethers } from "ethers";
import abi from "../../../abi/tokenize/SimpleToken.js";
import bytecode from "../../../abi/tokenize/byteCodeSimpleToken.js";
import apiMethods from '../../services/apiMothods';
  
export default {
    data() {
      return {
          tokenName: "",
          tokenSymbol: "",
          tokenPrice: "",
          responseError: ""
      };
    },
    methods: {
      async deployToken() {
          if (!window.ethereum) {
              this.responseError = "Please install MetaMask!";
              return;
          }
  
          try {
              if (!this.tokenName || !this.tokenSymbol || !this.tokenPrice) {
                  this.responseError = "All inputs are required!";
                  return;
              }
  
              const provider = new ethers.providers.Web3Provider(window.ethereum);
              const signer = provider.getSigner();
              const addressCreator = await signer.getAddress();
              const factory = new ethers.ContractFactory(abi, bytecode, signer);
              const tokenPriceInEther = ethers.utils.parseEther(String(this.tokenPrice));
              const contract = await factory.deploy(this.tokenName, this.tokenSymbol, tokenPriceInEther);
              const deployedContractAddress = contract.address;

              if(deployedContractAddress){
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

                this.createTokenDb(body);
              }
          } catch (error) {
              console.error(error);
              this.responseError = "Error deploying token!";
          }
      },
      async createTokenDb(body){
        try {
          const response = await apiMethods.postData("/token/create", body);
          console.log(response, ">>>>>>");
          this.$router.push('/token')
        } catch (error) {
          console.log(error);
        }
      }
    },
};
</script>
