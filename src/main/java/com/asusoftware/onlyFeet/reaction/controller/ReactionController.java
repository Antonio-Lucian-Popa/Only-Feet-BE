package com.asusoftware.onlyFeet.reaction.controller;

import com.asusoftware.onlyFeet.reaction.model.dto.ReactionCountDto;
import com.asusoftware.onlyFeet.reaction.model.dto.ReactionRequestDto;
import com.asusoftware.onlyFeet.reaction.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<Void> react(@RequestBody ReactionRequestDto dto) {
        reactionService.addOrUpdateReaction(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/content/{contentId}")
    public ResponseEntity<List<ReactionCountDto>> getStats(@PathVariable UUID contentId) {
        return ResponseEntity.ok(reactionService.getReactionStats(contentId));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeReaction(
            @RequestParam UUID userId,
            @RequestParam UUID contentId
    ) {
        reactionService.removeReaction(userId, contentId);
        return ResponseEntity.noContent().build();
    }

}
