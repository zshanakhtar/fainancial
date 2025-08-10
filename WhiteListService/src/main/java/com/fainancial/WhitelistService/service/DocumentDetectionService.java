package com.fainancial.WhitelistService.service;

import org.springframework.stereotype.Service;

import static com.fainancial.WhitelistService.constants.WhitelistConstants.CONTENT_CONFIDENCE_BOOST;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.FILENAME_CONFIDENCE_BOOST;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.FORM_16;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.FORM_16_CONTENT_PATTERNS;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.FORM_16_FILENAME_PATTERNS;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.MIN_CONFIDENCE_THRESHOLD;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.UNKNOWN_DOCUMENT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentDetectionService {

    public DetectionResult detectDocumentType(String fileName, String fileContent) {
        log.info("Detecting document type for file: {}", fileName);
        
        // Try Form 16 detection first
        DetectionResult form16Result = detectForm16(fileName, fileContent);
        if (form16Result.isDetected()) {
            return form16Result;
        }
        
        // If no document type detected
        return DetectionResult.builder()
                .detectedType(UNKNOWN_DOCUMENT)
                .isDetected(false)
                .confidence(0.0)
                .build();
    }

    private DetectionResult detectForm16(String fileName, String fileContent) {
        double confidence = 0.0;
        boolean detected = false;
        
        // Check filename patterns
        String filenameLower = fileName.toLowerCase();
        for (String pattern : FORM_16_FILENAME_PATTERNS) {
            if (filenameLower.contains(pattern)) {
                confidence += FILENAME_CONFIDENCE_BOOST; // 0.7
                detected = true;
                log.debug("Form 16 detected from filename pattern: {}", pattern);
                break;
            }
        }
        
        // Check content patterns
        if (fileContent != null && !fileContent.trim().isEmpty()) {
            for (String pattern : FORM_16_CONTENT_PATTERNS) {
                if (fileContent.contains(pattern)) {
                    confidence += CONTENT_CONFIDENCE_BOOST; // 0.3
                    detected = true;
                    log.debug("Form 16 detected from content pattern: {}", pattern);
                    break;
                }
            }
        }
        
        // Ensure confidence doesn't exceed 1.0
        confidence = Math.min(confidence, 1.0);
        
        // Only consider it detected if confidence meets minimum threshold
        if (confidence < MIN_CONFIDENCE_THRESHOLD) {
            detected = false;
        }
        
        log.info("Form 16 detection result: detected={}, confidence={}", detected, confidence);
        
        return DetectionResult.builder()
                .detectedType(detected ? FORM_16 : UNKNOWN_DOCUMENT)
                .isDetected(detected)
                .confidence(confidence)
                .build();
    }

    @lombok.Data
    @lombok.Builder
    public static class DetectionResult {
        private String detectedType;
        private boolean isDetected;
        private double confidence;
    }
}