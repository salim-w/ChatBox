package com.example.chatbox.repository;


import com.example.chatbox.controller.ChatMessage;
import com.example.chatbox.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Vous pouvez ajouter des méthodes de requête personnalisées ici si nécessaire

    // Ajouter des méthodes pour récupérer les messages par utilisateur
    List<ChatMessage> findByUserOrderByTimestampDesc(User user);
    List<ChatMessage> findByUserUsernameOrderByTimestampDesc(String username);

}