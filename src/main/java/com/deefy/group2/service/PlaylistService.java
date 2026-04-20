package com.deefy.group2.service;

import com.deefy.group2.dto.request.PlaylistRequest;
import com.deefy.group2.dto.response.PlaylistResponse;

import java.util.List;

public interface PlaylistService {

    PlaylistResponse criarPlaylist(PlaylistRequest request);

    PlaylistResponse atualizarPlaylist(Long playlistId, PlaylistRequest request);

    PlaylistResponse adicionarMusica(Long playlistId, Long usuarioId, Long musicaId);

    PlaylistResponse removerMusica(Long playlistId, Long usuarioId, Long musicaId);

    PlaylistResponse reordenarMusicas(Long playlistId, Long usuarioId, List<Long> musicasOrdenadas);

    List<Long> listarMusicasDaPlaylist(Long playlistId, Long usuarioId);
}
