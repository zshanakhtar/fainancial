package com.fainancial.KnowledgeBuilderService.service;

import com.fainancial.KnowledgeBuilderService.constants.FileConstants;
import com.fainancial.KnowledgeBuilderService.model.ExtractionModel;
import com.fainancial.KnowledgeBuilderService.model.ExtractionModelWrapper;
import com.fainancial.KnowledgeBuilderService.util.ExtractionModelParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Service
public class InfoExtractionService {


    public ExtractionModelWrapper extractRules() {
        StringBuilder fileContent = new StringBuilder();
        ExtractionModelWrapper extractionModelWrapper = new ExtractionModelWrapper();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(FileConstants.FILE_NAME)))) {

            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }
            List<ExtractionModel> extractionModels = ExtractionModelParseUtil.getAllRulesFromText(fileContent.toString());
            extractionModelWrapper.setExtractionModelList(extractionModels);

        } catch (Exception e) {
            log.error("An error occurred: {}", e);
        }
        return extractionModelWrapper;
    }
}
