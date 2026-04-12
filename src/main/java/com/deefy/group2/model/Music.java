package com.deefy.group2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Mapeia a tabela {@code musica} conforme {@code deefy_schema.sql} (Grupo 1).
 */
@Entity
@Table(name = "musica")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 300)
    private String title;

    @Column(name = "artista", nullable = false, length = 200)
    private String artist;

    @Column(name = "genero", nullable = false, length = 100)
    private String genre;

    @Column(name = "duracao", nullable = false)
    private Integer durationSeconds;

    @Column(name = "preview_url", length = 500)
    private String previewUrl;

    @Column(name = "capa_url", length = 500)
    private String coverUrl;

    @Column(name = "id_externo", length = 100)
    private String externalId;

    @Column(name = "criado_em", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    protected Music() {
    }

    /** Uso em testes unit\u00e1rios (entidade desanexada). */
    public Music(Long id, String title, String artist, String genre, Integer durationSeconds) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getExternalId() {
        return externalId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
