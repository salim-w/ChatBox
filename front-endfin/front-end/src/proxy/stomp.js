import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

class StompClient extends Client {
    constructor() {
        super();
        this.WEB_SOCKET_BASE_URL = 'http://localhost:8080/ws';
        this.messageCallbacks = new Set();

        this.webSocketFactory = () => {
            return new SockJS(this.WEB_SOCKET_BASE_URL);
        };

        this.reconnectDelay = 5000;
        this.heartbeatIncoming = 4000;
        this.heartbeatOutgoing = 4000;

        this.onConnect = () => {
            console.log('Connected to WebSocket');
            const username = localStorage.getItem('username');

            // Subscribe to global channel
            this.subscribe('/topic/public', this.handleMessage);

            // Subscribe to user's private channel
            if (username) {
                this.subscribe(`/user/${username}/private`, this.handleMessage);
                this.subscribe(`/topic/chat/${username}`, this.handleMessage);
            }
        };

        this.onStompError = (frame) => {
            console.error('STOMP error:', frame);
        };
    }

    handleMessage = (message) => {
        try {
            const parsedMessage = JSON.parse(message.body);
            console.log('Received:', parsedMessage);
            this.messageCallbacks.forEach(callback => callback(parsedMessage));
        } catch (error) {
            console.error('Message parsing error:', error);
        }
    }

    addMessageCallback(callback) {
        this.messageCallbacks.add(callback);
    }

    removeMessageCallback(callback) {
        this.messageCallbacks.delete(callback);
    }

    sendMessage(message) {
        if (!this.connected) {
            throw new Error('WebSocket not connected');
        }

        this.publish({
            destination: '/app/chat.send',
            body: JSON.stringify(message)
        });
    }
}

export default StompClient;