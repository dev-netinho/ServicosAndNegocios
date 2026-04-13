package com.deefy.group2.service.impl;

import com.deefy.group2.dto.response.MusicRatingResponseDto;
import com.deefy.group2.exception.InvalidMusicRatingScoreException;
import com.deefy.group2.exception.MusicNotFoundException;
import com.deefy.group2.model.Album;
import com.deefy.group2.model.Artist;
import com.deefy.group2.model.Music;
import com.deefy.group2.model.MusicRating;
import com.deefy.group2.repository.MusicRatingRepository;
import com.deefy.group2.repository.MusicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MusicRatingServiceImplTest {

    @Mock
    private MusicRepository musicRepository;

    @Mock
    private MusicRatingRepository musicRatingRepository;

    @InjectMocks
    private MusicRatingServiceImpl musicRatingService;

    private static Music sampleMusic(long id) {
        Artist band = new Artist(1L, "Band");
        Album album = new Album(1L, "LP", band);
        return new Music(id, "Track", "Rock", 180, album);
    }

    @Test
    void rateMusic_notaNula_lancaInvalidMusicRatingScoreException() {
        assertThatThrownBy(() -> musicRatingService.rateMusic(1L, 10L, null))
                .isInstanceOf(InvalidMusicRatingScoreException.class)
                .hasMessageContaining("obrigatória");
    }

    @Test
    void rateMusic_notaAbaixoDoMinimo_lancaInvalidMusicRatingScoreException() {
        assertThatThrownBy(() -> musicRatingService.rateMusic(1L, 10L, 0))
                .isInstanceOf(InvalidMusicRatingScoreException.class)
                .hasMessageContaining("Nota inválida");
    }

    @Test
    void rateMusic_notaAcimaDoMaximo_lancaInvalidMusicRatingScoreException() {
        assertThatThrownBy(() -> musicRatingService.rateMusic(1L, 10L, 6))
                .isInstanceOf(InvalidMusicRatingScoreException.class)
                .hasMessageContaining("Nota inválida");
    }

    @Test
    void rateMusic_musicaInexistente_lancaMusicNotFoundException() {
        when(musicRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> musicRatingService.rateMusic(1L, 99L, 3))
                .isInstanceOf(MusicNotFoundException.class)
                .hasMessageContaining("99");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void rateMusic_limitesDaFaixa_aceita(int score) {
        Music music = sampleMusic(10L);
        when(musicRepository.findById(10L)).thenReturn(Optional.of(music));
        when(musicRatingRepository.findByUserIdAndMusic_Id(1L, 10L)).thenReturn(Optional.empty());
        when(musicRatingRepository.save(any(MusicRating.class)))
                .thenAnswer(inv -> {
                    MusicRating r = inv.getArgument(0);
                    return new MusicRating(100L, r.getUserId(), r.getMusic(), r.getScore(), r.getRatedAt());
                });

        MusicRatingResponseDto out = musicRatingService.rateMusic(1L, 10L, score);

        assertThat(out.score()).isEqualTo(score);
    }

    @Test
    void rateMusic_primeiraAvaliacao_persisteERetornaComUpdatedFalse() {
        Music music = sampleMusic(10L);
        when(musicRepository.findById(10L)).thenReturn(Optional.of(music));
        when(musicRatingRepository.findByUserIdAndMusic_Id(1L, 10L)).thenReturn(Optional.empty());
        when(musicRatingRepository.save(any(MusicRating.class)))
                .thenAnswer(inv -> {
                    MusicRating r = inv.getArgument(0);
                    return new MusicRating(100L, r.getUserId(), r.getMusic(), r.getScore(), r.getRatedAt());
                });

        MusicRatingResponseDto out = musicRatingService.rateMusic(1L, 10L, 4);

        assertThat(out.userId()).isEqualTo(1L);
        assertThat(out.musicId()).isEqualTo(10L);
        assertThat(out.score()).isEqualTo(4);
        assertThat(out.updated()).isFalse();
        assertThat(out.ratedAt()).isNotNull();
        verify(musicRatingRepository).save(any(MusicRating.class));
    }

    @Test
    void rateMusic_avaliacaoExistente_atualizaERetornaComUpdatedTrue() {
        Music music = sampleMusic(10L);
        LocalDateTime antes = LocalDateTime.of(2020, 1, 1, 12, 0);
        MusicRating existing = new MusicRating(50L, 1L, music, 2, antes);
        when(musicRepository.findById(10L)).thenReturn(Optional.of(music));
        when(musicRatingRepository.findByUserIdAndMusic_Id(1L, 10L)).thenReturn(Optional.of(existing));
        when(musicRatingRepository.save(any(MusicRating.class))).thenAnswer(inv -> inv.getArgument(0));

        MusicRatingResponseDto out = musicRatingService.rateMusic(1L, 10L, 5);

        assertThat(out.score()).isEqualTo(5);
        assertThat(out.updated()).isTrue();
        assertThat(existing.getRatedAt()).isAfter(antes);
    }
}
