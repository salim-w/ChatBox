import React, { createContext, useContext, useState, useCallback } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

const MessageContext = createContext();

export const useMessageContext = () => useContext(MessageContext);

export const MessageProvider = ({ children }) => {
  const [client, setClient] = useState(null);

  const connect = useCallback(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
      console.log('Connected to WebSocket');
      setClient(stompClient);
    });

    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.disconnect();
      }
    };
  }, []);

  const publishMessage = useCallback((destination, message) => {
    if (client && client.connected) {
      client.send(destination, {}, JSON.stringify(message));
    } else {
      console.error('WebSocket is not connected');
    }
  }, [client]);

  return (
    <MessageContext.Provider value={{ connect, publishMessage }}>
      {children}
    </MessageContext.Provider>
  );
};

