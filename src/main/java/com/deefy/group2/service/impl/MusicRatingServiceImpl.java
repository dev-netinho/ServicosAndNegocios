package com.deefy.group2.service.impl;

import com.deefy.group2.dto.response.MusicRatingResponseDto;
import com.deefy.group2.exception.InvalidMusicRatingScoreException;
import com.deefy.group2.exception.MusicNotFoundException;
import com.deefy.group2.model.Music;
import com.deefy.group2.model.MusicRating;
import com.deefy.group2.repository.MusicRatingRepository;
import com.deefy.group2.repository.MusicRepository;
import com.deefy.group2.service.MusicRatingScoreRange;
import com.deefy.group2.service.MusicRatingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class MusicRatingServiceImpl implements MusicRatingService {

    private final MusicRepository musicRepository;
    private final MusicRatingRepository musicRatingRepository;

    public MusicRatingServiceImpl(MusicRepository musicRepository, MusicRatingRepository musicRatingRepository) {
        this.musicRepository = musicRepository;
        this.musicRatingRepository = musicRatingRepository;
    }

    @Override
    @Transactional
    public MusicRatingResponseDto rateMusic(Long userId, Long musicId, Integer score) {
        validateScore(score);
        Music music = musicRepository.findById(musicId).orElseThrow(() -> new MusicNotFoundException(musicId));

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        Optional<MusicRating> existingOpt = musicRatingRepository.findByUserIdAndMusic_Id(userId, musicId);
        boolean updated = existingOpt.isPresent();
        MusicRating entity =
                existingOpt
                        .map(existing -> {
                            existing.setScore(score);
                            existing.setRatedAt(now);
                            return existing;
                        })
                        .orElseGet(() -> newRating(userId, music, score, now));

        MusicRating saved = musicRatingRepository.save(entity);
        return MusicRatingResponseDto.from(saved, updated);
    }

private static MusicRating newRating(Long userId, Music music, Integer score, LocalDateTime ratedAt) {
    return new MusicRating(null, userId, music, score, ratedAt);
}

    private static void validateScore(Integer score) {
        if (score == null) {
            throw InvalidMusicRatingScoreException.nullScore();
        }
        if (score < MusicRatingScoreRange.MIN_INCLUSIVE || score > MusicRatingScoreRange.MAX_INCLUSIVE) {
            throw InvalidMusicRatingScoreException.outOfRange(
                    score, MusicRatingScoreRange.MIN_INCLUSIVE, MusicRatingScoreRange.MAX_INCLUSIVE);
        }
    }
}
