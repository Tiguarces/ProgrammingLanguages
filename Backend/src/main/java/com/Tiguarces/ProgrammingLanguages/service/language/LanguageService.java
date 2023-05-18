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

        Optional<Language> foundLanguage;
        for(var language: doneLanguages) {
            if((foundLanguage = languageRepository.findByName(language.getName())).isPresent()) {
                checkLanguage(foundLanguage.get(), language);
            } else {
                languageRepository.save(language);
            }
        }
    }

    private void checkLanguage(final Language foundLanguage, final Language languageToSave) {
        if(!foundLanguage.equals(languageToSave)) { // Then update data
            foundLanguage.setParadigms(languageToSave.getParadigms());
            foundLanguage.setFileExtensions(languageToSave.getFileExtensions());
            foundLanguage.setStableRelease(languageToSave.getStableRelease());
            foundLanguage.setDescription(languageToSave.getDescription());
            foundLanguage.setWebsite(languageToSave.getWebsite());
            foundLanguage.setImplementations(languageToSave.getImplementations());
            foundLanguage.setThumbnailPath(languageToSave.getThumbnailPath());
            foundLanguage.setFirstAppeared(languageToSave.getFirstAppeared());
        }
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
