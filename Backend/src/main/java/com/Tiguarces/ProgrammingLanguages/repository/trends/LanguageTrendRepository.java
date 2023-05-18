package com.Tiguarces.ProgrammingLanguages.repository.trends;

import com.Tiguarces.ProgrammingLanguages.model.trend.LanguageTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageTrendRepository extends JpaRepository<LanguageTrend, Integer> {
    Optional<LanguageTrend> findByRepositoryName(String repositoryName);
}
