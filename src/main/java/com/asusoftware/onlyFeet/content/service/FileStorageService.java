package com.asusoftware.onlyFeet.content.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class FileStorageService {

    public List<String> storeFiles(List<MultipartFile> files, UUID creatorId) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            validateFileType(file);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path userDir = Paths.get("uploads", creatorId.toString());
            Path fullPath = userDir.resolve(filename);

            try {
                Files.createDirectories(userDir);
                file.transferTo(fullPath);
                urls.add("/uploads/" + creatorId + "/" + filename);
            } catch (IOException e) {
                throw new RuntimeException("Eroare la salvarea fișierului: " + file.getOriginalFilename());
            }
        }

        return urls;
    }

    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
            throw new IllegalArgumentException("Fișierul trebuie să fie imagine sau video: " + file.getOriginalFilename());
        }
    }
}