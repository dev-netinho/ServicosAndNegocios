package com.deefy.group2.exception;

/**
 * Lançada quando a nota está fora da faixa permitida ou é nula.
 */
public class InvalidMusicRatingScoreException extends RuntimeException {

    public InvalidMusicRatingScoreException(String message) {
        super(message);
    }

    public static InvalidMusicRatingScoreException nullScore() {
        return new InvalidMusicRatingScoreException("A nota é obrigatória.");
    }

    public static InvalidMusicRatingScoreException outOfRange(int score, int minInclusive, int maxInclusive) {
        return new InvalidMusicRatingScoreException(
                "Nota inválida: "
                        + score
                        + ". A faixa válida é entre "
                        + minInclusive
                        + " e "
                        + maxInclusive
                        + " (inclusive).");
    }
}
