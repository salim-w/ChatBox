package com.example.chatbox.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Message content cannot be empty")
    @Size(max = 500, message = "Message content cannot exceed 500 characters")
    private String content;

    private LocalDateTime timestamp;


    @ManyToOne
    @JoinColumn(name = "room_id")  // Nom de la clé étrangère dans la table des messages
    private Room room;
    @ManyToOne
    @JoinColumn(name = "user_id")  // Nom de la clé étrangère pour l'utilisateur
    private User user ;  // Le champ 'sender' représente l'utilisateur qui a envoyé ce message


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Message content cannot be empty") @Size(max = 500, message = "Message content cannot exceed 500 characters") String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "Message content cannot be empty") @Size(max = 500, message = "Message content cannot exceed 500 characters") String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
