package com.fainancial.UploadService.validator;

import com.fainancial.UploadService.constants.FileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class FileValidator {

    public boolean isValidFileType(MultipartFile file) throws IOException {
        Tika tika = new Tika();
        try {
            String detectedType = tika.detect(file.getBytes());
            log.info("Detected file type: {} " , detectedType);
            return FileType.EXCEL.getType().stream().anyMatch(detectedType::contains) ||
                    FileType.DOCUMENT.getType().stream().anyMatch(detectedType::contains);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateFileSize(MultipartFile file, long maxSize) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        return file.getSize() < maxSize;
    }
}
