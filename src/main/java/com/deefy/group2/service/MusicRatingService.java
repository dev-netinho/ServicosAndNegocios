package com.deefy.group2.service;

import com.deefy.group2.dto.response.MusicRatingResponseDto;

public interface MusicRatingService {

    /**
     * Registra ou atualiza a avaliação da música pelo usuário.
     *
     * @param userId  identificador do usuário autenticado
     * @param musicId identificador da música
     * @param score   nota entre {@link MusicRatingScoreRange#MIN_INCLUSIVE} e {@link MusicRatingScoreRange#MAX_INCLUSIVE}
     * @return dados da avaliação persistida
     */
    MusicRatingResponseDto rateMusic(Long userId, Long musicId, Integer score);
}
