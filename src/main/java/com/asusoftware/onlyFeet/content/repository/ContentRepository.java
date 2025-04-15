package com.asusoftware.onlyFeet.content.repository;

import com.asusoftware.onlyFeet.content.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentRepository extends JpaRepository<Content, UUID> {
    List<Content> findByCreatorId(UUID creatorId);
    List<Content> findByVisibility(String visibility);
}