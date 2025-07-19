package com.fainancial.UploadService.service;

import com.fainancial.UploadService.storageFactory.FileStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.fainancial.UploadService.constants.UploadConstants.UPLOAD_VAULT;

@Slf4j
@Service
public class FileStorageLocalService implements FileStorage {

    @Override
    public boolean store(MultipartFile file) throws IOException {
        log.info("Uploading file to local file system: {}", file.getOriginalFilename());
        Path uploadVaultPath = Path.of(UPLOAD_VAULT);
        try {
            if (!Files.exists(uploadVaultPath)) {
                Files.createDirectories(uploadVaultPath);
            }
            if (StringUtils.isEmpty(file.getOriginalFilename())) {
                throw new IllegalArgumentException("File name must not be null");
            }
            try (InputStream inputStream = file.getInputStream()) {
                String filenameWithExtension = Paths.get(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString();
                Path path = uploadVaultPath.resolve(filenameWithExtension);
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to store file {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

}
