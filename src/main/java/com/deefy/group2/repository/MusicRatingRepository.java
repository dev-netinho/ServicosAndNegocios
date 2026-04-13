package com.deefy.group2.repository;

import com.deefy.group2.model.MusicRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MusicRatingRepository extends JpaRepository<MusicRating, Long> {

    Optional<MusicRating> findByUserIdAndMusic_Id(Long userId, Long musicId);
}
