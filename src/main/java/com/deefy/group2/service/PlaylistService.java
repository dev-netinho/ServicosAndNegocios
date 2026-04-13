package com.deefy.group2.service;

import java.util.List;

public interface PlaylistService {

    String criarPlaylist(String usuarioId, String nome, boolean publica);

    void atualizarPlaylist(String playlistId, String usuarioId, String nome, boolean publica);

    void adicionarMusica(String playlistId, String usuarioId, String musicaId);

    void removerMusica(String playlistId, String usuarioId, String musicaId);

    void reordenarMusicas(String playlistId, String usuarioId, List<String> musicasOrdenadas);

    List<String> listarMusicasDaPlaylist(String playlistId, String usuarioId);
}
