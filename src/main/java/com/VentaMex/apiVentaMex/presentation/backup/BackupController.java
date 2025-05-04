package com.VentaMex.apiVentaMex.presentation.backup;

import com.VentaMex.apiVentaMex.presentation.api.BackupAPI;
import com.VentaMex.apiVentaMex.util.backup.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class BackupController implements BackupAPI {

    @Autowired
    private BackupService backupService;

    @Override
    public ResponseEntity<String> createBackup() {
        try {
            String backupPath = backupService.createBackup();
            return ResponseEntity.ok("Backup created successfully: " + backupPath);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Failed to create backup: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<String>> listBackups() {
        try {
            List<String> backups = backupService.listBackups();
            return ResponseEntity.ok(backups);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadBackup(String fileName) {
        try {
            Resource resource = backupService.getBackupFile(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
