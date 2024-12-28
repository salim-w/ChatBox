package com.example.chatbox.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;

    // Initialisation du répertoire de stockage
    public FileStorageServiceImpl() {
        this.rootLocation = Paths.get("uploads");
        try {
            Files.createDirectories(rootLocation);  // Crée le répertoire s'il n'existe pas
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);  // Réinitialise le répertoire de stockage
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            String filename = "";

            // Extraction du nom de fichier et de l'extension
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                filename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            } else {
                filename = originalFilename != null ? originalFilename : "file";
            }

            // Générer un nom de fichier unique
            String uniqueFilename = filename + "_" + System.currentTimeMillis() + extension;
            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFilename))
                    .normalize()
                    .toAbsolutePath();

            // Vérification si le fichier ne sort pas du répertoire actuel
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            // Enregistrement du fichier
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            // Vérification si le fichier existe et peut être lu
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());  // Supprimer tous les fichiers dans le répertoire de stockage
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            // Charger tous les fichiers dans le répertoire de stockage
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))  // Filtrer le répertoire racine
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            // Charger un fichier spécifique en tant que ressource
            Path filePath = rootLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Vérification si le fichier peut être lu
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }
}
