package com.deefy.group2.exception;

public class MusicAlreadyOnPlaylistException extends RuntimeException {
    public MusicAlreadyOnPlaylistException(String message) {
        super(message);
    }
}
