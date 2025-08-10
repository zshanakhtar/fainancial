package com.fainancial.WhitelistService.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "whitelistServiceDetails")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WhitelistServiceDetails {

    @Id
    private String fileId;
    
    private String requestId;
    private Boolean whitelisted;
    private String failureComments;
    private String detectedType;
    private Double confidence;
    private Date processedAt;
    
    @Builder.Default
    private Date createdAt = new Date();
}