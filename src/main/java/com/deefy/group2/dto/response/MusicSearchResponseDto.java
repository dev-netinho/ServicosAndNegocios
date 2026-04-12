package com.deefy.group2.dto.response;

import java.util.List;

public record MusicSearchResponseDto(List<MusicResponseDto> results, int count) {

    public static MusicSearchResponseDto of(List<MusicResponseDto> results) {
        return new MusicSearchResponseDto(List.copyOf(results), results.size());
    }
}
