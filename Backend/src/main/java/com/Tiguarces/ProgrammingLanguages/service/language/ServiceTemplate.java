package com.Tiguarces.ProgrammingLanguages.service.language;

import com.Tiguarces.ProgrammingLanguages.controller.language.dto.LanguageDTO;
import com.Tiguarces.ProgrammingLanguages.controller.language.dto.LanguageDetailsDTO;
import com.Tiguarces.ProgrammingLanguages.model.language.Language;

import java.util.List;
import java.util.Optional;

interface ServiceTemplate {
    void saveAll(List<Language> doneLanguages);
    Optional<Language> findByName(String name);
    List<LanguageDTO> findAll();
    LanguageDetailsDTO getLanguageDetails(String languageName);
}
