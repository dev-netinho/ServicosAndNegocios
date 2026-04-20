package com.deefy.group2.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PlaylistCreationRequest(
        @NotBlank(message = "O nome da Playlist é obrigatório.")
        String playlistNome,
        boolean playlistEPublica
) {
}
