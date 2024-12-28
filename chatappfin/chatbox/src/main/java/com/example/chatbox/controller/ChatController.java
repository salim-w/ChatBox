package com.example.chatbox.controller;

import com.example.chatbox.entities.User;
import com.example.chatbox.repository.ChatMessageRepository;
import com.example.chatbox.services.FileStorageService;

import com.example.chatbox.services.MessageService;
import com.example.chatbox.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller

public class ChatController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserService userService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        if (chatMessage == null) return null;

        User user = userService.findByUsername(chatMessage.getSender());
        chatMessage.setUser(user);

        if (chatMessage.getType() == ChatMessage.MessageType.FILE) {
            if (chatMessage.getFileContent() != null) {
                String fileName = chatMessage.getContent();
                String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                chatMessage.setFileType(fileExtension.equals(".pdf") ? "pdf" : "image");
                chatMessage.setContent("File: " + fileName);
            }
        } else {
            if (chatMessage.getContent() == null || chatMessage.getContent().trim().isEmpty()) {
                return null;
            }
            chatMessage.setContent(chatMessage.getContent().trim());
        }

        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    // Nouvelle méthode pour récupérer les messages d'un utilisateur
    @GetMapping("/api/messages/user/{username}")
    public ResponseEntity<List<ChatMessage>> getUserMessages(@PathVariable String username) {
        List<ChatMessage> messages = chatMessageRepository.findByUserUsernameOrderByTimestampDesc(username);
        return ResponseEntity.ok(messages);
    }

    // Méthode existante modifiée pour inclure l'utilisateur dans la réponse
    @GetMapping("/api/messages")
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        List<ChatMessage> messages = chatMessageRepository.findAll();
        return ResponseEntity.ok(messages);
    }



     @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

            // Validate file type
            if (!fileExtension.matches("\\.(jpg|jpeg|png|gif|pdf)$")) {
                return ResponseEntity.badRequest().build();
            }

            // Choose directory based on file type
            String subDir = fileExtension.equals(".pdf") ? "documents" : "images";
            String uploadPath = Paths.get(uploadDir, subDir).toString();

            Files.createDirectories(Paths.get(uploadPath));

            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path path = Paths.get(uploadPath, uniqueFileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(subDir + "/")
                    .path(uniqueFileName)
                    .toUriString();

            Map<String, String> response = new HashMap<>();
            response.put("fileUrl", fileUrl);
            response.put("fileType", fileExtension.equals(".pdf") ? "pdf" : "image");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/uploads/{type}/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String type, @PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, type, fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType;
                String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

                switch (extension) {
                    case ".pdf":
                        contentType = "application/pdf";
                        break;
                    case ".png":
                        contentType = "image/png";
                        break;
                    case ".jpg":
                    case ".jpeg":
                        contentType = "image/jpeg";
                        break;
                    case ".gif":
                        contentType = "image/gif";
                        break;
                    default:
                        contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}