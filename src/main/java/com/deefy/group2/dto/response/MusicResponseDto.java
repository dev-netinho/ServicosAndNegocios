package com.deefy.group2.dto.response;

import com.deefy.group2.model.Music;

public record MusicResponseDto(
        Long id,
        String title,
        String artist,
        String genre,
        Integer durationSeconds,
        String previewUrl,
        String coverUrl,
        String externalId) {

    public static MusicResponseDto from(Music music) {
        return new MusicResponseDto(
                music.getId(),
                music.getTitle(),
                music.getArtist(),
                music.getGenre(),
                music.getDurationSeconds(),
                music.getPreviewUrl(),
                music.getCoverUrl(),
                music.getExternalId());
    }
}
