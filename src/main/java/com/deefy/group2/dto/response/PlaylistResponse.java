package com.deefy.group2.dto.response;

import com.deefy.group2.model.Playlist;

import java.util.List;

public record PlaylistResponse(
        Long id,
        Long usuarioId,
        String nome,
        boolean publica,
        List<Long> musicaIds,
        int quantidadeMusicas) {

    public static PlaylistResponse from(Playlist playlist) {
        List<Long> musicaIds = playlist.getTrackIds();
        Long usuarioId = playlist.getOwner() == null ? null : playlist.getOwner().getId();

        return new PlaylistResponse(
                playlist.getId(),
                usuarioId,
                playlist.getName(),
                playlist.isPublica(),
                List.copyOf(musicaIds),
                musicaIds.size());
    }
}
