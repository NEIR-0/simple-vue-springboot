<template>
    <div class="w-full h-fit flex items-center justify-center flex-col pt-36">
        <div class="absolute top-20 right-20">
            <router-link class="bg-red-200 rounded-md py-2 px-10 items-center justify-center" to="/token/create">+ create token</router-link>
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
                        <router-link class="bg-red-200 rounded-md py-2 px-10 items-center justify-center" :to="`/token/detail/${token.addressToken}`">detail token</router-link>
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
    </div>
</template>

<script>
import apiMethods from '../../services/apiMothods';
import { ethers } from "ethers";
import abi from "../../../abi/tokenize/SimpleToken.js";

export default {
    data() {
        return {
            tokens: [],
            currentSignerAddress: "",
            isConnected: false,
        };
    },
    methods: {
        async fetchTokens() {
            try {
                const response = await apiMethods.getData("/token");
                this.tokens = response;
            } catch (error) {
                console.log(error);
            }
        },
    },
    mounted() {
        this.fetchTokens();
    },
};
</script>
