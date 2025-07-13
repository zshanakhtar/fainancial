package com.fainancial.KnowledgeBuilderService.util;

import com.fainancial.KnowledgeBuilderService.constants.FileConstants;
import com.fainancial.KnowledgeBuilderService.model.ExtractionModel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@UtilityClass
@Slf4j
public class ExtractionModelParseUtil {

    public ExtractionModel populateExtractionModel(ExtractionModel extractionModel, String fileContent) {
        try {
            if (fileContent != null && !fileContent.isEmpty()) {
                String[] lines = fileContent.split(FileConstants.RULE_DESCRIPTION_SEPARATOR_REGEX);
                if (lines.length >= 2) {
                    extractionModel.setTitle(cleanRule(safeTrim(lines[0])));
                    extractionModel.setDescription(cleanRule(safeTrim(lines[1])));
                    if (lines.length == 3) {
                        String fullDescription = extractionModel.getDescription() + " " + cleanRule(safeTrim(lines[2]));
                        extractionModel.setDescription(fullDescription);
                    }
                } else if (lines.length == 1) {
                    String content = cleanRule(safeTrim(lines[0]));
                    extractionModel.setDescription(content);
                    extractionModel.setTitle(generateTitleFromDescription(content));
                }
            } else {
                throw new Exception("File content is empty or null. Cannot populate ExtractionModel.");
            }
        } catch (Exception e) {
            log.error("An error occurred while parsing the file content: {}", e);
        }
        return extractionModel;
    }

    public List<ExtractionModel> getAllRulesFromText(String fileContent) {
        List<ExtractionModel> extractionModels = new ArrayList<>();
        try {
            if (fileContent != null && !fileContent.isEmpty()) {
                String[] rules = fileContent.split(FileConstants.END_OF_RULE_REGEX);
                for (String rule : rules) {
                    if (!rule.trim().isEmpty()) {
                        ExtractionModel model = new ExtractionModel();
                        populateExtractionModel(model, rule.trim());
                        extractionModels.add(model);
                    } else {
                        log.warn("Rule format is incorrect: {}", rule);
                    }
                }
            }
            else {
                throw new Exception("Unable to create list of Extraction Models.");
            }

        } catch (Exception e) {
            log.error("An error occurred while reading the file content: {}", e);
        }
        return extractionModels;
    }

    public ExtractionModel createExtractionModel(String title, String description) {
        ExtractionModel model = new ExtractionModel();
        model.setTitle(cleanRule(safeTrim(title)));
        model.setDescription(cleanRule(safeTrim(description)));
        return model;
    }

    private String generateTitleFromDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return "Untitled Rule";
        }

        String[] words = description.trim().split("\\s+");
        int wordCount = Math.min(5, words.length); // Need to ask, how to create description

        StringBuilder title = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            title.append(words[i]).append(" ");
        }

        String generatedTitle = title.toString().trim();
        return generatedTitle.length() > 50 ? generatedTitle.substring(0, 50) + "..." : generatedTitle;
    }

    private String safeTrim(String value) {
        return Optional.ofNullable(value)
                .map(String::trim)
                .orElse("");
    }

    public String cleanRule(String rule) {
        return rule.replaceAll(FileConstants.RULE_CLEANER, "");
    }
}