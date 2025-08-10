package com.fainancial.WhitelistService.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fainancial.WhitelistService.constants.WhitelistConstants.UPLOAD_COMPLETED_QUEUE;
import static com.fainancial.WhitelistService.constants.WhitelistConstants.WHITELIST_COMPLETED_QUEUE;
import com.fainancial.WhitelistService.dto.FileUploadMessage;
import com.fainancial.WhitelistService.dto.WhitelistCompletedMessage;
import com.fainancial.WhitelistService.service.WhitelistService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WhitelistConsumer {

    @Autowired
    private WhitelistService whitelistService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = UPLOAD_COMPLETED_QUEUE)
    public void processFileUpload(FileUploadMessage message) {
        log.info("Received file upload message: fileId={}, requestId={}, fileName={}", 
                message.getFileId(), message.getRequestId(), message.getFileName());

        try {
            
            WhitelistCompletedMessage result = whitelistService.processFile(message);
            
            
            if (result.getWhitelisted()) {
                rabbitTemplate.convertAndSend(WHITELIST_COMPLETED_QUEUE, result);
                log.info("Sent whitelisted file to Knowledge Matcher: fileId={}, detectedType={}", 
                        result.getFileId(), result.getDetectedType());
            } else {
                log.info("File not whitelisted, not forwarding to Knowledge Matcher: fileId={}", 
                        result.getFileId());
            }
            
            log.info("Whitelist processing completed for fileId: {}, whitelisted: {}", 
                    result.getFileId(), result.getWhitelisted());
            
        } catch (Exception e) {
            log.error("Error processing file upload message for fileId={}: {}", 
                    message.getFileId(), e.getMessage(), e);
            
            handleProcessingError(message, e);
        }
    }

    private void handleProcessingError(FileUploadMessage message, Exception e) {
        log.error("Failed to process file upload message: fileId={}, error={}", 
                message.getFileId(), e.getMessage());
    }
}