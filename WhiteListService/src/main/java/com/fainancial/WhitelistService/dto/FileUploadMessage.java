package com.fainancial.WhitelistService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadMessage {
    private String fileId;
    private String requestId;
    private String fileLocation;
    private String fileName;
    private String fileType;
}