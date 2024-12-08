<template>
    <div>
      <h2>WebSocket with STOMP in Vue</h2>
      <div>
        <input v-model="message" placeholder="Enter your message" />
        <button @click="sendMessage">Send</button>
      </div>
      <div>
        <h3>Messages:</h3>
        <ul>
            <li v-for="msg in messages" :key="msg.from">{{ msg.from }}: {{ msg.content }}</li>
        </ul>
      </div>
    </div>
</template>

<script>
import WebSocketService from '../../services/WebSocketService';

export default {
    data() {
        return {
            message: '',
            messages: [],
        };
    },
    mounted() {
        WebSocketService.connect(this.addMessage);
    },
    methods: {
        sendMessage() {
            if (this.message.trim()) {
                    const message = {
                    from: 'Client',
                    content: this.message,
                };
                WebSocketService.sendMessage(message);
                this.message = ''; // Clear input field
            }
        },
        // Menambahkan pesan baru ke array messages di Vue dan menambahkan ke localStorage
        addMessage(newMessage) {
         this.messages.push(newMessage); // Update pesan ke array messages
        }
    },
    beforeDestroy() {
        WebSocketService.disconnect();
    },
};
</script>
  