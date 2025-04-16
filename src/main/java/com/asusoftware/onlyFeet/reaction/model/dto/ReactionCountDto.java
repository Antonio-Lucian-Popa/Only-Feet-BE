package com.asusoftware.onlyFeet.reaction.model.dto;

import com.asusoftware.onlyFeet.reaction.model.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReactionCountDto {
    private ReactionType type;
    private int count;
}

