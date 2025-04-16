package com.asusoftware.onlyFeet.content.service;

import com.asusoftware.onlyFeet.content.model.Like;
import com.asusoftware.onlyFeet.content.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public void toggleLike(UUID userId, UUID contentId) {
        Optional<Like> existing = likeRepository.findByUserIdAndContentId(userId, contentId);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
        } else {
            Like like = Like.builder()
                    .userId(userId)
                    .contentId(contentId)
                    .createdAt(LocalDateTime.now())
                    .build();
            likeRepository.save(like);
        }
    }

    public int countLikes(UUID contentId) {
        return likeRepository.countByContentId(contentId);
    }
}

