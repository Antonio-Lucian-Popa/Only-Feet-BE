package com.asusoftware.onlyFeet.content.controller;

import com.asusoftware.onlyFeet.content.model.dto.LikeRequestDto;
import com.asusoftware.onlyFeet.content.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/toggle")
    public ResponseEntity<Void> toggleLike(@RequestBody LikeRequestDto dto) {
        likeService.toggleLike(dto.getUserId(), dto.getContentId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/{contentId}")
    public ResponseEntity<Integer> getLikeCount(@PathVariable UUID contentId) {
        return ResponseEntity.ok(likeService.countLikes(contentId));
    }
}

