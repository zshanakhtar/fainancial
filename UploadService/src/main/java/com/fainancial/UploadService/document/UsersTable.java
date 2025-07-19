package com.fainancial.UploadService.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "usersTable")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersTable {

    @Id
    private String userId;

    private String name;
    private String email;
    private Date createdAt;
    private Date updatedAt;

}
