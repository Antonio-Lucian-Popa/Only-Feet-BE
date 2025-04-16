package com.asusoftware.onlyFeet.reaction.model.dto;

import com.asusoftware.onlyFeet.reaction.model.ReactionType;
import lombok.Data;

import java.util.UUID;

@Data
public class ReactionRequestDto {
    private UUID userId;
    private UUID contentId;
    private ReactionType type;
}

