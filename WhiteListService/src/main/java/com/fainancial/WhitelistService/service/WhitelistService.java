package com.fainancial.WhitelistService.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.fainancial.WhitelistService.constants.WhitelistConstants.FORM_16;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.UNKNOWN_DOCUMENT;
import com.fainancial.WhitelistService.document.WhitelistServiceDetails;
import com.fainancial.WhitelistService.dto.FileUploadMessage;
import com.fainancial.WhitelistService.dto.WhitelistCompletedMessage;
import com.fainancial.WhitelistService.repository.WhitelistRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhitelistService {

    @Autowired
    private S3FileService s3FileService;

    @Autowired
    private DocumentDetectionService documentDetectionService;

    @Autowired
    private WhitelistRepository whitelistRepository;

    public WhitelistCompletedMessage processFile(FileUploadMessage message) {
        log.info("Processing file for whitelist: fileId={}, requestId={}", 
                message.getFileId(), message.getRequestId());

        try {
            // Step 1: Check file accessibility
            String s3Key = extractS3KeyFromLocation(message.getFileLocation());
            if (!s3FileService.isFileAccessible(s3Key)) {
                return handleFailure(message, "File not accessible in S3");
            }

            // Step 2: Download and extract text
            String fileContent = "";
            try {
                fileContent = s3FileService.downloadAndExtractText(s3Key);
                log.debug("Extracted {} characters from file", fileContent.length());
            } catch (Exception e) {
                log.warn("Could not extract text from file, proceeding with filename only: {}", e.getMessage());
            }

            // Step 3: Detect document type
            DocumentDetectionService.DetectionResult result = 
                    documentDetectionService.detectDocumentType(message.getFileName(), fileContent);

            // Step 4: Determine whitelist status
            boolean isWhitelisted = isDocumentWhitelisted(result.getDetectedType());

            // Step 5: Save to database
            WhitelistServiceDetails details = WhitelistServiceDetails.builder()
                    .fileId(message.getFileId())
                    .requestId(message.getRequestId())
                    .whitelisted(isWhitelisted)
                    .detectedType(result.getDetectedType())
                    .confidence(result.getConfidence())
                    .processedAt(new Date())
                    .failureComments(isWhitelisted ? null : generateFailureComment(result))
                    .build();

            whitelistRepository.save(details);
            log.info("Saved whitelist result to MongoDB: fileId={}, whitelisted={}", 
                    message.getFileId(), isWhitelisted);

            // Step 6: Return result for next service
            return WhitelistCompletedMessage.builder()
                    .fileId(message.getFileId())
                    .requestId(message.getRequestId())
                    .whitelisted(isWhitelisted)
                    .detectedType(result.getDetectedType())
                    .build();

        } catch (Exception e) {
            log.error("Error processing file: {}", e.getMessage(), e);
            return handleFailure(message, "Internal processing error: " + e.getMessage());
        }
    }

    private WhitelistCompletedMessage handleFailure(FileUploadMessage message, String failureReason) {
        log.warn("Processing failed for fileId={}: {}", message.getFileId(), failureReason);
        
        WhitelistServiceDetails details = WhitelistServiceDetails.builder()
                .fileId(message.getFileId())
                .requestId(message.getRequestId())
                .whitelisted(false)
                .detectedType(UNKNOWN_DOCUMENT)
                .confidence(0.0)
                .processedAt(new Date())
                .failureComments(failureReason)
                .build();

        whitelistRepository.save(details);

        return WhitelistCompletedMessage.builder()
                .fileId(message.getFileId())
                .requestId(message.getRequestId())
                .whitelisted(false)
                .detectedType(UNKNOWN_DOCUMENT)
                .build();
    }

    private boolean isDocumentWhitelisted(String documentType) {
        // For now, only Form 16 is whitelisted
        // TODO: Add more document types as we expand
        return FORM_16.equals(documentType);
    }

    private String generateFailureComment(DocumentDetectionService.DetectionResult result) {
        if (UNKNOWN_DOCUMENT.equals(result.getDetectedType())) {
            return "Document type not recognized or not relevant for tax processing";
        } else {
            return "Document type '" + result.getDetectedType() + "' is not in whitelist";
        }
    }

    private String extractS3KeyFromLocation(String s3Location) {
        // Extract key from s3://bucket-name/key format
        if (s3Location.startsWith("s3://")) {
            int firstSlash = s3Location.indexOf('/', 5);
            if (firstSlash != -1) {
                return s3Location.substring(firstSlash + 1);
            }
        }
        // If it's just the key without s3:// prefix
        return s3Location;
    }
}