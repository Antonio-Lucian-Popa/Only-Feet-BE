package com.asusoftware.onlyFeet.reaction.repository;

import com.asusoftware.onlyFeet.reaction.model.Reaction;
import com.asusoftware.onlyFeet.reaction.model.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, UUID> {
    Optional<Reaction> findByUserIdAndContentId(UUID userId, UUID contentId);
    List<Reaction> findByContentId(UUID contentId);
    int countByContentIdAndType(UUID contentId, ReactionType type);

}

