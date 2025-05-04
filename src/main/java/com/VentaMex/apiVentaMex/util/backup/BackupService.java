package com.VentaMex.apiVentaMex.util.backup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BackupService {

    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    private final Path backupDirectory = Paths.get("backups");

    public BackupService() {
        try {
            Files.createDirectories(backupDirectory);
        } catch (IOException e) {
            logger.error("Failed to create backup directory", e);
        }
    }

    public String createBackup() throws IOException, InterruptedException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFileName = "backup_bdventamex_" + timestamp + ".sql";
        Path backupPath = backupDirectory.resolve(backupFileName);

        // Extract database name from URL
        String databaseName = databaseUrl.substring(databaseUrl.lastIndexOf("/") + 1);

        // Construct mysqldump command
        String[] command = {
                "mysqldump",
                "--user=" + databaseUsername,
                "--password=" + databasePassword,
                databaseName
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectOutput(backupPath.toFile());

        logger.info("Starting database backup: {}", backupFileName);

        Process process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            logger.info("Backup created successfully: {}", backupFileName);
            return backupPath.toString();
        } else {
            logger.error("Backup creation failed with exit code: {}", exitCode);
            throw new IOException("Failed to create backup");
        }
    }

    public List<String> listBackups() throws IOException {
        return Files.list(backupDirectory)
                .filter(path -> path.toString().endsWith(".sql"))
                .map(path -> path.getFileName().toString()) // Return only filename
                .collect(Collectors.toList());
    }

    public Resource getBackupFile(String fileName) throws IOException {
        Path filePath = backupDirectory.resolve(fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("Backup file not found: " + fileName);
        }
        return new UrlResource(filePath.toUri());
    }
}