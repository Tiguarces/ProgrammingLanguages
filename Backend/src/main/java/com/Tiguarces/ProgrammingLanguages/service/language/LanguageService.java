package com.Tiguarces.ProgrammingLanguages.service.language;

import com.Tiguarces.ProgrammingLanguages.controller.language.dto.LanguageDTO;
import com.Tiguarces.ProgrammingLanguages.controller.language.dto.LanguageDetailsDTO;
import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.repository.language.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.Tiguarces.ProgrammingLanguages.utils.LanguageUtils.replaceToSharpWord;
import static com.Tiguarces.ProgrammingLanguages.utils.LanguageUtils.validLanguageName;

@Service
@RequiredArgsConstructor
public class LanguageService implements ServiceTemplate {
    private final LanguageRepository languageRepository;

    @Override
    public void saveAll(final List<Language> doneLanguages) {
        Objects.requireNonNull(doneLanguages);
        languageRepository.saveAll(doneLanguages);
    }

    @Override
    public Optional<Language> findByName(final String languageName) {
        Objects.requireNonNull(languageName);
        return languageRepository.findByName(replaceToSharpWord(languageName));
    }

    @Override
    public List<LanguageDTO> findAll() {
        return (languageRepository.findAll()).stream()
                                  .map(LanguageDTO::mapToDTO)
                                  .toList();
    }

    @Override
    public LanguageDetailsDTO getLanguageDetails(final String languageName) throws IllegalArgumentException {
        return findByName(validLanguageName(languageName))
                 .map(LanguageDetailsDTO::mapToDTO).orElseThrow();
    }
}
