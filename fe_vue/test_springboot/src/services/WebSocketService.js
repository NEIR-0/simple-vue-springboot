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

  responseUpdateTransactionsRealTime(callback) {
    const socket = new SockJS('http://localhost:8081/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null // disable log

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      
      this.stompClient.subscribe('/topic/updateTransactions', (messageOutput) => {
        // console.log('Received message: ' + messageOutput.body, ">>>>>>>???");
        const newMessage = JSON.parse(messageOutput.body);
        
        callback(newMessage);
      });
    });
  }

  updateTransactionsRealTime() {
    if (this.stompClient) {
      this.stompClient.send('/app/updateTransactions');
    }
  }

  responseUpdateDataDeleteProductsRealTime(callback) {
    const socket = new SockJS('http://localhost:8081/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null // disable log

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      
      this.stompClient.subscribe('/topic/updateDataRealTime', (messageOutput) => {
        console.log('Received message: ' + messageOutput.body, ">>>>>>>???");
        const newMessage = JSON.parse(messageOutput.body);
        callback(newMessage);
      });
    });
  }

  UpdateDataDeleteProductsRealTime() {
    if (this.stompClient) {
      this.stompClient.send('/app/updateDataRealTime');
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