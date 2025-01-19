import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
const baseUrl = import.meta.env.VITE_BASE_API_URL; 

class WebSocketService {
  constructor() {
    this.stompClient = null;
  }

  // Method untuk menghubungkan ke server WebSocket
  connect(callback) {
    const socket = new SockJS(baseUrl + '/ws'); // Sesuaikan dengan endpoint WebSocket yang kamu konfigurasi di Spring Boot
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
    });
  }

  responseSendMessageRealTime(callback) {
    const socket = new SockJS(baseUrl + '/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null // disable log

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      
      this.stompClient.subscribe('/topic/responseMessage', (messageOutput) => {
        // console.log('Received message: ' + messageOutput.body, ">>>>>>>???");
        const newMessage = JSON.parse(messageOutput.body);
        callback(newMessage);
      });
    });
  }

  UpdateSendMessageRealTime(payload) {   
    if (this.stompClient) {
      this.stompClient.send(
        '/app/responseMessage',
        {},                    
        JSON.stringify(payload)
      );
    }
  }

  responseUpdateTransactionsRealTime(callback) {
    const socket = new SockJS(baseUrl + '/ws');
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
    const socket = new SockJS(baseUrl + '/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null // disable log

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      
      this.stompClient.subscribe('/topic/updateDataRealTime', (messageOutput) => {
        // console.log('Received message: ' + messageOutput.body, ">>>>>>>???");
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

  UpdateNotificationRealTime(payload) {   
    if (this.stompClient) {
      this.stompClient.send(
        '/app/responseNotifications',
        {},                    
        JSON.stringify(payload)
      );
    }
  }

  responseNotificationRealTime(callback) {
    const socket = new SockJS(baseUrl + '/ws');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null // disable log

    this.stompClient.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      
      this.stompClient.subscribe('/topic/responseNotifications', (messageOutput) => {
        const newMessage = JSON.parse(messageOutput.body);
        callback(newMessage);
      });
    });
  }

  // Disconnect WebSocket connection
  disconnect() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }
}

export default new WebSocketService();