import { useEffect, useState, useCallback } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

export const useSocket = (username) => {
  const [client, setClient] = useState(null);
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    if (!username) return;

    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      console.log('Connected to WebSocket');
      setClient(stompClient);

      stompClient.subscribe('/topic/messages', (message) => {
        const newMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, newMessage]);
      });

      stompClient.subscribe(`/user/${username}/queue/private`, (message) => {
        const newMessage = JSON.parse(message.body);
        setMessages((prevMessages) => [...prevMessages, newMessage]);
      });
    });

    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.disconnect();
      }
    };
  }, [username]);

  const sendMessage = useCallback((message) => {
    if (client && client.connected) {
      client.send("/app/sendMessage", {}, JSON.stringify(message));
    }
  }, [client]);

  return { messages, sendMessage };
};

