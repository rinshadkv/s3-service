package com.assignment.s3.controller;


import com.assignment.s3.Dto.FileInfo;
import com.assignment.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @GetMapping("/search")
    public ResponseEntity<List<FileInfo>> searchFiles(
            @RequestParam String userName,
            @RequestParam String q) {
        List<FileInfo> files = s3Service.searchFilesInUserFolder(userName, q);
        return ResponseEntity.ok(files);
    }


    @PostMapping("/upload/{userName}")
    public ResponseEntity<String> uploadFile(
            @PathVariable String userName,
            @RequestParam("file") MultipartFile file) throws IOException {

        s3Service.uploadFile(userName, file.getOriginalFilename(), file.getInputStream());
        return ResponseEntity.ok("File uploaded successfully");
    }
}
