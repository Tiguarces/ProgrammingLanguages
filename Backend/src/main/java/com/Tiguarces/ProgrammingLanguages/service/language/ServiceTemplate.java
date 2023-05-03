package com.Tiguarces.ProgrammingLanguages.service.language;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;

import java.util.List;

interface ServiceTemplate {
    void saveAll(List<Language> doneLanguages);
}
