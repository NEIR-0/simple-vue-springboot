<template>
    <div class="w-full h-fit flex items-center justify-center flex-col pt-20">
        <!-- Tombol Connect MetaMask -->
        <div>
            <button @click="connectMetaMask" class="bg-blue-500 text-white p-2 rounded mb-4">Connect MetaMask</button>
        </div>

        <!-- Info token dan tombol lainnya jika MetaMask terhubung -->
        <div v-if="isConnected">
            <h1 class="text-2xl font-semibold mb-4">Token Information</h1>

            <!-- Tabel untuk menampilkan data token -->
            <table class="table-auto border-collapse border border-gray-300 w-1/2">
                <thead>
                    <tr>
                        <th class="border px-4 py-2">Attribute</th>
                        <th class="border px-4 py-2">Value</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="border px-4 py-2">Token Name</td>
                        <td class="border px-4 py-2">{{ tokenName }}</td>
                    </tr>
                    <tr>
                        <td class="border px-4 py-2">Token Symbol</td>
                        <td class="border px-4 py-2">{{ tokenSymbol }}</td>
                    </tr>
                    <tr>
                        <td class="border px-4 py-2">Token Price</td>
                        <td class="border px-4 py-2">{{ tokenPrice }}</td>
                    </tr>
                    <tr>
                        <td class="border px-4 py-2">Total Supply</td>
                        <td class="border px-4 py-2">{{ totalSupply }}</td>
                    </tr>
                    <tr>
                        <td class="border px-4 py-2">Decimals</td>
                        <td class="border px-4 py-2">{{ decimals }}</td>
                    </tr>
                    <tr>
                        <td class="border px-4 py-2">Owner</td>
                        <td class="border px-4 py-2">{{ owner }}</td>
                    </tr>
                    <tr>
                        <td class="border px-4 py-2">Balance of User</td>
                        <td class="border px-4 py-2">{{ userBalance }}</td>
                    </tr>
                </tbody>
            </table>

            <div v-if="token && role === 'admin'">
                <div class="mt-4 flex items-center justify-center space-x-4">
                    <div v-if="!isBurning" class="flex items-center justify-center space-x-4">
                        <div class="mb-2">
                            <label for="mintAmount" class="block text-lg">Amount to Mint:</label>
                            <input v-model="mintAmount" type="number" class="p-2 border rounded" placeholder="Enter amount to mint" />
                        </div>
                        <div class="mb-2">
                            <label for="persentaseProfit" class="block text-lg">Persentase Profit:</label>
                            <input v-model="persentaseProfit" type="number" class="p-2 border rounded" placeholder="Enter amount to mint" />
                        </div>
                    </div>
    
                    <div class="flex items-center justify-center space-x-4">
                        <div v-if="isBurning" class="mb-2">
                            <label for="burnAmount" class="block text-lg">Amount to Burn:</label>
                            <input v-model="burnAmount" type="number" class="p-2 border rounded" placeholder="Enter amount to burn" />
                        </div>
                    </div>
                </div>
                <div class="mt-4 flex items-center justify-center space-x-4">
                    <button @click="showMinting" class="bg-green-500 text-white p-2 rounded">Mint Token</button>
                    <button @click="showburnging" class="bg-red-500 text-white p-2 rounded ml-2">Burn Token</button>
                    <button @click="withdrawFunds" class="bg-blue-500 text-white p-2 rounded ml-2">Withdraw</button>
                </div>
            </div>

            <div v-if="token && role !== 'admin'">
                <div class="mt-4 flex items-center justify-center space-x-4">
                    <input v-model="buyAmount" type="number" placeholder="Amount to Buy" class="p-2 border rounded"/>
                    <button @click="buyToken" class="bg-blue-500 text-white p-2 rounded ml-2">Buy Token</button>
                </div>
            </div>
            
            <p v-if="status" class="mt-4">{{ status }}</p>
        </div>
    </div>
</template>

<script>
import { ethers } from "ethers";
import abi from "../../../abi/tokenize/SimpleToken.js"; // ABI kontrak

