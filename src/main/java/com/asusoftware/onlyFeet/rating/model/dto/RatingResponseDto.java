package com.asusoftware.onlyFeet.rating.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RatingResponseDto {
    private UUID contentId;
    private double averageRating;
    private int totalRatings;
}
