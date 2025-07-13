package com.fainancial.KnowledgeBuilderService.controller;

import com.fainancial.KnowledgeBuilderService.model.ExtractionModelWrapper;
import com.fainancial.KnowledgeBuilderService.service.InfoExtractionService;
import io.swagger.v3.oas.annotations.Operation;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
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

        var driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "fainancial"));
        try (Session session = driver.session()) {
            // A Managed transaction is a quick and easy way to wrap a Cypher Query.
            // The `session.run` method will run the specified Query.
            // This simpler method does not use any automatic retry mechanism.
            Result result = session.run(
                    "MATCH (n) where n.title=\"The Godfather\" RETURN n.director AS name");
            // Each Cypher execution returns a stream of records.
            while (result.hasNext()) {
                Record record = result.next();
                // Values can be extracted from a record by index or name.
                System.out.println(record.get("name").asString());
            }
        }

        ExtractionModelWrapper extractionModel = infoExtractionService.extractRules();
        if (extractionModel != null && extractionModel.getExtractionModelList().size() > 0) {
            return new ResponseEntity<>(extractionModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(extractionModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
