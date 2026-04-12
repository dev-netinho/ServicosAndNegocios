package com.deefy.group2.repository;

import com.deefy.group2.model.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long>, JpaSpecificationExecutor<Music> {

    @EntityGraph(attributePaths = {"album", "album.artist"})
    @Override
    Page<Music> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"album", "album.artist"})
    @Override
    List<Music> findAll(Specification<Music> spec, Sort sort);
}
