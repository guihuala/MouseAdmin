package com.example.adminapi.controller;

import com.example.adminapi.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    @GetMapping
    public ResponseEntity<FileSystemResource> backupDatabase() {
        try {
            Path backupFilePath = backupService.backupDatabase();
            FileSystemResource fileSystemResource = new FileSystemResource(backupFilePath.toFile());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + backupFilePath.getFileName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileSystemResource);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
