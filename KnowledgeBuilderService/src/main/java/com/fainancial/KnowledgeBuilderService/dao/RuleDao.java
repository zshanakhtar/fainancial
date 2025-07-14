package com.fainancial.KnowledgeBuilderService.dao;

import com.fainancial.KnowledgeBuilderService.model.ExtractionModel;
import com.fainancial.KnowledgeBuilderService.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleDao {

    private final RuleRepository ruleRepository;

    @Transactional
    public ExtractionModel saveRule(ExtractionModel rule) {
        try {
            if (ruleRepository.existsByTitle(rule.getTitle())) {
                log.warn("Rule with title '{}' already exists. Skipping save.", rule.getTitle());
                return null;
            }

            ExtractionModel savedRule = ruleRepository.save(rule);
            log.info("Successfully saved rule: {}", savedRule.getTitle());
            return savedRule;

        } catch (Exception e) {
            log.error("Error saving rule: {}", rule.getTitle(), e);
            throw new RuntimeException("Failed to save rule: " + rule.getTitle(), e);
        }
    }

    @Transactional
    public List<ExtractionModel> saveAllRules(List<ExtractionModel> rules) {
        try {
            List<ExtractionModel> newRules = rules.stream()
                    .filter(rule -> !ruleRepository.existsByTitle(rule.getTitle()))
                    .toList();

            if (newRules.isEmpty()) {
                log.info("No new rules to save. All rules already exist.");
                return List.of();
            }

            List<ExtractionModel> savedRules = ruleRepository.saveAll(newRules);
            log.info("Successfully saved {} rules to Neo4j", savedRules.size());
            return savedRules;

        } catch (Exception e) {
            log.error("Error saving rules batch", e);
            throw new RuntimeException("Failed to save rules batch", e);
        }
    }

    public Optional<ExtractionModel> findByTitle(String title) {
        return ruleRepository.findByTitle(title);
    }

    public List<ExtractionModel> getAllRules() {
        return ruleRepository.findAllOrderByCreatedAtDesc();
    }

    public List<ExtractionModel> searchRulesByDescription(String searchText) {
        return ruleRepository.findByDescriptionContainingIgnoreCase(searchText);
    }

    @Transactional
    public ExtractionModel updateRule(ExtractionModel rule) {
        try {
            rule.updateTimestamp(); // Update the timestamp
            ExtractionModel updatedRule = ruleRepository.save(rule);
            log.info("Successfully updated rule: {}", updatedRule.getTitle());
            return updatedRule;

        } catch (Exception e) {
            log.error("Error updating rule: {}", rule.getTitle(), e);
            throw new RuntimeException("Failed to update rule: " + rule.getTitle(), e);
        }
    }

    @Transactional
    public void deleteRule(Long id) {
        try {
            ruleRepository.deleteById(id);
            log.info("Successfully deleted rule with ID: {}", id);

        } catch (Exception e) {
            log.error("Error deleting rule with ID: {}", id, e);
            throw new RuntimeException("Failed to delete rule with ID: " + id, e);
        }
    }

    @Transactional
    public void deleteAllRules() {
        try {
            ruleRepository.deleteAllRules();
            log.info("Successfully deleted all rules");

        } catch (Exception e) {
            log.error("Error deleting all rules", e);
            throw new RuntimeException("Failed to delete all rules", e);
        }
    }

    public long getRuleCount() {
        return ruleRepository.count();
    }
}