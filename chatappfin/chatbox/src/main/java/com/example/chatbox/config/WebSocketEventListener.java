package com.example.chatbox.config;

import com.example.chatbox.controller.ChatMessage;
import com.example.chatbox.entities.User;
import com.example.chatbox.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private  SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserService userService; // Ajoutez cette ligne si vous voulez lier à un utilisateur

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        log.info("New web socket connection established");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null) {
            log.info("User disconnected: {}", username);

            // Créer un message de déconnexion
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setTimestamp(LocalDateTime.now());

            // Optionnel : Si vous voulez lier le message à l'utilisateur
          try {
                User user = userService.findByUsername(username);
                chatMessage.setUser(user);
            } catch (Exception e) {
                log.warn("Could not find user for disconnection message: {}", username);
            }

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}