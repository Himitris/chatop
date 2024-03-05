package com.chatop.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.io.IOException;
import java.io.InputStream;

@Service
public class StorageService {

    private final Path rootLocation;

    public StorageService(@Value("${storage.location}") String storageLocation) {
        this.rootLocation = Paths.get(storageLocation);
    }

    public String save(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Échec de l'enregistrement du fichier vide.");
        }

        // Générer un nom de fichier unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension; // Ajout d'un UUID


        Path destinationFile = this.rootLocation.resolve(
                Paths.get(newFileName))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new IOException("Impossible d'enregistrer le fichier en dehors du répertoire actuel.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        return destinationFile.toString();
    }
}