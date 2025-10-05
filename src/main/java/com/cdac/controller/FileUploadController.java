package com.cdac.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class FileUploadController {

    private static final String UPLOAD_DIR = "uploads/";
    private static final String IMAGE_DIR = UPLOAD_DIR + "images/";
    private static final String DOCUMENT_DIR = UPLOAD_DIR + "documents/";

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        
        try {
            // Create directories if they don't exist
            String uploadPath = type.equals("image") ? IMAGE_DIR : DOCUMENT_DIR;
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // Return file path
            String filePathString = uploadPath + filename;
            log.info("File uploaded successfully: {}", filePathString);
            
            return ResponseEntity.ok(new FileUploadResponse(filePathString));
            
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new FileUploadResponse("Error uploading file"));
        }
    }

    public static class FileUploadResponse {
        private String filePath;

        public FileUploadResponse(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
