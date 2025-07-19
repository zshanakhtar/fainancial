package com.fainancial.UploadService.repository;

import com.fainancial.UploadService.document.RequestTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestTableRepository extends MongoRepository<RequestTable, String> {
}
