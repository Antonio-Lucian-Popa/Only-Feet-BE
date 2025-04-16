package com.asusoftware.onlyFeet.comment.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentRequestDto {
    private UUID userId;
    private UUID contentId;
    private String text;
}
