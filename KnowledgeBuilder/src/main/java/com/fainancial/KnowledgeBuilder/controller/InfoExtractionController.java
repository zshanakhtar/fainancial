package com.fainancial.KnowledgeBuilder.controller;

import com.fainancial.KnowledgeBuilder.model.ExtractionModelWrapper;
import com.fainancial.KnowledgeBuilder.service.InfoExtractionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fainance")
@CrossOrigin
public class InfoExtractionController {

    @Autowired
    InfoExtractionService infoExtractionService;

    @GetMapping("/getRules")
    @Operation(summary = "getRules", description = "Returns IT rules for Knowledge Base")
    public ResponseEntity<ExtractionModelWrapper> extractInfo() {
        ExtractionModelWrapper extractionModel = infoExtractionService.extractRules();
        if (extractionModel != null && extractionModel.getExtractionModelList().size() > 0) {
            return new ResponseEntity<>(extractionModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(extractionModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
