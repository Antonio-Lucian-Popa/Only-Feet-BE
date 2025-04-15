package com.asusoftware.onlyFeet.content.repository;

import com.asusoftware.onlyFeet.content.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    Optional<Like> findByUserIdAndContentId(UUID userId, UUID contentId);
    int countByContentId(UUID contentId);
}
