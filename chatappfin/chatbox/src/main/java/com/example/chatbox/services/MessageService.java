package com.example.chatbox.services;

import com.example.chatbox.entities.Message;
import com.example.chatbox.entities.User;
import com.example.chatbox.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {


    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        // Enregistrer le message dans la base de données
        return messageRepository.save(message);
    }

   /*
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public") // Diffuser à tous les utilisateurs connectés
    public Message sendMessage(Message message) {

        // Ici, vous pouvez ajouter un utilisateur lorsque vous avez la logique d'authentification
        // Exemple : message.setUser(currentUser);

        // Enregistrer le message dans la base de données
        messageRepository.save(message);

        // Retourner le message pour qu'il soit diffusé à tous les abonnés
        return message;
    }
*/

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }



}
