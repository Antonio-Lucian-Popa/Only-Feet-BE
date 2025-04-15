package com.asusoftware.onlyFeet.poll.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class PollCreateRequestDto {
    private UUID creatorId;
    private String question;
    private List<String> options;
    private LocalDateTime expiresAt;
}
