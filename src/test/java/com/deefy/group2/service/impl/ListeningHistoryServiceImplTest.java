package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.ListeningHistoryRequest;
import com.deefy.group2.dto.response.ListeningHistoryResponse;
import com.deefy.group2.mapper.ListeningHistoryMapper;
import com.deefy.group2.model.Artist;
import com.deefy.group2.model.ListeningHistory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.deefy.group2.service.ListeningHistoryService;
import com.deefy.group2.repository.ListeningHistoryRepository;
import com.deefy.group2.repository.UserRepository;
import com.deefy.group2.repository.MusicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListeningHistoryServiceImplTest {

    @Mock
    private ListeningHistoryRepository listeningHistoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MusicRepository musicRepository;
    @Mock
    private ListeningHistoryMapper listeningHistoryMapper;

    @InjectMocks
    private ListeningHistoryServiceImpl service;

    @Test
    void saveListeningHistory_quandoDadosValidos_salvaERetornaResponse() {
        ListeningHistoryRequest request = new ListeningHistoryRequest(1L, 2L);
        ListeningHistory entity = new ListeningHistory();
        ListeningHistory savedEntity = new ListeningHistory();
        ListeningHistoryResponse expectedResponse = mock(ListeningHistoryResponse.class);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(musicRepository.existsById(2L)).thenReturn(true);
        when(listeningHistoryMapper.toEntity(request)).thenReturn(entity);
        when(listeningHistoryRepository.save(entity)).thenReturn(savedEntity);
        when(listeningHistoryMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        ListeningHistoryResponse out = service.saveListeningHistory(request);

        assertThat(out).isSameAs(expectedResponse);
        assertThat(entity.getDataHoraExecucao()).isNotNull();
        verify(userRepository).existsById(1L);
        verify(musicRepository).existsById(2L);
        verify(listeningHistoryRepository).save(entity);
    }

    @Test
    void saveListeningHistory_quandoUserNaoExiste_lancaExcecao() {
        ListeningHistoryRequest request = new ListeningHistoryRequest(99L, 2L);
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.saveListeningHistory(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");

        verifyNoInteractions(musicRepository, listeningHistoryRepository, listeningHistoryMapper);
    }
}