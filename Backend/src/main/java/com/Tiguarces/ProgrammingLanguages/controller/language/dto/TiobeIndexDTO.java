package com.Tiguarces.ProgrammingLanguages.controller.language.dto;

import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeIndex;
import com.Tiguarces.ProgrammingLanguages.model.tiobe.TiobeStatus;

import java.time.LocalDate;
import java.util.Objects;

public record TiobeIndexDTO(int rank, String indexDate, TiobeStatus status) {
    public static TiobeIndexDTO mapToDTO(final TiobeIndex tiobeIndex) {
        return new TiobeIndexDTO(tiobeIndex.getRank(), changeDatePattern(tiobeIndex.getIndexDate()), tiobeIndex.getStatus());
    }

    private static final String DATE_PATTERN = "%s - %d";
    private static String changeDatePattern(final LocalDate date) {
        Objects.requireNonNull(date);
        return String.format(DATE_PATTERN, date.getMonth().name(), date.getYear());
    }
}
