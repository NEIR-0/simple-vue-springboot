<template>
    <div class="w-[550px] h-[700px] shadow-lg border-4 border-slate-500 rounded-lg bg-white relative px-3 pt-3">
      <button @click="closeChatUser" class="absolute top-2 right-5 text-xl text-slate-500 font-bold duration-300 ease-in-out hover:text-slate-800">X</button>
      <!-- chatbox -->
      <div class="w-full h-10 flex items-center justify-between">
          <h1>{{currentEmail ? currentEmail : "Admin"}}</h1>
      </div>
      <div 
        ref="chatBox" 
        class="w-full h-[84%] bg-gray-100 overflow-y-auto flex flex-col space-y-3 p-3"
      >
        <div
            v-if="messages?.length > 0"
            v-for="(message, index) in sortedMessages"
            :key="index"
            class="flex"
            :class="isAdmin ? message?.sender === 'admin' ? 'justify-start' : 'justify-end' : message?.sender === 'user' ? 'justify-start' : 'justify-end'"
        >
          <div
            class="max-w-[70%] px-4 py-2 rounded-lg text-white font-semibold"
            :class="message?.sender === 'user' ? 'bg-green-500' : 'bg-red-500'"
          >
            <p class="w-full break-words text-base">
              {{ message.content }}
            </p>
            <span class="block text-xs text-gray-300 mt-1">
              {{ formatTime(message.createdAt) }}
            </span>
          </div>
        </div>
      </div>
  
      <!-- inputUsers -->
      <div class="w-full h-10 absolute left-0 bottom-4 flex items-center justify-between px-3">
        <input
          ref="inputMessage"
          :value="newMessage"
          class="h-10 w-[80%] font-semibold text-slate-500 border-2 border-slate-300 px-2 rounded-md focus:ring-0 outline-none"
          type="text"
          placeholder="Enter your chat..."
          @input="$emit('update:newMessage', $event.target.value)"
          @keydown.enter="sendMessageUser"
        />
        <button
          class="w-[19%] h-full flex items-center justify-center rounded-md font-semibold text-slate-800 bg-[#ffde09] hover:bg-[#ffe74e] duration-300 ease-in-out"
          @click="sendMessageUser"
        >
          Send
        </button>
      </div>
    </div>
</template>
  
<script>
export default {
    props: {
        messages: {
            type: Array
        },
        newMessage: {
            type: String
        },
        isAdmin: {
            type: Boolean
        },
        sendMessageUser:{
            type: Function,
            required: true
        },
        closeChatUser:{
            type: Function,
            required: true
        },
        currentEmail:{
            type: String,
        },
    },
    computed: {
      sortedMessages() {
        return this.messages.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
      },
    },
    methods: {
        formatTime(timestamp) {
            const date = new Date(timestamp);
            return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        },
        scrollToBottom() {
            // Scroll ke bawah setelah render
            this.$nextTick(() => {
                const chatBox = this.$refs.chatBox;
                chatBox.scrollTop = chatBox.scrollHeight;
            });
        }
    },
    mounted() {
        // Fokus ke input saat komponen dipasang
        this.$refs.inputMessage.focus();
        this.scrollToBottom();  // Scroll otomatis ke bawah saat komponen pertama kali dimuat
    },
    updated() {
        // Scroll ke bawah setiap kali ada pesan baru
        this.scrollToBottom();
    },
};
</script>
