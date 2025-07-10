package com.fainancial.KnowledgeBuilder.service;

import com.fainancial.KnowledgeBuilder.constants.FileConstants;
import com.fainancial.KnowledgeBuilder.model.ExtractionModel;
import com.fainancial.KnowledgeBuilder.model.ExtractionModelWrapper;
import com.fainancial.KnowledgeBuilder.util.ExtractionModelParseUtil;
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
