<template>
    <div class="w-full h-fit flex items-center justify-center flex-col pt-20">
        <!-- Tombol Connect MetaMask -->
        <div>
            <button v-if="!isWalletConnected" @click="connectWallets" class="bg-blue-500 text-white p-2 rounded mb-4">Connect MetaMask</button>
        </div>

        <!-- Info token dan tombol lainnya jika MetaMask terhubung -->
        <div v-if="isWalletConnected" class="w-full flex items-center justify-center flex-col space-y-10">
            <div class="w-full flex-col flex items-center justify-center">
                <h1 class="text-2xl font-semibold mb-2">Token Information</h1>
                <div class="h-1 w-1/5 bg-slate-600"></div>
            </div>

            <!-- Tabel untuk menampilkan data token -->
            <table class="table-auto border-collapse w-1/2 shadow-lg">
                <thead>
                    <tr>
                        <th class="border border-gray-500  px-4 py-2">Attribute</th>
                        <th class="border border-gray-500  px-4 py-2">Value</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="border border-gray-500  px-4 py-2">Token Name</td>
                        <td class="border border-gray-500  px-4 py-2">{{ tokenName }}</td>
                    </tr>
                    <tr>
                        <td class="border border-gray-500  px-4 py-2">Token Symbol</td>
                        <td class="border border-gray-500  px-4 py-2">{{ tokenSymbol }}</td>
                    </tr>
                    <tr>
                        <td class="border border-gray-500  px-4 py-2">Token Price</td>
                        <td class="border border-gray-500  px-4 py-2">{{ tokenPrice }}</td>
                    </tr>
                    <tr>
                        <td class="border border-gray-500  px-4 py-2">Total Supply</td>
                        <td class="border border-gray-500  px-4 py-2">{{ totalSupply }}</td>
                    </tr>
                    <tr>
                        <td class="border border-gray-500  px-4 py-2">Decimals</td>
                        <td class="border border-gray-500  px-4 py-2">{{ decimals }}</td>
                    </tr>
                    <tr>
                        <td class="border border-gray-500  px-4 py-2">Owner</td>
                        <td class="border border-gray-500  px-4 py-2">{{ owner }}</td>
                    </tr>
                    <tr>
                        <td class="border border-gray-500  px-4 py-2">Balance of User</td>
                        <td class="border border-gray-500  px-4 py-2">{{ userBalance }}</td>
                    </tr>
                </tbody>
            </table>

            <div v-if="token && role === 'admin' && tempAddress === owner">
                <div class="mt-4 flex items-center justify-center space-x-4">
                    <div v-if="tokenDetail?.totalBurn == 0" class="flex items-center justify-center space-x-4">
                        <div class="mb-2">
                            <label for="mintAmount" class="block text-lg">Amount to Mint:</label>
                            <input v-model="mintAmount" type="number" class="p-2 border rounded" placeholder="Enter amount to mint" />
                        </div>
                        <div class="mb-2">
                            <label for="persentaseProfit" class="block text-lg">Persentase Profit (%) :</label>
                            <input v-model="persentaseProfit" type="number" class="p-2 border rounded" placeholder="Enter Persentase Profit" />
                        </div>
                        <div class="mb-2">
                            <label for="total_burn" class="block text-lg">Choose Total Burn:</label>
                            <select
                                v-model="total_burn"
                                id="total_burn"
                                class="bg-white border border-gray-200 text-slate-900 text-sm rounded w-full p-2 focus:ring-blue-500 focus:border-blue-500"
                                >
                                <option v-for="burn in totalBurn" :value="burn">{{burn}}</option>
                            </select>
                        </div>
                        <div class="mb-2">
                            <label for="time_burn" class="block text-lg">Choose Burning Time:</label>
                            <select
                                v-model="time_burn"
                                id="time_burn"
                                class="bg-white border border-gray-200 text-slate-900 text-sm rounded w-full p-2 focus:ring-blue-500 focus:border-blue-500"
                                >
                                <option v-for="time in timeBurn" :value="time">{{time}}</option>
                            </select>
                        </div>
                    </div>
    
                    <div v-if="tokenDetail?.totalBurn - tokenDetail?.alreadyBurn != 0" class="flex items-center justify-center space-x-4">
                        <div class="mb-2">
                            <h2 class="capitalize font-medium text-lg my-5">Total Burn Remains: {{ tokenDetail?.totalBurn - tokenDetail?.alreadyBurn }}</h2>
                            <label for="burnAmount" class="block text-lg">Amount to Burn:</label>
                            <input v-model="burnAmount" type="number" class="p-2 border rounded" placeholder="Enter amount to burn" />
                        </div>
                        <div class="mb-2">
                            <h2 class="capitalize font-medium text-lg my-5">Total share profit:</h2>
                            <label for="burnAmount" class="block text-lg">Amount profit to pay:</label>
                            <input v-model="profitShare" type="number" class="p-2 border rounded" placeholder="Enter amount to burn" />
                        </div>
                    </div>
                </div>
                <div class="mt-4 flex items-center justify-center space-x-4">
                    <button v-if="tokenDetail?.status !== 'ongoing'" @click="showMinting" class="bg-green-500 text-white p-2 rounded">Mint Token</button>
                    <button v-if="tokenDetail?.alreadyBurn !== tokenDetail?.totalBurn" @click="showburning" class="bg-red-500 text-white p-2 rounded ml-2">Burn Token</button>
                    <!-- <button v-if="tokenDetail?.status == 'ongoing' && !tokenDetail?.withdraw && contractBalance > 0" @click="withdrawFunds" class="bg-blue-500 text-white p-2 rounded ml-2">Withdraw</button> -->
                    <button v-if="tokenDetail?.status == 'ongoing'" @click="withdrawFunds" class="bg-blue-500 text-white p-2 rounded ml-2">Withdraw</button>
                </div>
            </div>

            <div v-if="token && (role !== 'admin' || tempAddress !== owner) && tokenDetail?.status != 'close'">
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
import apiMethods from '../../services/apiMothods';

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
            total_burn: null,
            burnAmount: "", 
            buyAmount: "",
            isBurning: false,
            tokenDetail: {},
            profitShare: 0,
            time_burn: null,
            isWalletConnected: false,
            tempAddress: false,
            totalBurn: [1,2,3,4],
            timeBurn: ["Monthly (per Month)", "Quarterly (per 3 month)", "Half Yearly (per 6 month)", "Yearly (per 12 month)"],
            contractBalance: 0
        };
    },
    methods: {
        showMinting() {
            this.isBurning = false
            this.burnAmount = "";
            if (this.mintAmount && this.persentaseProfit) {
                this.mintToken()
            }
        },

        showburning() {
            this.isBurning = true
            this.mintAmount = "";
            this.persentaseProfit = null;
            this.total_burn =  null;
            if (this.burnAmount) {
                this.burnToken()      
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
                const contractBalance = await provider.getBalance(contract.address);

                this.tokenName = name;
                this.tokenSymbol = symbol;
                this.decimals = decimalCount;
                this.owner = ownerAddress.toLowerCase();
                this.tokenPrice = price.toString()
                this.totalSupply = supply.toString()
                this.userBalance = userBalance.toString();
                this.contractBalance = contractBalance
            } catch (error) {
                console.error(error);
                this.status = "Error fetching contract data!";
            }
        },

        async mintToken() {
            if(this.mintAmount < 100){
                this.$toast.open({
                    message: "Amount to Mint Must More Than 100",
                    type: 'warning',
                    duration: 3000,
                    position: 'top-right'
                });
                return
            }
            const contractAddress = this.$route.params.address;

            if (!this.mintAmount || this.mintAmount <= 0) {
                this.status = "Please enter a valid amount to mint!";
                return;
            }
            try {
                const provider = new ethers.providers.Web3Provider(window.ethereum);
                const signer = provider.getSigner();
                const contract = new ethers.Contract(contractAddress, abi, signer);
                const tx = await contract.mint(signer.getAddress(), this.mintAmount);
                await tx.wait();
                this.status = `Minting successful! Transaction Hash: ${tx.hash}`;
                if(tx){
                    const payPerBurn = this.calculateProfitPerBurn(this.mintAmount, this.persentaseProfit, this.total_burn, this.tokenDetail?.tokenPrice)
                    const body = {
                        profitPersen: this.persentaseProfit,
                        status: "ongoing",
                        totalBurn: this.total_burn,
                        totalSupply: this.mintAmount,
                        payPerBurn: payPerBurn,
                        burnTempo: this.time_burn
                    }
                    this.updateTokens(body)
                }
                this.getContractData();
            } catch (error) {
                console.error(error);
                this.status = "Error minting token!";
            }
        },
        calculateProfitPerBurn(totalToken, profitPercentage, totalBurn, price) {
            if (totalBurn === 0) {
                throw new Error("totalBurn tidak boleh nol");
            }
            const totalProfit = totalToken * price * (profitPercentage / 100);
            const profitPerBurn = totalProfit / totalBurn; // Menghindari Math.floor
            const formatFLoat = parseFloat(profitPerBurn.toFixed(6)); // Mengembalikan nilai dengan 6 angka desimal
            return `${formatFLoat}`;
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
                let tx;
                let amountBurnDb;
                if(this.tokenDetail?.totalBurn - this.tokenDetail?.alreadyBurn == 1){
                    const lastCalculateRaw = this.tokenDetail?.totalSupply * this.tokenDetail?.tokenPrice * (this.tokenDetail?.profitPersen / 100) 
                    const lastCalculate = lastCalculateRaw - this.profitShare * this.tokenDetail?.totalBurn
                    const calculateLastProfitShare = this.profitShare + this.tokenDetail?.totalSupply + lastCalculate
                    tx = await contract.burn(this.burnAmount, true, {
                        value: ethers.utils.parseEther(String(calculateLastProfitShare)), // Ether dikirim melalui msg.value
                    });
                    amountBurnDb = calculateLastProfitShare
                }else{
                    tx = await contract.burn(this.burnAmount, false, {
                        value: ethers.utils.parseEther(String(this.profitShare)),
                    });
                    amountBurnDb = this.profitShare
                }
                await tx.wait(); // Tunggu transaksi selesai
                this.status = `Burning successful! Transaction Hash: ${tx.hash}`;
                if(tx){
                    const body = {
                        amount: amountBurnDb
                    }     
                    this.updateAfterBurn()
                }
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
                if(tx){
                    const body = {
                        hash: tx.hash
                    }
                    this.updatewithdrawTokens(body)
                }
                await tx.wait(); // Tunggu transaksi selesai
                this.status = `Withdrawal successful! Transaction Hash: ${tx.hash}`;
            } catch (error) {
                console.error(error);
                this.status = "Error withdrawing funds!";
            }
        },

        async updatewithdrawTokens(body) {
            try {
                const tokenId = this.$route.params.id;
                const endpoint = "/token/update-withdraw/" + tokenId                
                const response = await apiMethods.putData(endpoint, body);
                if (response) {
                    const [title, userId] = response?.msg.split('-');
                    this.$router.push("/token?userId="+userId);
                }
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                    this.$router.push('/login')
                }
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
                this.getContractData();

                const signerAddress = await signer.getAddress();
                const userBalance = await contract.balanceOf(signer.getAddress());
                let userBalanceCurrent = userBalance.toString();
                if(tx){
                    const body = {
                        addressInvestor: signerAddress,
                        holdToken: this.buyAmount,
                        holdAfterBurn: ethers.utils.formatUnits(userBalanceCurrent, "ether"),
                        hash: tx.hash
                    }
                    this.buyTokenCreateDb(body)
                }
                this.buyAmount = "",
                this.status = `Token purchase successful! Transaction Hash: ${tx.hash}`;
            } catch (error) {
                if (error.code === "ACTION_REJECTED") {
                    this.status = "Transaction rejected by user!";
                } else {
                    console.error(error);
                    this.status = "Error purchasing tokens!";
                }
            }
        },

        async updateTokens(body) {
            try {
                const tokenId = this.$route.params.id;
                const endpoint = "/token/update/" + tokenId                
                const response = await apiMethods.putData(endpoint, body);
                if (response) {
                    const [title, userId] = response?.msg.split('-');
                    this.$router.push("/token?userId="+userId);
                }
                this.mintAmount = "";
                this.persentaseProfit = null;
                this.total_burn =  null;
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                    this.$router.push('/login')
                }
            }
        },

        async buyTokenCreateDb(body) {
            try {
                const tokenId = this.$route.params.id;
                const endpoint = "/token/invest-token/" + tokenId                
                const response = await apiMethods.postData(endpoint, body);
                if (response) {
                    const [title, userId] = response?.msg.split('-');
                    this.$router.push("/token?userId="+userId);
                }
                this.buyAmount = "";
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                    this.$router.push('/login')
                }
            }
        },

        async updateAfterBurn() {
            try {
                this.total_burn = 0
                const tokenId = this.$route.params.id;
                const endpoint = "/token/update-burn/" + tokenId                
                const response = await apiMethods.putData(endpoint);
                if (response) {
                    const [title, userId] = response?.msg.split('-');
                    this.$router.push("/token?userId="+userId);
                }
                this.burnAmount = "";
                this.fetchTokens();
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                    this.$router.push('/login')
                }
            }
        },

        async fetchTokens() {
            try {
                const tokenId = this.$route.params.id;
                const response = await apiMethods.getData(`/token/${tokenId}`);
                this.burnAmount = response?.amountPerBurning
                this.profitShare = response?.payPerBurn
                this.tokenDetail = response;
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                    this.$router.push('/login')
                }
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
                        this.getContractData();
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
    },
    mounted() {
        this.fetchTokens();
        this.connectWallets();

        // Monitor wallet address changes
        if (typeof window.ethereum !== "undefined") {
        window.ethereum.on("accountsChanged", (accounts) => {
            if (accounts.length > 0) {
                this.tempAddress = accounts[0]; // Update tempAddress with the new address
                console.log("Wallet address changed to: ", this.tempAddress);
                this.isWalletConnected = true;
                this.getContractData();
            } else {
                this.tempAddress = "";
                this.isWalletConnected = false;
                console.log("No wallet connected.");
            }
        });
        }
    },
};
</script>