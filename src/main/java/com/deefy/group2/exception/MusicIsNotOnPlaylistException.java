package com.deefy.group2.exception;

public class MusicIsNotOnPlaylistException extends PlaylistDomainException {

    public MusicIsNotOnPlaylistException(String message) {
        super(message);
    }
}
