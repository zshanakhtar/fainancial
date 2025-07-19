package com.fainancial.UploadService.dto.response;

import com.fainancial.model.RequestTable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UploadResponse {
    private RequestTable requestTable;
    private String fileName;
    private String filePath;
}
