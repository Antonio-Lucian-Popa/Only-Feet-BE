package com.asusoftware.onlyFeet.poll.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PollVoteResponseDto {
    private UUID id;
    private UUID pollId;
    private UUID userId;
    private String selectedOption;
    private LocalDateTime votedAt;
}
