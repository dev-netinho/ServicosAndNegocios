package com.deefy.group2.model;

import com.deefy.group2.exception.InvalidPlaylistNameException;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "publica", nullable = false)
    private boolean ePublica;

    @Column(name = "datacriacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @OneToMany(
            mappedBy = "playlist",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PlaylistMusic> musicas = new ArrayList<>();

    // Construtores
    public Playlist() {

    }

    // Métodos
    public void adicionarMusica(Music musica) {

        boolean jaExiste = this.musicas.stream()
                .anyMatch(pm -> pm.getMusica().getId().equals(musica.getId()));

        if (jaExiste) {
            throw new InvalidPlaylistNameException("Música já está na playlist");
        }

        int ordem = this.musicas.size() + 1;

        PlaylistMusic pm = new PlaylistMusic();
        pm.setPlaylist(this);
        pm.setMusica(musica);
        pm.setOrdem(ordem);

        this.musicas.add(pm);
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isEPublica() {
        return ePublica;
    }

    public void setEPublica(boolean ePublica) {
        this.ePublica = ePublica;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<PlaylistMusic> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<PlaylistMusic> musicas) {
        this.musicas = musicas;
    }
}