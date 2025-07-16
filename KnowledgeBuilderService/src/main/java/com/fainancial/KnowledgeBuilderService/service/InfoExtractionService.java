package com.fainancial.KnowledgeBuilderService.service;

import com.fainancial.KnowledgeBuilderService.constants.FileConstants;
import com.fainancial.KnowledgeBuilderService.dao.RuleDao;
import com.fainancial.KnowledgeBuilderService.model.ExtractionModel;
import com.fainancial.KnowledgeBuilderService.model.ExtractionModelWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoExtractionService {

    private final RuleDao ruleDao;

    public ExtractionModelWrapper extractAndSaveRules() {
        log.info("Starting rule extraction and Neo4j save process...");

        ExtractionModelWrapper extractionModelWrapper = new ExtractionModelWrapper();
        List<ExtractionModel> extractionModels = new ArrayList<>();

        try {
            extractionModels = readFromClasspath();
            if (extractionModels.isEmpty()) {
                log.info("No files found in classpath, trying file system...");
                extractionModels = readFromFileSystem();
            }

            log.info("Successfully processed {} rules", extractionModels.size());

            if (!extractionModels.isEmpty()) {
                List<ExtractionModel> savedRules = ruleDao.saveAllRules(extractionModels);
                log.info("Successfully saved {} rules to Neo4j", savedRules.size());
                extractionModelWrapper.setExtractionModelList(savedRules);
            } else {
                extractionModelWrapper.setExtractionModelList(extractionModels);
            }

        } catch (Exception e) {
            log.error("An error occurred during rule extraction and save process", e);
            throw new RuntimeException("Failed to extract and save rules", e);
        }

        return extractionModelWrapper;
    }

    private List<ExtractionModel> readFromClasspath() {
        List<ExtractionModel> extractionModels = new ArrayList<>();

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:rules/*.txt");

            log.info("Found {} rule files in classpath", resources.length);

            for (Resource resource : resources) {
                try {
                    ExtractionModel rule = processResourceFile(resource);
                    if (rule != null) {
                        extractionModels.add(rule);
                    }
                } catch (Exception e) {
                    log.error("Error processing resource file: {}", resource.getFilename(), e);
                }
            }

        } catch (Exception e) {
            log.error("Error reading from classpath", e);
        }

        return extractionModels;
    }

    private List<ExtractionModel> readFromFileSystem() {
        List<ExtractionModel> extractionModels = new ArrayList<>();

        try {
            String ruleFolderPath = FileConstants.RULE_FOLDER;
            File ruleFolder = new File(ruleFolderPath);

            if (!ruleFolder.exists() || !ruleFolder.isDirectory()) {
                log.warn("Rule folder does not exist: {}", ruleFolderPath);
                return extractionModels;
            }

            File[] ruleFiles = ruleFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

            if (ruleFiles == null || ruleFiles.length == 0) {
                log.warn("No rule files found in directory: {}", ruleFolderPath);
                return extractionModels;
            }

            log.info("Found {} rule files in file system", ruleFiles.length);

            for (File ruleFile : ruleFiles) {
                try {
                    ExtractionModel rule = processRuleFile(ruleFile);
                    if (rule != null) {
                        extractionModels.add(rule);
                    }
                } catch (Exception e) {
                    log.error("Error processing rule file: {}", ruleFile.getName(), e);
                }
            }

        } catch (Exception e) {
            log.error("Error reading from file system", e);
        }

        return extractionModels;
    }

    private ExtractionModel processResourceFile(Resource resource) throws IOException {
        log.debug("Processing resource file: {}", resource.getFilename());

        String filename = resource.getFilename();
        if (filename == null) return null;

        String title = filename.replaceAll("\\.txt$", "");

        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }
        }

        String content = fileContent.toString().trim();

        if (content.isEmpty()) {
            log.warn("Empty content in resource file: {}", filename);
            return null;
        }
        if (content.endsWith("---END OF RULE---")) {
            content = content.substring(0, content.length() - "---END OF RULE---".length()).trim();
        }

        ExtractionModel rule = new ExtractionModel();
        rule.setTitle(title);
        rule.setDescription(content);

        log.debug("Created rule: {} with content length: {}", title, content.length());
        return rule;
    }

    private ExtractionModel processRuleFile(File ruleFile) throws IOException {
        log.debug("Processing rule file: {}", ruleFile.getName());

        String title = ruleFile.getName().replaceAll("\\.txt$", "");

        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(ruleFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }
        }

        String content = fileContent.toString().trim();

        if (content.isEmpty()) {
            log.warn("Empty content in rule file: {}", ruleFile.getName());
            return null;
        }

        if (content.endsWith("---END OF RULE---")) {
            content = content.substring(0, content.length() - "---END OF RULE---".length()).trim();
        }


        ExtractionModel rule = new ExtractionModel();
        rule.setTitle(title);
        rule.setDescription(content);

        log.debug("Created rule: {} with content length: {}", title, content.length());
        return rule;
    }

    public List<ExtractionModel> getAllRulesFromDatabase() {
        log.info("Fetching all rules from Neo4j database...");
        try {
            List<ExtractionModel> rules = ruleDao.getAllRules();
            log.info("Retrieved {} rules from database", rules.size());
            return rules;
        } catch (Exception e) {
            log.error("Error fetching rules from database", e);
            throw new RuntimeException("Failed to fetch rules from database", e);
        }
    }


    public List<ExtractionModel> searchRules(String searchText) {
        log.info("Searching rules with text: {}", searchText);
        try {
            List<ExtractionModel> rules = ruleDao.searchRulesByDescription(searchText);
            log.info("Found {} rules matching search criteria", rules.size());
            return rules;
        } catch (Exception e) {
            log.error("Error searching rules", e);
            throw new RuntimeException("Failed to search rules", e);
        }
    }
}