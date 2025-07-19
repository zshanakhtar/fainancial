package com.fainancial.UploadService.service;

import com.fainancial.UploadService.storageFactory.FileStorage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3FileStorageService implements FileStorage {

    @Override
    public boolean store(MultipartFile file) throws IOException {
        return false;
    }
}
