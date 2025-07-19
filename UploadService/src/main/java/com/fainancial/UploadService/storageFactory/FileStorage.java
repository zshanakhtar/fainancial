package com.fainancial.UploadService.storageFactory;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {
    boolean store(MultipartFile file) throws IOException;
}
