package com.deefy.group2.service.impl;

import com.deefy.group2.dto.response.MusicSearchResponseDto;
import com.deefy.group2.model.Music;
import com.deefy.group2.repository.MusicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MusicSearchServiceImplTest {

    @Mock
    private MusicRepository musicRepository;

    @InjectMocks
    private MusicSearchServiceImpl musicSearchService;

    @Test
    void search_semCriterios_retornaAte50DoCatalogo() {
        when(musicRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new Music(1L, "A Song", "Band", "Rock", 180))));

        MusicSearchResponseDto out = musicSearchService.search("  ", null, "");

        assertThat(out.count()).isEqualTo(1);
        assertThat(out.results().get(0).title()).isEqualTo("A Song");
        verify(musicRepository).findAll(any(Pageable.class));
        verify(musicRepository, never()).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    void search_comTitulo_usaSpecification() {
        when(musicRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(List.of(new Music(1L, "Rock Song", "Band", "Rock", 180)));

        MusicSearchResponseDto out = musicSearchService.search("rock", null, null);

        assertThat(out.count()).isEqualTo(1);
        assertThat(out.results().get(0).title()).isEqualTo("Rock Song");
        verify(musicRepository).findAll(any(Specification.class), any(Sort.class));
        verify(musicRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void search_semResultados_retornaListaVaziaECountZero() {
        when(musicRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        MusicSearchResponseDto out = musicSearchService.search(null, "Nobody", null);

        assertThat(out.count()).isZero();
        assertThat(out.results()).isEmpty();
    }
}
