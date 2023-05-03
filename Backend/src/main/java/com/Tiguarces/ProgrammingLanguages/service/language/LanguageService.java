package com.Tiguarces.ProgrammingLanguages.service.language;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;
import com.Tiguarces.ProgrammingLanguages.repository.language.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LanguageService implements ServiceTemplate {
    private final LanguageRepository languageRepository;

    @Override
    public void saveAll(final List<Language> doneLanguages) {
        Objects.requireNonNull(doneLanguages);
        languageRepository.saveAll(doneLanguages);
    }
}