package com.deefy.group2.exception;

public class InvalidPlaylistNameException extends PlaylistDomainException {

    public InvalidPlaylistNameException(String message) {
        super(message);
    }
}
