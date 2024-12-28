// ChatMessage.java
package com.example.chatbox.controller;
import com.example.chatbox.entities.Message;
import com.example.chatbox.entities.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MessageType type;

    private String content;

    private String sender;  // On garde sender comme backup

    private LocalDateTime timestamp;

  @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;  // Ajout de la relation avec User

  //  @OneToOne(cascade = CascadeType.ALL)
  //  @JoinColumn(name = "file_info_id")
    private String fileContent;

    private String fileType; // Pour distinguer "image" ou "pdf"

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        PRIVATE,
        NOTIFICATION,
        FILE
    }


    // Méthode pour convertir ChatMessage en Message
    public Message toMessage() {
        Message message = new Message();
        message.setContent(this.content);
        message.setTimestamp(this.timestamp);
        // Note: vous devrez gérer l'utilisateur et la room selon votre logique
        return message;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


