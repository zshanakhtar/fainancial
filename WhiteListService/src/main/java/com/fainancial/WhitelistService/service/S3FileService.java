// package com.fainancial.WhitelistService.service;

// import java.io.IOException;
// import java.io.InputStream;
// import java.util.List;

// import org.springframework.ai.document.Document;
// import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
// import org.springframework.ai.reader.tika.TikaDocumentReader;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.InputStreamResource;
// import org.springframework.stereotype.Service;

// import com.amazonaws.SdkClientException;
// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.AmazonS3ClientBuilder;
// import com.amazonaws.services.s3.model.GetObjectRequest;
// import com.amazonaws.services.s3.model.S3Object;

// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @Service
// public class S3FileService {

//     @Value("${aws.s3.bucket-name}")
//     private String bucketName;

//     private final AmazonS3 s3Client;

//     public S3FileService() {
//         this.s3Client = AmazonS3ClientBuilder.defaultClient();
//     }

//     public String downloadAndExtractText(String s3Key) throws IOException {
//         log.info("Downloading file from S3: {}", s3Key);
        
//         try {
//             S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, s3Key));
//             InputStream inputStream = s3Object.getObjectContent();
            
//             // Use Spring AI for better document processing
//             if (s3Key.toLowerCase().endsWith(".pdf")) {
//                 return extractTextFromPdf(inputStream);
//             } else {
//                 return extractTextWithTika(inputStream);
//             }
            
//         } catch (SdkClientException e) {
//             log.error("AWS S3 error downloading file: {}", e.getMessage());
//             throw new IOException("Failed to download file from S3: " + e.getMessage(), e);
//         }
//     }

//     private String extractTextFromPdf(InputStream inputStream) throws IOException {
//         try {
//             InputStreamResource resource = new InputStreamResource(inputStream);
//             PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource);
//             List<Document> documents = pdfReader.get();
            
//             StringBuilder text = new StringBuilder();
//             for (Document doc : documents) {
//                 text.append(doc.getText()).append("\n");
//             }
            
//             log.debug("Extracted {} characters from PDF", text.length());
//             return text.toString();
//         } catch (RuntimeException e) {
//             log.error("Error extracting text from PDF: {}", e.getMessage());
//             throw new IOException("Failed to extract text from PDF", e);
//         }
//     }

//     private String extractTextWithTika(InputStream inputStream) throws IOException {
//         try {
//             InputStreamResource resource = new InputStreamResource(inputStream);
//             TikaDocumentReader tikaReader = new TikaDocumentReader(resource);
//             List<Document> documents = tikaReader.get();
            
//             StringBuilder text = new StringBuilder();
//             for (Document doc : documents) {
//                 text.append(doc.getText()).append("\n");
//             }
            
//             log.debug("Extracted {} characters using Tika", text.length());
//             return text.toString();
//         } catch (RuntimeException e) {
//             log.error("Error extracting text with Tika: {}", e.getMessage());
//             throw new IOException("Failed to extract text with Tika", e);
//         }
//     }

//     public boolean isFileAccessible(String s3Key) {
//         try {
//             return s3Client.doesObjectExist(bucketName, s3Key);
//         } catch (SdkClientException e) {
//             log.error("Error checking file accessibility: {}", e.getMessage());
//             return false;
//         }
//     }
// }


// -------------------------------------------------------------------------------------------------------------------------------------------------------------

// To test without the actual S3 service, you can mock the S3FileService in your tests.
// Mock 

package com.fainancial.WhitelistService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3FileService {

    @Value("${aws.s3.bucket-name:mock-bucket}")
    private String bucketName;

    public String downloadAndExtractText(String s3Key) throws Exception {
        log.info("Mock: Downloading file from S3: {}", s3Key);
        
        // Mock S3 download - for testing purposes
        // Return empty content so filename detection can still work
        log.warn("S3 service is mocked - returning empty content for testing");
        return "";
    }

    public boolean isFileAccessible(String s3Key) {
        log.info("Mock: Checking file accessibility for: {}", s3Key);
        // Always return true for testing
        return true;
    }
}