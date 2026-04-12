package com.deefy.group2.service.impl;

import com.deefy.group2.dto.response.MusicResponseDto;
import com.deefy.group2.dto.response.MusicSearchResponseDto;
import com.deefy.group2.model.Music;
import com.deefy.group2.repository.MusicRepository;
import com.deefy.group2.repository.MusicSpecifications;
import com.deefy.group2.service.MusicSearchService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MusicSearchServiceImpl implements MusicSearchService {

    private final MusicRepository musicRepository;

    public MusicSearchServiceImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public MusicSearchResponseDto search(String title, String artist, String genre) {
        String t = blankToNull(title);
        String a = blankToNull(artist);
        String g = blankToNull(genre);

        if (t == null && a == null && g == null) {
            List<Music> catalog = musicRepository
                    .findAll(PageRequest.of(0, 50, Sort.by(Sort.Direction.ASC, "title")))
                    .getContent();
            return MusicSearchResponseDto.of(catalog.stream().map(MusicResponseDto::from).toList());
        }

        List<Music> rows = musicRepository.findAll(
                MusicSpecifications.withOptionalFilters(t, a, g),
                Sort.by(Sort.Direction.ASC, "title"));
        List<MusicResponseDto> dtos = rows.stream().map(MusicResponseDto::from).toList();
        return MusicSearchResponseDto.of(dtos);
    }

    private static String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
