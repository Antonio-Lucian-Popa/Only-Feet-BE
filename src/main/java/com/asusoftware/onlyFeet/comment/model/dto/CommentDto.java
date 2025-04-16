package com.asusoftware.onlyFeet.comment.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentDto {
    private UUID id;
    private UUID userId;
    private String text;
    private LocalDateTime createdAt;
}