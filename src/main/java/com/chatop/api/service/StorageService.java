package com.chatop.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.io.InputStream;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("C:\\Users\\Himitris\\Documents\\OpenClassRoom\\P3\\chatop\\storage");

    public String save(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Échec de l'enregistrement du fichier vide.");
        }

        // This may need to be enhanced if files with the same name are to be handled
        Path destinationFile = this.rootLocation.resolve(
                Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new IOException("Impossible d'enregistrer le fichier en dehors du répertoire actuel.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        return destinationFile.toString();
    }
}