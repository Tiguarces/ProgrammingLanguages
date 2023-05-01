package com.Tiguarces.ProgrammingLanguages.repository.language;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
}
