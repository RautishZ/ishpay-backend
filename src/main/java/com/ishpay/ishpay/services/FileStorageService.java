package com.ishpay.ishpay.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploaded-files");

    public FileStorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    public String storeFile(String id, MultipartFile file, String type) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            // Create a directory for each file type
            Path typeDirectory = rootLocation.resolve(type);
            if (!Files.exists(typeDirectory)) {
                Files.createDirectory(typeDirectory);
            }

            // Generate a new filename based on ID and type
            String filename = id + "-" + type + ".jpg";
            Path destinationFile = typeDirectory.resolve(Paths.get(filename))
                    .normalize().toAbsolutePath();
            file.transferTo(destinationFile.toFile());

            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}
