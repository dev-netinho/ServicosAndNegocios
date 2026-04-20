package com.deefy.group2.exception;

public class MusicIsNotOnPlaylistException extends RuntimeException {
    public MusicIsNotOnPlaylistException(String message) {
        super(message);
    }
}
