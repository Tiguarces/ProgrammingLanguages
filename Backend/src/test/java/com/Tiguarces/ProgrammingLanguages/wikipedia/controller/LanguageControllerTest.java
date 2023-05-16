package com.Tiguarces.ProgrammingLanguages.wikipedia.controller;

import com.Tiguarces.ProgrammingLanguages.controller.language.LanguageController;
import com.Tiguarces.ProgrammingLanguages.controller.language.dto.LanguageDTO;
import com.Tiguarces.ProgrammingLanguages.controller.language.dto.LanguageDetailsDTO;
import com.Tiguarces.ProgrammingLanguages.service.language.LanguageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.Tiguarces.ProgrammingLanguages.utils.SwaggerConstants.SERVER_ERROR_STATUS;
import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@RunWith(SpringRunner.class)
@WebMvcTest(LanguageController.class)
public class LanguageControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private LanguageService languageService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldOkStatusAndContent__getAll() throws Exception {
        when(languageService.findAll())
                .thenReturn(List.of(new LanguageDTO("C#", null), new LanguageDTO("Java", anyString())));

        mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/language/all")
                            .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$.size()").isNotEmpty())
         .andExpect(jsonPath("$.size()").value(2))
         .andExpect(jsonPath("$[0].name").value("C#"))
         .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundStatusAndNotFound__getAll() throws Exception {
        when(languageService.findAll())
                .thenReturn(emptyList());

        mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/language/all")
                            .contentType(APPLICATION_JSON)
        ).andExpect(status().isNotFound())
         .andDo(print());
    }

    @Test
    public void shouldReturnServerInternalErrorStatusAndExceptionIsThrown__getAll() throws Exception {
        when(languageService.findAll())
                .thenThrow(NullPointerException.class);

        mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/language/all")
                            .contentType(APPLICATION_JSON)
        ).andExpect(status().is(parseInt(SERVER_ERROR_STATUS)))
         .andDo(print());
    }


    @Test
    public void shouldReturnDesiredLanguage__getLanguageDetails() throws Exception {
        String languageName = "Java";
        LanguageDetailsDTO result = new LanguageDetailsDTO(languageName, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
                                                                         EMPTY, 0, emptyList(), emptyList());

        when(languageService.getLanguageDetails(languageName))
                .thenReturn(result);

        mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/language/get")
                            .param("language", languageName)
        ).andExpect(status().isOk())
         .andExpect(jsonPath("$.name").value(languageName))
         .andDo(print());
    }
}
