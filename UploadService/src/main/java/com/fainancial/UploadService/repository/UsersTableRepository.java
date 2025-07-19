package com.fainancial.UploadService.repository;

import com.fainancial.UploadService.document.UsersTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersTableRepository extends MongoRepository<UsersTable, String> {

}
