package com.deefy.group2.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.deefy.group2.exception.HistoryNotFoundException;
import com.deefy.group2.exception.MusicNotFoundException;
import com.deefy.group2.exception.UserNotFoundException;
import com.deefy.group2.mapper.ListeningHistoryMapper;
import com.deefy.group2.model.ListeningHistory;
import com.deefy.group2.model.Music;
import com.deefy.group2.model.User;
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
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.userId()));

        Music music = musicRepository.findById(request.musicId())
                .orElseThrow(() -> new MusicNotFoundException(request.musicId()));

        LocalDateTime now = LocalDateTime.now();

        ListeningHistory saved = listeningHistoryMapper.toEntity(request);
        saved.setUser(user);
        saved.setMusic(music);
        saved.setDataHoraExecucao(now);

        ListeningHistory response = listeningHistoryRepository.save(saved);

        return listeningHistoryMapper.toResponse(response);
    }


    @Override
    public List<ListeningHistoryResponse> getHistoryByUserId(Long userId) {
        if  (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        List<ListeningHistory> history = listeningHistoryRepository.findAllByUserIdOrderByDataHoraExecucaoDesc(userId);

        if (history.isEmpty()) {
            throw new HistoryNotFoundException("Listening history not found for user id: " + userId);
        }

        return history.stream()
                .map(listeningHistoryMapper::toResponse)
                .toList();
    }
}
