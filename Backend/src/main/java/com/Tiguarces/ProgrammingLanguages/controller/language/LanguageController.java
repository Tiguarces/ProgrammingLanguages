package com.Tiguarces.ProgrammingLanguages.controller.language;

import com.Tiguarces.ProgrammingLanguages.controller.language.dto.LanguageDTO;
import com.Tiguarces.ProgrammingLanguages.service.language.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.Tiguarces.ProgrammingLanguages.controller.ControllerConstants.NOT_FOUND_RESPONSE;
import static com.Tiguarces.ProgrammingLanguages.utils.SwaggerConstants.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.CollectionUtils.isEmpty;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/language")
@Tag(name = "Language", description = "Main, scraped language controller")
public class LanguageController implements ControllerTemplate {
    private final LanguageService languageService;

    @Override
    @GetMapping(path = "/all")
    @Operation(summary = "Retrieve all languages", tags = { GET, ALL })
    @ApiResponses({
            @ApiResponse(responseCode = OK_STATUS          , content = { @Content(schema = @Schema(implementation = LanguageDTO.class), mediaType = APPLICATION_JSON ) }),
            @ApiResponse(responseCode = NOT_FOUND_STATUS   , content = { @Content(schema = @Schema()) }, description = "When Languages has not been found"),
            @ApiResponse(responseCode = SERVER_ERROR_STATUS, content = { @Content(schema = @Schema()) }, description = "When any problem occurs")
    })
    public ResponseEntity<?> getAll() {
        var foundLanguages = languageService.findAll();

        return (isEmpty(foundLanguages))
                 ? NOT_FOUND_RESPONSE : new ResponseEntity<>(foundLanguages, OK);
    }


    @Override
    @GetMapping(path = "/get")
    @Operation(summary = "Retrieve specified language by name", tags = { GET, BY_NAME })
    @ApiResponses({
            @ApiResponse(responseCode = OK_STATUS          , content = { @Content(schema = @Schema(implementation = LanguageDTO.class), mediaType = APPLICATION_JSON ) }),
            @ApiResponse(responseCode = NOT_FOUND_STATUS   , content = { @Content(schema = @Schema()) }, description = "When Language has not been found"),
            @ApiResponse(responseCode = SERVER_ERROR_STATUS, content = { @Content(schema = @Schema()) }, description = "When any problem occurs")
    })
    public ResponseEntity<?> getLanguageDetails(@RequestParam("language") final String languageName) {
        return new ResponseEntity<>(languageService.getLanguageDetails(languageName), OK);
    }
}
