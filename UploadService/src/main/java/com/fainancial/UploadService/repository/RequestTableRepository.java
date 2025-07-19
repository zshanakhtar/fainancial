package com.fainancial.UploadService.repository;

import com.fainancial.model.RequestTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestTableRepository extends MongoRepository<RequestTable, String> {
}
