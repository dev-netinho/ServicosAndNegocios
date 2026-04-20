package com.deefy.group2.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 *     Esta classe serve para lidar com a chave composta em PlaylistMusica.java
 * </p>
 */
@Embeddable
public class PlaylistMusicaId implements Serializable {

    private Long playlistId;
    private Long musicaId;

    public PlaylistMusicaId() {}

    public PlaylistMusicaId(Long playlistId, Long musicaId) {
        this.playlistId = playlistId;
        this.musicaId = musicaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaylistMusicaId)) return false;
        PlaylistMusicaId that = (PlaylistMusicaId) o;
        return Objects.equals(playlistId, that.playlistId) &&
                Objects.equals(musicaId, that.musicaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, musicaId);
    }
}