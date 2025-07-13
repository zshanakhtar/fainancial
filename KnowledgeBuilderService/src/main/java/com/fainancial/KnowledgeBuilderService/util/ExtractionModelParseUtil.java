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
                if (lines.length == 3) {
                    extractionModel.setDescription(cleanRule(safeTrim(lines[0])));
                    extractionModel.setRule(cleanRule(safeTrim(lines[1])));
                    extractionModel.setContext(cleanRule(safeTrim(lines[2])));
                }
            } else {
                throw new Exception("File content is empty or null. Cannot populate ExtractionModel Object.");
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

    private String safeTrim(String value) {
        return Optional.ofNullable(value)
                .map(String::trim)
                .orElse("");
    }

    public String cleanRule(String rule) {
        return rule.replaceAll(FileConstants.RULE_CLEANER, "");
    }

}
