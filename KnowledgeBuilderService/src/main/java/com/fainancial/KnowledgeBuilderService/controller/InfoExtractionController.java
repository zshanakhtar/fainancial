package com.fainancial.KnowledgeBuilderService.controller;

import com.fainancial.KnowledgeBuilderService.model.ExtractionModelWrapper;
import com.fainancial.KnowledgeBuilderService.service.InfoExtractionService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fainance")
@CrossOrigin
public class InfoExtractionController {

    @Autowired
    InfoExtractionService infoExtractionService;

    @GetMapping("/getRules")
    @Operation(summary = "getRules", description = "Returns IT rules for Knowledge Base (extract only)")
    public ResponseEntity<ExtractionModelWrapper> extractInfo() {
        try {
            ExtractionModelWrapper extractionModel = infoExtractionService.extractRulesOnly();
            if (extractionModel != null && extractionModel.getExtractionModelList().size() > 0) {
                return new ResponseEntity<>(extractionModel, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(extractionModel, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/extractAndSave")
    @Operation(summary = "extractAndSave", description = "Extract IT rules and save to Neo4j database")
    public ResponseEntity<ExtractionModelWrapper> extractAndSaveRules() {
        try {
            //extracts rules from Rule files  saves to neo4J
            ExtractionModelWrapper extractionModel = infoExtractionService.extractAndSaveRules();
            if (extractionModel != null && extractionModel.getExtractionModelList().size() > 0) {
                return new ResponseEntity<>(extractionModel, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(extractionModel, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}