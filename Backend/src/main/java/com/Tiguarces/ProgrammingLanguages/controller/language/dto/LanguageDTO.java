package com.Tiguarces.ProgrammingLanguages.controller.language.dto;

import com.Tiguarces.ProgrammingLanguages.model.language.Language;

import java.util.Objects;

import static com.Tiguarces.ProgrammingLanguages.utils.LanguageUtils.replaceToSharpSign;

public record LanguageDTO(String name, String thumbnailPath) {

    public static LanguageDTO mapToDTO(final Language language) {
        Objects.requireNonNull(language);
        return new LanguageDTO(replaceToSharpSign(language.getName()), language.getThumbnailPath());
    }
}
