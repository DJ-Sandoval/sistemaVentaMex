package com.VentaMex.apiVentaMex.presentation.api;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(BackupAPI.BASE_URL)
public interface BackupAPI {
    String BASE_URL="/api/backup";

    @PostMapping("/create")
    ResponseEntity<String> createBackup();

    @GetMapping("/list")
    public ResponseEntity<List<String>> listBackups();

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadBackup(@PathVariable String fileName);


}
