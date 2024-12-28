package com.example.chatbox.services;

import com.example.chatbox.entities.User;
import com.example.chatbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import com.example.chatbox.entities.StatusUser;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public User register(String email, String password) {
        // Implémentez la logique d'enregistrement
        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // En production, vous devez hasher le mot de passe
        return userRepository.save(user);
    }
    public User saveUser(User user) {
        // Logique pour sauvegarder l'utilisateur
        return userRepository.save(user);
    }
    public void updateUserStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setLastUpdated(LocalDateTime.now());
        userRepository.save(user);
    }



    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }




    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}

