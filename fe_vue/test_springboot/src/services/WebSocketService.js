import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

class WebSocketService {
  constructor() {
    this.stompClient = null;
  }

  // Method untuk menghubungkan ke server WebSocket
  connect(callback) {
    const socket = new SockJS('http://localhost:8081/ws'); // Sesuaikan dengan endpoint WebSocket yang kamu konfigurasi di Spring Boot
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);

      // Setelah koneksi berhasil, subscribe ke topic untuk menerima pesan
      this.stompClient.subscribe('/topic/messages', (messageOutput) => {
        console.log('Received message: ' + messageOutput.body, ">>>>>>>???");
        const newMessage = JSON.parse(messageOutput.body);
        callback(newMessage);
      });
    });
  }

  // Method untuk mengirim pesan ke server
  sendMessage(message) {
    if (this.stompClient) {
      this.stompClient.send('/app/send', {}, JSON.stringify(message));
    }
  }

  // Disconnect WebSocket connection
  disconnect() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }
}

export default new WebSocketService();