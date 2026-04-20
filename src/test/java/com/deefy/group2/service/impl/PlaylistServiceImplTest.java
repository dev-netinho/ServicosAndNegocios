package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.PlaylistRequest;
import com.deefy.group2.dto.response.PlaylistResponse;
import com.deefy.group2.exception.InvalidPlaylistNameException;
import com.deefy.group2.exception.MusicNotFoundException;
import com.deefy.group2.exception.MusicInvalidOrderPlaylistException;
import com.deefy.group2.exception.MusicIsNotOnPlaylistException;
import com.deefy.group2.exception.PlaylistAccessDeniedException;
import com.deefy.group2.exception.PlaylistDomainException;
import com.deefy.group2.exception.UserNotFoundException;
import com.deefy.group2.model.Music;
import com.deefy.group2.model.Playlist;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.MusicRepository;
import com.deefy.group2.repository.PlaylistRepository;
import com.deefy.group2.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceImplTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MusicRepository musicRepository;

    @InjectMocks
    private PlaylistServiceImpl playlistService;

    @Test
    @DisplayName("Deve criar playlist com sucesso")
    void deveCriarPlaylistComSucesso() {
        User owner = user(7L);
        PlaylistRequest request = new PlaylistRequest(7L, " Minha playlist ", true);

        when(userRepository.findById(7L)).thenReturn(Optional.of(owner));
        when(playlistRepository.save(any(Playlist.class)))
                .thenAnswer(invocation -> {
                    Playlist playlist = invocation.getArgument(0);
                    return new Playlist(10L, playlist.getOwner(), playlist.getName(), playlist.isPublica(), playlist.getTracks());
                });

        PlaylistResponse response = playlistService.criarPlaylist(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.usuarioId()).isEqualTo(7L);
        assertThat(response.nome()).isEqualTo("Minha playlist");
        assertThat(response.publica()).isTrue();
        assertThat(response.musicaIds()).isEmpty();
        assertThat(response.quantidadeMusicas()).isZero();
    }

    @Test
    @DisplayName("Deve rejeitar criacao de playlist sem nome")
    void deveRejeitarCriacaoDePlaylistSemNome() {
        PlaylistRequest request = new PlaylistRequest(7L, "   ", false);

        assertThatThrownBy(() -> playlistService.criarPlaylist(request))
                .isInstanceOf(InvalidPlaylistNameException.class)
                .hasMessageContaining("nome");

        verify(playlistRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar erro quando usuario dono nao existir")
    void deveLancarErroQuandoUsuarioDonoNaoExistir() {
        PlaylistRequest request = new PlaylistRequest(99L, "Nova playlist", true);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playlistService.criarPlaylist(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Deve atualizar playlist quando usuario for o dono")
    void deveAtualizarPlaylistQuandoUsuarioForODono() {
        User owner = user(7L);
        Playlist playlist = new Playlist(20L, owner, "Antiga", false, List.of());

        when(userRepository.existsById(7L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlaylistResponse response = playlistService.atualizarPlaylist(20L, new PlaylistRequest(7L, "Atualizada", true));

        assertThat(response.id()).isEqualTo(20L);
        assertThat(response.nome()).isEqualTo("Atualizada");
        assertThat(response.publica()).isTrue();
    }

    @Test
    @DisplayName("Deve impedir edicao por usuario que nao seja dono")
    void deveImpedirEdicaoPorUsuarioQueNaoSejaDono() {
        User owner = user(7L);
        Playlist playlist = new Playlist(20L, owner, "Antiga", false, List.of());

        when(userRepository.existsById(9L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));

        assertThatThrownBy(() -> playlistService.atualizarPlaylist(20L, new PlaylistRequest(9L, "Atualizada", true)))
                .isInstanceOf(PlaylistAccessDeniedException.class)
                .hasMessageContaining("dono");
    }

    @Test
    @DisplayName("Deve adicionar musica a playlist")
    void deveAdicionarMusicaAPlaylist() {
        User owner = user(7L);
        Music music = music(30L);
        Playlist playlist = new Playlist(20L, owner, "Minha", true, List.of());

        when(userRepository.existsById(7L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));
        when(musicRepository.findById(30L)).thenReturn(Optional.of(music));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlaylistResponse response = playlistService.adicionarMusica(20L, 7L, 30L);

        assertThat(response.musicaIds()).containsExactly(30L);
        assertThat(response.quantidadeMusicas()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve lancar erro ao adicionar musica inexistente")
    void deveLancarErroAoAdicionarMusicaInexistente() {
        User owner = user(7L);
        Playlist playlist = new Playlist(20L, owner, "Minha", true, List.of());

        when(userRepository.existsById(7L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));
        when(musicRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> playlistService.adicionarMusica(20L, 7L, 404L))
                .isInstanceOf(MusicNotFoundException.class)
                .hasMessageContaining("404");
    }

    @Test
    @DisplayName("Deve remover musica da playlist")
    void deveRemoverMusicaDaPlaylist() {
        User owner = user(7L);
        Music first = music(1L);
        Music second = music(2L);
        Playlist playlist = new Playlist(20L, owner, "Minha", true, List.of(first, second));

        when(userRepository.existsById(7L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlaylistResponse response = playlistService.removerMusica(20L, 7L, 1L);

        assertThat(response.musicaIds()).containsExactly(2L);
    }

    @Test
    @DisplayName("Deve lancar erro ao remover musica ausente")
    void deveLancarErroAoRemoverMusicaAusente() {
        User owner = user(7L);
        Playlist playlist = new Playlist(20L, owner, "Minha", true, List.of(music(2L)));

        when(userRepository.existsById(7L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));

        assertThatThrownBy(() -> playlistService.removerMusica(20L, 7L, 1L))
                .isInstanceOf(MusicIsNotOnPlaylistException.class)
                .hasMessageContaining("nao esta presente");
    }

    @Test
    @DisplayName("Deve reordenar musicas da playlist")
    void deveReordenarMusicasDaPlaylist() {
        User owner = user(7L);
        Music first = music(1L);
        Music second = music(2L);
        Music third = music(3L);
        Playlist playlist = new Playlist(20L, owner, "Minha", true, List.of(first, second, third));

        when(userRepository.existsById(7L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlaylistResponse response = playlistService.reordenarMusicas(20L, 7L, List.of(3L, 1L, 2L));

        assertThat(response.musicaIds()).containsExactly(3L, 1L, 2L);
    }

    @Test
    @DisplayName("Deve rejeitar reordenacao invalida")
    void deveRejeitarReordenacaoInvalida() {
        User owner = user(7L);
        Music first = music(1L);
        Music second = music(2L);
        Playlist playlist = new Playlist(20L, owner, "Minha", true, List.of(first, second));

        when(userRepository.existsById(7L)).thenReturn(true);
        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));

        assertThatThrownBy(() -> playlistService.reordenarMusicas(20L, 7L, List.of(1L, 3L)))
                .isInstanceOf(MusicInvalidOrderPlaylistException.class)
                .hasMessageContaining("nova ordem");
    }

    @Test
    @DisplayName("Deve impedir leitura de playlist privada por outro usuario")
    void deveImpedirLeituraDePlaylistPrivadaPorOutroUsuario() {
        User owner = user(7L);
        Playlist playlist = new Playlist(20L, owner, "Privada", false, List.of());

        when(playlistRepository.findById(20L)).thenReturn(Optional.of(playlist));

        assertThatThrownBy(() -> playlistService.listarMusicasDaPlaylist(20L, 9L))
                .isInstanceOf(PlaylistAccessDeniedException.class)
                .hasMessageContaining("visualizar");
    }

    private static User user(Long id) {
        User user = mock(User.class);
        when(user.getId()).thenReturn(id);
        return user;
    }

    private static Music music(Long id) {
        Music music = mock(Music.class);
        when(music.getId()).thenReturn(id);
        return music;
    }
}
