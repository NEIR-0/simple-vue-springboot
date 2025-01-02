<template>
    <div class="h-screen flex flex-col items-center justify-center bg-red-300">
      <h1>Wallet</h1>

      <input
        type="number"
        v-model="nominal"
        placeholder="nominal..."
        class="mb-4 px-4 py-2 border rounded"
      />
      <input
        type="text"
        v-model="address"
        placeholder="address..."
        class="mb-4 px-4 py-2 border rounded"
      />

      <button @click="signTransaction">sign</button>
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
        } else {
          console.log("Ethereum wallet is not connected. !!!!!");
        }
      } catch (error) {
        console.error('Error send message data:', error);
        if (error?.message) {
          this.$router.push('/login')
        }
      }
    },
  },
  mounted() {

  },
}
</script>