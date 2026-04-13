package com.deefy.group2.dto.response;

import com.deefy.group2.model.MusicRating;

import java.time.LocalDateTime;

public record MusicRatingResponseDto(
        Long id, Long userId, Long musicId, int score, LocalDateTime ratedAt, boolean updated) {

    public static MusicRatingResponseDto from(MusicRating entity, boolean updated) {
        return new MusicRatingResponseDto(
                entity.getId(),
                entity.getUserId(),
                entity.getMusic().getId(),
                entity.getScore(),
                entity.getRatedAt(),
                updated);
    }
}
