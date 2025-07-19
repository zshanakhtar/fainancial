package com.fainancial.model;


import com.fainancial.enums.OverallStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "requestTable")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTable {

    @Id
    private String requestId;

    private String userId;
    private boolean uploadStatus;
    private boolean whitelistStatus;
    private OverallStatus overallStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RequestTable createRequestTable(boolean uploadStatus, OverallStatus overallStatus){
        return RequestTable.builder()
//                .requestId(requestId)
                .uploadStatus(uploadStatus)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .overallStatus(overallStatus)
                .build();
    }

    public static RequestTable updateRequestTable(RequestTable requestTable){
        return RequestTable.builder()
//                .requestId(requestId)
                .uploadStatus(requestTable.isUploadStatus())
                .updatedAt(LocalDateTime.now())
                .overallStatus(requestTable.getOverallStatus())
                .build();
    }

}
