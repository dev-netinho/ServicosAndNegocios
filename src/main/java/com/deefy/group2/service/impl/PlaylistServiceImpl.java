package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.PlaylistRequest;
import com.deefy.group2.dto.response.PlaylistResponse;
import com.deefy.group2.exception.MusicNotFoundException;
import com.deefy.group2.exception.PlaylistAccessDeniedException;
import com.deefy.group2.exception.PlaylistDomainException;
import com.deefy.group2.exception.PlaylistNotFoundException;
import com.deefy.group2.exception.UserNotFoundException;
import com.deefy.group2.model.Music;
import com.deefy.group2.model.Playlist;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.MusicRepository;
import com.deefy.group2.repository.PlaylistRepository;
import com.deefy.group2.repository.UserRepository;
import com.deefy.group2.service.PlaylistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

    public PlaylistServiceImpl(
            PlaylistRepository playlistRepository,
            UserRepository userRepository,
            MusicRepository musicRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.musicRepository = musicRepository;
    }

    @Override
    public PlaylistResponse criarPlaylist(PlaylistRequest request) {
        validatePlaylistRequest(request);

        User owner = userRepository.findById(request.usuarioId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.usuarioId()));

        Playlist playlist = new Playlist(null, owner, normalizeName(request.nome()), request.publica(), List.of());
        Playlist saved = playlistRepository.save(playlist);
        return PlaylistResponse.from(saved);
    }

    @Override
    public PlaylistResponse atualizarPlaylist(Long playlistId, PlaylistRequest request) {
        validatePlaylistRequest(request);

        Playlist playlist = findOwnedPlaylist(playlistId, request.usuarioId());
        playlist.setName(normalizeName(request.nome()));
        playlist.setPublica(request.publica());

        Playlist saved = playlistRepository.save(playlist);
        return PlaylistResponse.from(saved);
    }

    @Override
    public PlaylistResponse adicionarMusica(Long playlistId, Long usuarioId, Long musicaId) {
        Playlist playlist = findOwnedPlaylist(playlistId, usuarioId);
        Music music = musicRepository.findById(musicaId)
                .orElseThrow(() -> new MusicNotFoundException(musicaId));

        playlist.addTrack(music);
        Playlist saved = playlistRepository.save(playlist);
        return PlaylistResponse.from(saved);
    }

    @Override
    public PlaylistResponse removerMusica(Long playlistId, Long usuarioId, Long musicaId) {
        Playlist playlist = findOwnedPlaylist(playlistId, usuarioId);
        boolean removed = playlist.removeFirstTrackByMusicId(musicaId);

        if (!removed) {
            throw new PlaylistDomainException("A musica informada nao esta presente na playlist.");
        }

        Playlist saved = playlistRepository.save(playlist);
        return PlaylistResponse.from(saved);
    }

    @Override
    public PlaylistResponse reordenarMusicas(Long playlistId, Long usuarioId, List<Long> musicasOrdenadas) {
        Playlist playlist = findOwnedPlaylist(playlistId, usuarioId);
        validateTrackOrder(playlist, musicasOrdenadas);

        List<Music> reorderedTracks = reorderTracks(playlist.getTracks(), musicasOrdenadas);
        playlist.setTracks(reorderedTracks);

        Playlist saved = playlistRepository.save(playlist);
        return PlaylistResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> listarMusicasDaPlaylist(Long playlistId, Long usuarioId) {
        Playlist playlist = findPlaylist(playlistId);

        if (!playlist.isPublica() && !playlist.belongsTo(usuarioId)) {
            throw new PlaylistAccessDeniedException("Apenas o dono pode visualizar esta playlist privada.");
        }

        return List.copyOf(playlist.getTrackIds());
    }

    private Playlist findOwnedPlaylist(Long playlistId, Long usuarioId) {
        ensureUserExists(usuarioId);
        Playlist playlist = findPlaylist(playlistId);

        if (!playlist.belongsTo(usuarioId)) {
            throw new PlaylistAccessDeniedException("Apenas o dono pode editar esta playlist.");
        }

        return playlist;
    }

    private Playlist findPlaylist(Long playlistId) {
        if (playlistId == null) {
            throw new PlaylistDomainException("O id da playlist e obrigatorio.");
        }

        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException(playlistId));
    }

    private void ensureUserExists(Long usuarioId) {
        if (usuarioId == null || !userRepository.existsById(usuarioId)) {
            throw new UserNotFoundException("User not found with id: " + usuarioId);
        }
    }

    private static void validatePlaylistRequest(PlaylistRequest request) {
        if (request == null) {
            throw new PlaylistDomainException("Os dados da playlist sao obrigatorios.");
        }
        if (request.usuarioId() == null) {
            throw new PlaylistDomainException("O id do usuario dono da playlist e obrigatorio.");
        }
        validateName(request.nome());
    }

    private static void validateName(String nome) {
        if (nome == null || nome.strip().isEmpty()) {
            throw new PlaylistDomainException("O nome da playlist e obrigatorio.");
        }
    }

    private static String normalizeName(String nome) {
        return nome.strip();
    }

    private static void validateTrackOrder(Playlist playlist, List<Long> musicasOrdenadas) {
        if (musicasOrdenadas == null) {
            throw new PlaylistDomainException("A nova ordem de musicas e obrigatoria.");
        }

        List<Long> currentIds = playlist.getTrackIds();
        if (currentIds.size() != musicasOrdenadas.size()
                || !countOccurrences(currentIds).equals(countOccurrences(musicasOrdenadas))) {
            throw new PlaylistDomainException(
                    "A nova ordem deve conter exatamente as musicas atuais da playlist.");
        }
    }

    private static Map<Long, Long> countOccurrences(List<Long> ids) {
        return ids.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static List<Music> reorderTracks(List<Music> currentTracks, List<Long> orderedIds) {
        Map<Long, ArrayDeque<Music>> tracksById = new HashMap<>();
        for (Music track : currentTracks) {
            tracksById.computeIfAbsent(track.getId(), ignored -> new ArrayDeque<>()).add(track);
        }

        List<Music> reordered = new ArrayList<>(orderedIds.size());
        for (Long musicId : orderedIds) {
            reordered.add(tracksById.get(musicId).removeFirst());
        }
        return reordered;
    }
}
