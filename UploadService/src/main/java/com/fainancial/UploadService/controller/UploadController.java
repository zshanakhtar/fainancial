package com.fainancial.UploadService.controller;

import com.fainancial.UploadService.dto.response.UploadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import com.fainancial.UploadService.service.UploadService;

import static com.fainancial.UploadService.constants.UploadConstants.*;

@RestController
@RequestMapping(FAINANCE)
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(value = UPLOAD, produces = "application/json")
    @Operation(summary = "upload", description = "uploads a file")
    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file, @RequestParam Boolean saveToLocal) {
        try {
            ResponseEntity<UploadResponse> responseEntity = uploadService.uploadFile(file, saveToLocal);
            return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
