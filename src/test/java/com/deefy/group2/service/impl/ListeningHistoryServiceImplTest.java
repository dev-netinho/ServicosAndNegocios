package com.deefy.group2.service.impl;

import com.deefy.group2.dto.request.ListeningHistoryRequest;
import com.deefy.group2.dto.response.ListeningHistoryResponse;
import com.deefy.group2.exception.UserNotFoundException;
import com.deefy.group2.mapper.ListeningHistoryMapper;
import com.deefy.group2.model.ListeningHistory;
import com.deefy.group2.model.Music;
import com.deefy.group2.model.User;
import com.deefy.group2.repository.ListeningHistoryRepository;
import com.deefy.group2.repository.MusicRepository;
import com.deefy.group2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
        User user = mock(User.class);
        Music music = mock(Music.class);
        ListeningHistory entity = new ListeningHistory();
        ListeningHistory savedEntity = new ListeningHistory();
        ListeningHistoryResponse expectedResponse = mock(ListeningHistoryResponse.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(musicRepository.findById(2L)).thenReturn(Optional.of(music));
        when(listeningHistoryMapper.toEntity(request)).thenReturn(entity);
        when(listeningHistoryRepository.save(entity)).thenReturn(savedEntity);
        when(listeningHistoryMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        ListeningHistoryResponse out = service.saveListeningHistory(request);

        assertThat(out).isSameAs(expectedResponse);
        assertThat(entity.getUser()).isSameAs(user);
        assertThat(entity.getMusic()).isSameAs(music);
        assertThat(entity.getDataHoraExecucao()).isNotNull();
        verify(userRepository).findById(1L);
        verify(musicRepository).findById(2L);
        verify(listeningHistoryRepository).save(entity);
    }

    @Test
    void saveListeningHistory_quandoUserNaoExiste_lancaExcecao() {
        ListeningHistoryRequest request = new ListeningHistoryRequest(99L, 2L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.saveListeningHistory(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");

        verifyNoInteractions(musicRepository, listeningHistoryRepository, listeningHistoryMapper);
    }
}
