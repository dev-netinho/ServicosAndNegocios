package com.deefy.group2.model;

import jakarta.persistence.*;

/**
 * Esta classe segue o <strong>modelo do banco de dados (PLAYLIST_MUSICA)</strong> e serve como intermediária da Playlist.java
 */
@Entity
@Table(name = "playlist_musica")
public class PlaylistMusic {

    @EmbeddedId
    private PlaylistMusicaId id;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne
    @MapsId("musicaId")
    @JoinColumn(name = "musica_id")
    private Music musica;

    private int ordem;

    // Getters e Setters
    public PlaylistMusicaId getId() {
        return id;
    }

    public void setId(PlaylistMusicaId id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Music getMusica() {
        return musica;
    }

    public void setMusica(Music musica) {
        this.musica = musica;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
}
