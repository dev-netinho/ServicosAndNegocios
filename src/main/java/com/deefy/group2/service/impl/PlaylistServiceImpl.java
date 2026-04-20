package com.deefy.group2.service.impl;


import com.deefy.group2.exception.*;
import com.deefy.group2.model.Music;
import com.deefy.group2.model.Playlist;
import com.deefy.group2.model.PlaylistMusic;
import com.deefy.group2.repository.MusicRepository;
import com.deefy.group2.repository.PlaylistRepository;
import com.deefy.group2.service.PlaylistService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicaRepositorio;

    public PlaylistServiceImpl(
            PlaylistRepository playlistRepository,
            MusicRepository musicaRepositorio
    ) {
        this.playlistRepository = playlistRepository;
        this.musicaRepositorio = musicaRepositorio;
    }

    @Override
    public String criarPlaylist(Long usuarioId, String nome, boolean publica) {

        if (nome == null || nome.length() < 3) {
            throw new InvalidPlaylistNameException("Nome inválido");
        }

        Playlist playlist = new Playlist();
        playlist.setNome(nome);
        playlist.setEPublica(publica);
        playlist.setUsuarioId(usuarioId);
        playlist.setDataCriacao(LocalDateTime.now());

        playlistRepository.save(playlist);

        return "Playlist criada com sucesso";
    }

    @Override
    public void atualizarPlaylist(Long playlistId, String nome, boolean publica) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist não encontrada"));

        if (nome != null && nome.length() >= 3) {
            playlist.setNome(nome);
        }

        playlist.setEPublica(publica);

        playlistRepository.save(playlist);
    }

    @Override
    public void adicionarMusica(Long playlistId, Long musicaId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist não encontrada"));

        Music musica = musicaRepositorio.findById(musicaId)
                .orElseThrow(() -> new MusicNotFoundException(musicaId));

        boolean jaExiste = playlist.getMusicas().stream()
                .anyMatch(pm -> pm.getMusica().getId().equals(musicaId));

        if (jaExiste) {
            throw new MusicAlreadyOnPlaylistException("Música já está na playlist");
        }

        int ordem = playlist.getMusicas().size() + 1;

        PlaylistMusic pm = new PlaylistMusic();
        pm.setPlaylist(playlist);
        pm.setMusica(musica);
        pm.setOrdem(ordem);

        playlist.getMusicas().add(pm);

        playlistRepository.save(playlist);
    }

    @Override
    public void removerMusica(Long playlistId, Long musicaId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist não encontrada"));

        boolean removeu = playlist.getMusicas().removeIf(
                pm -> pm.getMusica().getId().equals(musicaId)
        );

        if (!removeu) {
            throw new MusicIsNotOnPlaylistException("Música não está na playlist");
        }

        playlistRepository.save(playlist);
    }

    @Override
    public void reordenarMusicas(Long playlistId, Long usuarioId, List<Long> musicasOrdenadas) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist não encontrada"));

        if (!playlist.getUsuarioId().equals(usuarioId)) {
            // throw new BusinessException("Usuário não autorizado");
            throw new RuntimeException("Usuário não autorizado");
        }

        Map<Long, PlaylistMusic> mapa = playlist.getMusicas().stream()
                .collect(Collectors.toMap(
                        pm -> pm.getMusica().getId(),
                        pm -> pm
                ));

        int ordem = 1;

        for (Long musicaId : musicasOrdenadas) {
            PlaylistMusic pm = mapa.get(musicaId);

            if (pm == null) {
                throw new MusicInvalidOrderPlaylistException("Música inválida na ordenação");
            }

            pm.setOrdem(ordem++);
        }

        playlistRepository.save(playlist);
    }

    @Override
    public List<Long> listarMusicasDaPlaylist(Long playlistId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist não encontrada"));

        return playlist.getMusicas().stream()
                .sorted(Comparator.comparing(PlaylistMusic::getOrdem))
                .map(pm -> pm.getMusica().getId())
                .toList();
    }

    
}
