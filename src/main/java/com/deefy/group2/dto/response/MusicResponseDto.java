package com.deefy.group2.dto.response;

import com.deefy.group2.model.Music;

import java.time.Instant;

public record MusicResponseDto(
        Long id,
        String title,
        String artist,
        String genre,
        Integer durationSeconds,
        String previewUrl,
        String coverUrl,
        String externalId,
        Instant createdAt) {

    public static MusicResponseDto from(Music music) {
        return new MusicResponseDto(
                music.getId(),
                music.getTitle(),
                music.getArtist(),
                music.getGenre(),
                music.getDurationSeconds(),
                music.getPreviewUrl(),
                music.getCoverUrl(),
                music.getExternalId(),
                music.getCreatedAt());
    }
}
