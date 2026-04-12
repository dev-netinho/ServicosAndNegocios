package com.deefy.group2.controller;

import com.deefy.group2.dto.response.MusicSearchResponseDto;
import com.deefy.group2.service.MusicSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/music", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Music search", description = "Busca b\u00e1sica por t\u00edtulo, artista e g\u00eanero")
public class MusicSearchController {

    private final MusicSearchService musicSearchService;

    public MusicSearchController(MusicSearchService musicSearchService) {
        this.musicSearchService = musicSearchService;
    }

    @GetMapping("/search")
    @Operation(
            summary = "Buscar m\u00fasicas",
            description = "Par\u00e2metros em ingl\u00eas: title, artist, genre. Com filtros: AND entre os preenchidos (LIKE, sem diferenciar mai\u00fasculas). "
                    + "Sem nenhum filtro: devolve at\u00e9 50 faixas do cat\u00e1logo ordenadas por t\u00edtulo.")
    public MusicSearchResponseDto search(
            @Parameter(description = "Trecho do t\u00edtulo (case-insensitive)")
            @RequestParam(required = false) String title,
            @Parameter(description = "Trecho do artista (case-insensitive)")
            @RequestParam(required = false) String artist,
            @Parameter(description = "Trecho do g\u00eanero (case-insensitive)")
            @RequestParam(required = false) String genre) {
        return musicSearchService.search(title, artist, genre);
    }
}
