package com.ms.spring.starter.service;

import com.ms.spring.starter.dto.FileUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public FileUploadResponse storeFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Validate content type
        List<String> allowedTypes = List.of("image/png", "image/jpeg", "application/pdf");
        if (!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file type");
        }

        // Clean filename
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";

        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex);
        }

        // Rename file (UUID)
        String fileName = UUID.randomUUID() + extension;

        try {
            Path targetLocation = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new FileUploadResponse(
                    fileName,
                    file.getContentType(),
                    file.getSize());

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file", ex);
        }
    }

    public Resource loadAsResource(String filename) {
        try {
            Path filePath = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found", e);
        }
    }
}
