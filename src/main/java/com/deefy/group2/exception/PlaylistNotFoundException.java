package com.deefy.group2.exception;

public class PlaylistNotFoundException extends RuntimeException {

    public PlaylistNotFoundException(Long playlistId) {
        super("Playlist not found with id: " + playlistId);
    }
}
