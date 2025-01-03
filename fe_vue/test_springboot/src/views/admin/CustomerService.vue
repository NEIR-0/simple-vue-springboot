<template>
    <div class="h-screen w-full flex flex-col items-center justify-center py-14 px-5 relative">
        <h1 class="text-3xl font-semibold text-center">Customer Services</h1>
        <div class="w-1/2 flex items-center justify-center mt-5 mb-10">
            <div class="h-fit w-1/2 bg-[#3D3D3D] flex items-center justify-center p-5 rounded-md">
                <div class="w-full">
                    <input v-model="searchQuery" @input="onSearch" class="h-10 w-full font-semibold text-slate-500 border-2 border-slate-300 px-3 rounded-md focus:ring-0 outline-none" type="text" placeholder="Search by email....." name="search" id="search">
                </div>
            </div>
        </div>
        <div class="w-full h-screen overflow-hidden overflow-y-scroll ms-6">
            <div class="w-1/2 h-full space-y-4 relative">
                <div class="w-full h-fit grid grid-cols-3 gap-4 bg-[#578E7E] p-5 rounded-md">
                    <div v-for="user in sortedUsers" :key="user.id" class="w-full h-fit flex items-center justify-center flex-col space-y-4">
                        <button 
                            @click="changeUser(user)" 
                            :class="`${userNewMessages.includes(user.id) ? 'bg-red-300' : 'bg-white'} w-full h-10 shadow-lg rounded-md flex items-center justify-center border-2 border-slate-200`"
                        >
                            {{user?.email}}
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- chat -->
        <div v-if="showChatBox" class="absolute bottom-5 right-10 w-fit h-fit">
            <ChatBox 
                :messages="messages" 
                :sendMessageUser="sendMessageUser" 
                :newMessage="newMessage" 
                :isAdmin="true"
                :currentEmail="currentEmail"
                :closeChatUser="closeChatUser"
                @update:newMessage="newMessage = $event"
            />
        </div>

        <!-- Notification Badge -->
        <div v-if="newMessageNotification" class="absolute top-14 right-10 bg-red-500 text-white p-2 rounded-full">
            New message from user!
        </div>
    </div>
</template>

<script>
import apiMethods from "../../services/apiMothods";
import ChatBox from "../../components/chatBox.vue";
import WebSocketService from '../../services/WebSocketService';

export default {
    components: {
        ChatBox
    },
    data() {
        return {
            users: [],
            currentUserId: null,
            messages: [],
            newMessage: "",
            newMessageNotification: false,
            currentUserMessages: [],
            currentEmail: "",
            userNewMessages: [],
            showChatBox: false,
            searchQuery: "",
            currentPage: 0,
            totalPages: 0,
            pageSize: 10,
        }
    },
    methods: {
        onSearch() {
            this.currentPage = 0;
            this.getAllUsers(this.searchQuery);
        },
        async getAllUsers(query = "") {
            try {
                const params = {
                    page: this.currentPage,
                    size: this.pageSize,
                    search: query,
                }
                const response = await apiMethods.getData("/users/without-admin", params);
                this.users = response;
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                this.$router.push('/login')
                }
            }
        },

        changeUser(user) {
            this.currentUserId = user?.id;
            this.currentEmail = user?.email;
            // Menghapus userId yang dipilih dari userNewMessages
            this.userNewMessages = this.userNewMessages.filter(id => id !== user.id);
            // Set notifikasi ke false setelah memilih user
            this.newMessageNotification = false;
            this.showChatBox = true
            this.getAllMessageById();
        },

        closeChatUser() {
            this.showChatBox = !this.showChatBox
        },

        async getAllMessageById() {
            try {
                const response = await apiMethods.getDataById("/message/" + this.currentUserId);
                this.messages = response;
                this.currentUserMessages = response;
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message === "Invalid or expired token") {
                this.$router.push('/login')
                }
            }
        },

        async sendMessageUser() {
            if (this.newMessage.trim() === "") return;
            try {
                const body = {
                    content: this.newMessage,
                    sendTo: this.currentUserId
                };
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
            }
        },

        RealTimeMessager(newMessage) {
            // Cek apakah pesan baru datang dari pengguna yang berbeda
            if (this.currentUserId !== newMessage[0]?.senderId?.id) {
                this.newMessageNotification = true;
            }
            let check = this.userNewMessages.includes(newMessage[0]?.senderId?.id)
            if(!check && this.currentUserId !== newMessage[0]?.senderId ?.id){
                this.userNewMessages.push(newMessage[0]?.senderId?.id)
            }
            this.currentUserId === newMessage[0]?.senderId ?.id
            if(this.currentUserId === newMessage[0]?.senderId ?.id){
                this.messages = newMessage
            }
        },

        SendRealTimeMessager(payload) {
            WebSocketService.UpdateSendMessageRealTime(payload);
        },
    },

    watch: {
        currentUserId(newValue, oldValue) {
            if (newValue !== oldValue) {
                this.getAllMessageById();
            }
        }
    },

    computed: {
        sortedUsers() {
            return this.users.sort((a, b) => {
                // Jika a ada di userNewMessages dan b tidak, maka a akan di atas
                if (this.userNewMessages.includes(a.id) && !this.userNewMessages.includes(b.id)) {
                    return -1;
                }
                // Jika b ada di userNewMessages dan a tidak, maka b akan di atas
                if (!this.userNewMessages.includes(a.id) && this.userNewMessages.includes(b.id)) {
                    return 1;
                }
                return 0; // Jika keduanya ada atau tidak ada di userNewMessages, urutan tetap
            });
        }
    },

    mounted() {
        this.getAllUsers();
        WebSocketService.responseSendMessageRealTime(this.RealTimeMessager);
    },

    beforeDestroy() {
        WebSocketService.disconnect();
    },
}
</script>

