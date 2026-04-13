package com.deefy.group2.service.impl;

import java.time.LocalDateTime;

import com.deefy.group2.mapper.ListeningHistoryMapper;
import com.deefy.group2.model.ListeningHistory;
import org.springframework.stereotype.Service;

import com.deefy.group2.dto.response.ListeningHistoryResponse;
import com.deefy.group2.service.ListeningHistoryService;
import com.deefy.group2.repository.ListeningHistoryRepository;
import com.deefy.group2.repository.MusicRepository;
import com.deefy.group2.dto.request.ListeningHistoryRequest;
import com.deefy.group2.repository.UserRepository;

@Service
public class ListeningHistoryServiceImpl implements ListeningHistoryService {

    private final ListeningHistoryRepository listeningHistoryRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final ListeningHistoryMapper listeningHistoryMapper;

    public ListeningHistoryServiceImpl(ListeningHistoryRepository listeningHistoryRepository, UserRepository userRepository, MusicRepository musicRepository, ListeningHistoryMapper listeningHistoryMapper) {
        this.listeningHistoryRepository = listeningHistoryRepository;
        this.userRepository = userRepository;
        this.musicRepository = musicRepository;
        this.listeningHistoryMapper = listeningHistoryMapper;
    }

	@Override
	public ListeningHistoryResponse saveListeningHistory(ListeningHistoryRequest request) {

        if (!userRepository.existsById(request.userId())) {
            throw new IllegalArgumentException("User not found with id: " + request.userId());
        }

        if (!musicRepository.existsById(request.musicId())) {
            throw new IllegalArgumentException("Music not found with id: " + request.musicId());
        }

        LocalDateTime now = LocalDateTime.now();

        ListeningHistory saved = listeningHistoryMapper.toEntity(request);
        saved.setDataHoraExecucao(now);

        ListeningHistory response = listeningHistoryRepository.save(saved);

        return listeningHistoryMapper.toResponse(response);
    }
}
