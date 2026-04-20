package com.deefy.group2.exception;

public class MusicAlreadyOnPlaylistException extends PlaylistDomainException {

    public MusicAlreadyOnPlaylistException(String message) {
        super(message);
    }
}
