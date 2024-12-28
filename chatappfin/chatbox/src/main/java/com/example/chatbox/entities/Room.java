package com.example.chatbox.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Indique si la salle est publique ou privée
    private boolean isPrivate;

    // Liste des utilisateurs dans la salle
    @ManyToMany
    @JoinTable(
            name = "room_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    // Liste des messages dans la salle
    @OneToMany(mappedBy = "room")
    private List<Message> messages;

    // Getter pour id
    public Long getId() {
        return id;
    }

    // Setter pour id
    public void setId(Long id) {
        this.id = id;
    }

    // Getter pour name
    public String getName() {
        return name;
    }

    // Setter pour name
    public void setName(String name) {
        this.name = name;
    }

    // Getter pour isPrivate
    public boolean isPrivate() {
        return isPrivate;
    }

    // Setter pour isPrivate
    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    // Getter pour users
    public List<User> getUsers() {
        return users;
    }

    // Setter pour users
    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Getter pour messages
    public List<Message> getMessages() {
        return messages;
    }

    // Setter pour messages
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }
}
