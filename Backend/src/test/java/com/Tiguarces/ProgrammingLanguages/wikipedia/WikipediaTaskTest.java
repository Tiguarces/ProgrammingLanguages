package com.Tiguarces.ProgrammingLanguages.wikipedia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class WikipediaTaskTest {

    @Test
    public void shouldRemoveUnnecessarySignsFromTheText() {
        //Given
        String givenDescription = "Matsumoto has said that Ruby was conceived in 1993. In a 1999 post to the ruby-talk " +
                                  "mailing list, he describes some of his early ideas about the language:[11]";
        String regex = "(\\[\\d+\\])";

        // Then
        String desiredResult = "Matsumoto has said that Ruby was conceived in 1993. In a 1999 post to the ruby-talk " +
                               "mailing list, he describes some of his early ideas about the language:";

        Assertions.assertEquals(desiredResult, givenDescription.replaceAll(regex, EMPTY));
    }
}
