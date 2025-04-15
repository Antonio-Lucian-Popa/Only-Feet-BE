package com.asusoftware.onlyFeet.poll.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PollVoteRequestDto {
    private UUID pollId;
    private UUID userId;
    private String selectedOption;
}
