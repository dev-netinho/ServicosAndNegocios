package com.deefy.group2.service;

import com.deefy.group2.model.Playlist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaylistService {

    String criarPlaylist(Long usuarioId, String nome, boolean publica);

    void atualizarPlaylist(Long playlistId, String nome, boolean publica);

    void adicionarMusica(Long playlistId, Long musicaId);

    void removerMusica(Long playlistId, Long musicaId);

    void reordenarMusicas(Long playlistId, Long usuarioId, List<Long> musicasOrdenadas);

    List<Long> listarMusicasDaPlaylist(Long playlistId);

}
