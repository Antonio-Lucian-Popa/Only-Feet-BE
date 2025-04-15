package com.asusoftware.onlyFeet.rating.repository;

import com.asusoftware.onlyFeet.rating.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByUserIdAndContentId(UUID userId, UUID contentId);
    List<Rating> findByContentId(UUID contentId);
}
