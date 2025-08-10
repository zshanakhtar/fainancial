package com.fainancial.WhitelistService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fainancial.WhitelistService.document.WhitelistServiceDetails;

@Repository
public interface WhitelistRepository extends MongoRepository<WhitelistServiceDetails, String> {
    
    List<WhitelistServiceDetails> findByRequestId(String requestId);
    Optional<WhitelistServiceDetails> findByFileId(String fileId);
    
    List<WhitelistServiceDetails> findByWhitelisted(Boolean whitelisted);
    List<WhitelistServiceDetails> findByDetectedType(String detectedType);
}