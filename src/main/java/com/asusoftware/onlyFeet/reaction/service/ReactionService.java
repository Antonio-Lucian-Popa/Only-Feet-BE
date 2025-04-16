package com.asusoftware.onlyFeet.reaction.service;

import com.asusoftware.onlyFeet.reaction.model.Reaction;
import com.asusoftware.onlyFeet.reaction.model.ReactionType;
import com.asusoftware.onlyFeet.reaction.model.dto.ReactionCountDto;
import com.asusoftware.onlyFeet.reaction.model.dto.ReactionRequestDto;
import com.asusoftware.onlyFeet.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;

    public void addOrUpdateReaction(ReactionRequestDto dto) {
        Reaction reaction = reactionRepository
                .findByUserIdAndContentId(dto.getUserId(), dto.getContentId())
                .map(existing -> {
                    existing.setType(dto.getType());
                    return existing;
                })
                .orElseGet(() -> Reaction.builder()
                        .userId(dto.getUserId())
                        .contentId(dto.getContentId())
                        .type(dto.getType())
                        .build());

        reactionRepository.save(reaction);
    }

    public List<ReactionCountDto> getReactionStats(UUID contentId) {
        List<Reaction> reactions = reactionRepository.findByContentId(contentId);

        return Arrays.stream(ReactionType.values())
                .map(type -> new ReactionCountDto(
                        type,
                        (int) reactions.stream().filter(r -> r.getType() == type).count()
                ))
                .toList();
    }

    public void removeReaction(UUID userId, UUID contentId) {
        reactionRepository.findByUserIdAndContentId(userId, contentId)
                .ifPresent(reactionRepository::delete);
    }

}
