package com.deefy.group2.exception;

/**
 * Lançada quando a música referenciada na avaliação não existe.
 */
public class MusicNotFoundException extends RuntimeException {

    public MusicNotFoundException(Long musicId) {
        super("Música não encontrada: id=" + musicId);
    }
}
