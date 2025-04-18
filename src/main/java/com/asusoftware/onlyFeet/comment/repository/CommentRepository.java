package com.asusoftware.onlyFeet.comment.repository;

import com.asusoftware.onlyFeet.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByContentId(UUID contentId);
}