export default {
    data() {
        return {
            token: localStorage.getItem('token'),
            role: localStorage.getItem('role'),
            tokenName: "",
            tokenSymbol: "",
            tokenPrice: "",
            totalSupply: "",
            decimals: "",
            owner: "",
            userBalance: "",
            status: "",
            mintAmount: "",
            persentaseProfit: null,
            burnAmount: "", 
            buyAmount: "",
            isConnected: false,
            currentSignerAddress: "",
            isBurning: false,
        };
    },
    mounted() {
        this.checkMetaMaskConnection();
    },
    methods: {
        showMinting() {
            this.isBurning = false
            this.burnAmount = "";
            console.log(this.mintAmount, this.persentaseProfit);
            if (this.mintAmount && this.persentaseProfit) {
                this.mintToken()
            }
        },
        showburnging() {
            this.isBurning = true
            this.mintAmount = "";
            this.persentaseProfit = null;
            if (this.burnAmount) {
                this.burnToken()      
            }
        },
        async connectMetaMask() {
            if (!window.ethereum) {
                this.status = "Please install MetaMask!";
                return;
            }
            try {
                // Meminta akses ke akun MetaMask
                await window.ethereum.request({ method: "eth_requestAccounts" });

                // Menghubungkan ke provider dan signer
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();

                // Menyimpan alamat signer dan mengubah status koneksi
                this.currentSignerAddress = await signer.getAddress();
                this.isConnected = true;

                // Mendapatkan data kontrak
                this.getContractData();
            } catch (error) {
                console.error(error);
                this.status = "Error connecting to MetaMask!";
            }
        },

        async checkMetaMaskConnection() {
            if (window.ethereum) {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const address = await signer.getAddress();
                if (address) {
                    this.isConnected = true;
                    this.currentSignerAddress = address;
                    this.getContractData();
                }
            }
        },

        async getContractData() {
            const contractAddress = this.$route.params.address;

            if (!window.ethereum) {
                this.status = "Please install MetaMask!";
                return;
            }

            try {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const contract = new ethers.Contract(contractAddress, abi, signer);

                const name = await contract.name();
                const symbol = await contract.symbol();
                const price = await contract.tokenPrice();
                const supply = await contract.totalSupply();
                const decimalCount = await contract.decimals();
                const ownerAddress = await contract.owner();
                const userBalance = await contract.balanceOf(signer.getAddress());

                this.tokenName = name;
                this.tokenSymbol = symbol;
                this.decimals = decimalCount;
                this.owner = ownerAddress;
                this.tokenPrice = price.toString()
                this.totalSupply = supply.toString()
                this.userBalance = userBalance.toString();
            } catch (error) {
                console.error(error);
                this.status = "Error fetching contract data!";
            }
        },

        async mintToken() {
            const contractAddress = this.$route.params.address;

            if (!this.mintAmount || this.mintAmount <= 0) {
                this.status = "Please enter a valid amount to mint!";
                return;
            }
            try {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const contract = new ethers.Contract(contractAddress, abi, signer);
                // const mintingQty = ethers.utils.parseEther(String(this.mintAmount)); // Mengkonversi jumlah ke dalam format wei
                const tx = await contract.mint(signer.getAddress(), this.mintAmount);
                await tx.wait(); // Tunggu transaksi selesai
                this.status = `Minting successful! Transaction Hash: ${tx.hash}`;
                this.getContractData(); // Perbarui data token setelah minting
            } catch (error) {
                console.error(error);
                this.status = "Error minting token!";
            }
        },

        async burnToken() {
            const contractAddress = this.$route.params.address;

            if (!this.burnAmount || this.burnAmount <= 0) {
                this.status = "Please enter a valid amount to burn!";
                return;
            }
            try {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const contract = new ethers.Contract(contractAddress, abi, signer);
                const tx = await contract.burn(this.burnAmount);
                await tx.wait(); // Tunggu transaksi selesai
                this.status = `Burning successful! Transaction Hash: ${tx.hash}`;
                this.getContractData(); // Perbarui data token setelah burning
            } catch (error) {
                console.error(error);
                this.status = "Error burning token!";
            }
        },

        async withdrawFunds() {
            const contractAddress = this.$route.params.address;

            try {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const contract = new ethers.Contract(contractAddress, abi, signer);

                const tx = await contract.withdraw();
                await tx.wait(); // Tunggu transaksi selesai
                this.status = `Withdrawal successful! Transaction Hash: ${tx.hash}`;
            } catch (error) {
                console.error(error);
                this.status = "Error withdrawing funds!";
            }
        },

        async buyToken() {
            const contractAddress = this.$route.params.address;
            
            if (!this.buyAmount || this.buyAmount <= 0) {
                this.status = "Please enter a valid amount to buy!";
                return;
            }
            try {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const contract = new ethers.Contract(contractAddress, abi, signer);

                const price = await contract.tokenPrice(); // Mendapatkan harga token dalam wei
                const totalCost = ethers.BigNumber.from(this.buyAmount).mul(price); // Menghitung total biaya dalam wei

                console.log("buyAmount:", this.buyAmount);
                console.log("Token Price (wei):", price.toString());
                console.log("Total Cost (wei):", totalCost.toString());

                // Kirim transaksi dengan nilai Ether yang sesuai
                const tx = await contract.buyToken(this.buyAmount, { value: totalCost });

                // Tunggu transaksi selesai
                await tx.wait();

                // Status sukses
                this.status = `Token purchase successful! Transaction Hash: ${tx.hash}`;
                this.getContractData(); // Update data setelah pembelian
            } catch (error) {
                if (error.code === "ACTION_REJECTED") {
                    this.status = "Transaction rejected by user!";
                } else {
                    console.error(error);
                    this.status = "Error purchasing tokens!";
                }
            }
        }
    }
};
</script>

<style scoped>
/* Sesuaikan gaya CSS untuk tampilannya */
</style>
