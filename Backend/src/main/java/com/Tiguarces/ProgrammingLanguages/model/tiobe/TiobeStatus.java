package com.Tiguarces.ProgrammingLanguages.model.tiobe;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TiobeStatus {
    DOWN(-1), SUPER_DOWN(-2),
    UP(1), SUPER_UP(2),
    NEUTRAL(0);

    @Getter private final int value;
}
