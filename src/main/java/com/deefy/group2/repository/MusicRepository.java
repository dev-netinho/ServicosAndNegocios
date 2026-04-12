package com.deefy.group2.repository;

import com.deefy.group2.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MusicRepository extends JpaRepository<Music, Long>, JpaSpecificationExecutor<Music> {
}
