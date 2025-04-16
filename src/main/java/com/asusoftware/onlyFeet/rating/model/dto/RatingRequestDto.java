package com.asusoftware.onlyFeet.rating.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RatingRequestDto {
    private UUID userId;
    private UUID contentId;
    private int score; // 1-5
}