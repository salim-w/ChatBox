package com.example.chatbox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadDirectory = "uploads";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .exposedHeaders("Content-Disposition") // Important pour les téléchargements
                .allowCredentials(true)
                .maxAge(3600); // Cache la configuration CORS pendant 1 heure
    }
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + Paths.get(uploadDir).toAbsolutePath().toString() + "/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        // Pour les images
        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations("file:" + Paths.get(uploadDir, "images").toAbsolutePath().toString() + "/");

        // Pour les PDFs
        registry.addResourceHandler("/uploads/documents/**")
                .addResourceLocations("file:" + Paths.get(uploadDir, "documents").toAbsolutePath().toString() + "/");
    }
}