package com.fainancial.KnowledgeBuilderService.repository;

import com.fainancial.KnowledgeBuilderService.model.ExtractionModel;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RuleRepository extends Neo4jRepository<ExtractionModel, Long> {

    Optional<ExtractionModel> findByTitle(String title);

    List<ExtractionModel> findByDescriptionContainingIgnoreCase(String searchText);

    @Query("MATCH (r:Rule) RETURN r ORDER BY r.createdAt DESC")
    List<ExtractionModel> findAllOrderByCreatedAtDesc();

    @Query("MATCH (r:Rule) WHERE r.title =~ $titlePattern RETURN r")
    List<ExtractionModel> findByTitlePattern(@Param("titlePattern") String titlePattern);

    @Query("MATCH (r:Rule) WHERE r.title = $title RETURN COUNT(r) > 0")
    boolean existsByTitle(@Param("title") String title);

    @Query("MATCH (r:Rule) DELETE r")
    void deleteAllRules();
}