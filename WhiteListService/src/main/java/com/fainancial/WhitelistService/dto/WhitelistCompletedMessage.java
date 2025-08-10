package com.fainancial.WhitelistService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhitelistCompletedMessage {
    private String fileId;
    private String requestId;
    private Boolean whitelisted;
    private String detectedType;
}