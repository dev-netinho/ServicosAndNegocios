package com.deefy.group2.repository;

import com.deefy.group2.model.Playlist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository {
    Playlist save(Playlist playlist);

    Optional<Playlist> findById(Long id);
    List<Playlist> findByOwnerId(Long ownerId);
}