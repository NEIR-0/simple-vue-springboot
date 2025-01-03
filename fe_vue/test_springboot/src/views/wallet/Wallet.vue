<template>
    <div class="h-screen flex flex-col items-center justify-center bg-[#3D3D3D]">
      <div class="px-20 py-10 w-[500px] rounded-lg shadow-xl flex flex-col justify-center items-center border-2 bg-white">
        <h1 class="w-full text-center text-2xl font-medium mb-5">Transfer Ether</h1>
        <div class="w-full space-y-4 flex items-center justify-center flex-col">
          <input
            class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-3 rounded-md focus:ring-0 outline-none" type="text"
            v-model="nominal"
            placeholder="nominal..."
          />
          <input
            class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-3 rounded-md focus:ring-0 outline-none" type="text"
            v-model="address"
            placeholder="address..."
          />
        </div>
        <button @click="signTransaction" class="w-1/2 h-10 p-5 mt-5 flex items-center justify-center rounded-md font-semibold text-slate-800 bg-[#ffde09] hover:bg-[#ffe74e] duration-300 ease-in-out">sign</button>
      </div>
    </div>
</template>

<script>
import abi from "../../../abi/sendEther.js";
import abiDeploy from "../../../abi/SimpleSendEtherTestSepolia.js";

import { ethers } from "ethers";

export default {
  data() {
    return {
      nominal: "",
      address: "",
    };
  },
  methods: {
    async signTransaction() {
      try {
        if (typeof window.ethereum !== "undefined") {
          await ethereum.request({ method: "eth_requestAccounts" });
          const prov = new ethers.providers.Web3Provider(window.ethereum);
          const sig = await prov.getSigner();
          // console.log(sig, ">>>>>>>>>>>");

          const contractAddress = import.meta.env
            .VITE_ADDRESS_CONTACT_SIMPLESENDERETHER;
          // console.log(contractAddress, "????");

          // local
          const smartContract = new ethers.Contract(contractAddress, abi, sig); 
          console.log("smartContract Local: ", smartContract);
          const tx = await smartContract.sendEther(this.address, {
            value: ethers.utils.parseEther(String(this.nominal)),
          });

          // deploy
          // const smartContract = new ethers.Contract(contractAddress, abiDeploy, sig);
          // console.log("smartContract Deploy: ", smartContract);
          // const tx = await smartContract.sendSepoliaTestnet(this.address, {
          //   value: ethers.utils.parseEther(String(this.nominal)),
          // });

          console.log("Tx: ", tx, "????");
          console.log("Transaction Hash: ", tx.hash);
          await tx.wait();
          this.$toast.open({
            message: "Transaction Success",
            type: 'success',
            duration: 3000,
            position: 'top-right'
          });
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
  },
  mounted() {

  },
}
</script>