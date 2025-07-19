package com.fainancial.UploadService.service;

import com.fainancial.enums.OverallStatus;
import com.fainancial.UploadService.dao.RequestTableDao;
import com.fainancial.UploadService.dto.response.UploadResponse;
import com.fainancial.UploadService.validator.FileValidator;
import com.fainancial.model.RequestTable;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.fainancial.UploadService.constants.UploadConstants.MAX_FILE_SIZE;
import static com.fainancial.UploadService.constants.UploadConstants.UPLOAD_VAULT;
import static com.fainancial.model.RequestTable.createRequestTable;

@Slf4j
@Service
public class UploadService {

    @Autowired
    private FileStorageLocalService fileStorageLocalService;

    @Autowired
    private S3FileStorageService s3FileStorageService;

    @Autowired
    private RequestTableDao requestTableDao;

    @Autowired
    private FileValidator fileValidator;

    private RequestTable requestTable;

    public ResponseEntity<UploadResponse> uploadFile(MultipartFile file, Boolean saveToLocal) throws IOException {
        ResponseEntity<UploadResponse> validationResponse = handleValidation(file);
        if (validationResponse != null) {
            return validationResponse;
        }
        boolean uploadStatus = false;
        if (saveToLocal) {
            uploadStatus = fileStorageLocalService.store(file);
        } else {
            s3FileStorageService.store(file);
        }

        UploadResponse uploadResponse = UploadResponse.builder()
                .requestTable(saveInMongo(uploadStatus))
                .fileName(file.getOriginalFilename())
                .filePath(UPLOAD_VAULT)
                .build();
        return new ResponseEntity<>(uploadResponse, HttpStatus.OK);
    }

    private ResponseEntity<UploadResponse> handleValidation(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (!fileValidator.isValidFileType(file)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(null);
        }
        if (!fileValidator.validateFileSize(file, MAX_FILE_SIZE)) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(null);
        }
        return null;
    }

    private RequestTable saveInMongo(boolean uploadStatus) {
        try{
            return requestTableDao.save(createRequestTable(uploadStatus, OverallStatus.PROCESSING));
        } catch (Exception e) {
            throw new MongoException("Failed to save request in MongoDB: " + e.getMessage(), e);
        }

    }


}
