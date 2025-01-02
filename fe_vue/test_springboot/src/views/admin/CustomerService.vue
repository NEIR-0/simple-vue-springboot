<template>
    <div class="h-screen w-full flex items-center justify-center py-14 px-5 relative">
        <div class="w-1/2 h-screen">
            <h1 class="text-3xl font-semibold">Customer Services</h1>
            <div class="w-full h-fit flex flex-col items-center justify-center mt-10 space-y-2">
                <div v-for="user in sortedUsers" :key="user.id" class="w-full h-fit flex items-center justify-center flex-col space-y-4">
                    <button 
                        @click="changeUser(user)" 
                        class=""
                        :class="`${userNewMessages.includes(user.id) ? 'bg-red-300' : 'bg-white'} w-1/4 h-10 shadow-lg rounded-md flex items-center justify-center border-2 border-slate-200`"
                    >
                        {{user?.email}}
                    </button>
                </div>
            </div>
        </div>

        <!-- chat -->
        <div class="absolute bottom-5 right-10 w-fit h-fit">
            <ChatBox 
                :messages="messages" 
                :sendMessageUser="sendMessageUser" 
                :newMessage="newMessage" 
                :isAdmin="true"
                :currentEmail="currentEmail"
                @update:newMessage="newMessage = $event"
            />
        </div>

        <!-- Notification Badge -->
        <div v-if="newMessageNotification" class="absolute top-5 right-10 bg-red-500 text-white p-2 rounded-full">
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
            userNewMessages: []
        }
    },
    methods: {
        async getAllUsers() {
            try {
                const response = await apiMethods.getData("/users");
                this.users = response;
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message) {
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
            this.getAllMessageById();
        },

        async getAllMessageById() {
            try {
                const response = await apiMethods.getDataById("/message/" + this.currentUserId);
                this.messages = response;
                this.currentUserMessages = response;
            } catch (error) {
                console.error('Error send message data:', error);
                if (error?.message) {
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
                if (error?.message) {
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

