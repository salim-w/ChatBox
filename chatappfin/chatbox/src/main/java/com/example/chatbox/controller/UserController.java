package com.example.chatbox.controller;

import com.example.chatbox.entities.User;
import com.example.chatbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Page de connexion, redirige si déjà authentifié
    @GetMapping("/login")
    public String loginPage() {
        if (isAuthenticated()) {
            return "redirect:/home";  // Redirige vers la page d'accueil si déjà connecté
        }
        return "login";  // Affiche la page de connexion si non authentifié
    }

    // Page d'inscription
    @GetMapping("/register")
    public String registerPage() {
        return "register";  // Affiche la page d'enregistrement
    }

    // Enregistrement d'un utilisateur
    @PostMapping("/register")
    public String registerUser(User user, Model model) {

        userService.saveUser(user);

        return "redirect:/login";  // Redirige vers la page de connexion après enregistrement
    }

    // Méthode pour vérifier si l'utilisateur est authentifié
    private boolean isAuthenticated() {
        // Implémenter votre logique pour vérifier l'authentification de l'utilisateur
        // Par exemple, vérifier dans la session ou un objet utilisateur
        return false;  // Remplacer par la logique d'authentification
    }
}
